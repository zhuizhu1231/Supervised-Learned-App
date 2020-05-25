package com.example.demo.data.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.demo.converter.MyConverter;
import com.example.demo.data.dao.MilepostDao;
import com.example.demo.data.database.MyDataBase;
import com.example.demo.data.datasource.MilepostDataSource;
import com.example.demo.data.model.Milepost;
import com.example.demo.data.model.User;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class MilepostRepository {

    private static volatile MilepostRepository instance;

    private MilepostDataSource dataSource;

    private  MyDataBase myDataBase;
    private MilepostDao milepostDao;
    Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_UPDATE_SUCCESS:
                    updateMilepostStatusById((List<Bean<com.example.demo.data.entities.Milepost>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_DELETE_SUCCESS:
                    removeMilepostById((List<Bean<com.example.demo.data.entities.Milepost>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_DELETE_FAIL:
                    break;
            }
        }
    };

    // private constructor : singleton access
    private MilepostRepository(MilepostDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static MilepostRepository getInstance(MilepostDataSource dataSource) {
        if (instance == null) {
            instance = new MilepostRepository(dataSource);
        }
        return instance;
    }
    public MilepostRepository(Application context) {
        setCacheDataSource(context);
    }
    public void setCacheDataSource(Context context) {
        myDataBase = MyDataBase.getInstance(context.getApplicationContext());
        milepostDao= myDataBase.getMilepostDao();
    }
    public static MilepostRepository getInstance(Application activity) {
        if (instance == null) {
            instance = new MilepostRepository(activity);
            instance.dataSource=new MilepostDataSource();
        }
        return instance;
    }

    public void netQueryServiceTimeStamp(Handler handler, User user) {
        dataSource.QueryMilepostServiceTimeStamp(handler, User.toEntity(user));
    }

    public Timestamp getMaxCacheMilepostSycTimeStamp() {
        return milepostDao.getMaxSycTimeStamp();
    }

    public List<Milepost> getNotSYCMilepost() {
        return milepostDao.getNotSYCMilepost();
    }

    public void netSYCNotSyMilepost(Handler handler, User user, List<Milepost> notSYCMilepost) {
        List<Milepost> insertMilepost=new ArrayList<>();
        List<Milepost> updateMilepost=new ArrayList<>();
        List<Milepost> deleteMilepost=new ArrayList<>();
        for(Milepost note:notSYCMilepost){
            if(note.getStatus()== StringUtil.LOCAL_INSERT)
                insertMilepost.add(note);
            else if(note.getStatus()==StringUtil.LOCAL_UPDATE)
                updateMilepost.add(note);
            else
                deleteMilepost.add(note);
        }
        if(insertMilepost.size()>0)
            dataSource.insertNotSycMilepost(handler,User.toEntity(user),Milepost.packageToBean(insertMilepost));
        if(updateMilepost.size()>0);
            dataSource.updateNotSycMilepost(handler,User.toEntity(user),Milepost.packageToBean(updateMilepost));
        if(deleteMilepost.size()>0);
            dataSource.deleteNotSycMilepost(handler,User.toEntity(user),Milepost.packageToBean(deleteMilepost));
    }

    public void updateMilepostStatusById(List<Bean<com.example.demo.data.entities.Milepost>> obj) {
        for(Bean<com.example.demo.data.entities.Milepost> bean:obj){
            Integer offlineId = bean.getOfflineId();
            Milepost milepost=this.getMilepostById(offlineId);
            milepost.setStatus(StringUtil.SYC);
            milepost.setDbId(bean.getData().getId());
            milepost.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
            new MilepostRepository.UpdateAsyncMilepostCache(milepostDao).execute(milepost);
        }
    }

    private Milepost getMilepostById(Integer id) {
        return milepostDao.getMilepostByIdStatic(id);
    }

    public void removeMilepostById(List<Bean<com.example.demo.data.entities.Milepost>> obj) {
        for(Bean<com.example.demo.data.entities.Milepost>bean:obj){
            milepostDao.deleteMilepostById(bean.getOfflineId());
        }
    }

    public void clearCache() {
        milepostDao.clearMilepostStatic();
    }

    public void netQueryMilepostList(Handler handler, User user) {
        dataSource. netQueryMilepostList(handler, User.toEntity( user),MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheMilepostSycTimeStamp())));
    }

    public void cacheMilepostListData(List<Bean<com.example.demo.data.entities.Milepost>> obj) {
        List<Milepost> unpack = com.example.demo.data.entities.Milepost.unpack(obj);
        for(Milepost m:unpack){
            //insertMilepost(m);
            Milepost object = milepostDao.getMilepostByIdStatic(m.getDbId());
            if( object==null)
                new InsertAsyncMilepostCache(milepostDao).execute(m);
            else
                new UpdateAsyncMilepostCache(milepostDao).execute( m);
        }
    }

    public void insertMilepost(Milepost milepost) {
        new MilepostRepository.InsertAsyncMilepost(milepostDao).execute(milepost);
    }

    public LiveData<List<Milepost>> getAllMilepost() {
        return milepostDao.getAllMilepost();
    }

    public LiveData<List<Milepost>> getLiveMilepostList() {
        return milepostDao.getLiveMilepostList(Tool.createNewTimeStamp());
    }

    public Milepost getLiveMilepostLately() {
        return milepostDao.getLiveMilepostLately(Tool.createNewTimeStamp());
    }

    public void updateMilepost(Milepost milepost) {
        new MilepostRepository.UpdateAsyncMilepost(milepostDao).execute(milepost);
    }

    public void statusDeleteMilepost(Milepost obj) {
        if(obj.getStatus()!=StringUtil.LOCAL_INSERT){
            obj.setStatus(StringUtil.LOCAL_DELETE);
            new MilepostRepository.UpdateAsyncMilepost(milepostDao).execute(obj);
        } else
            new MilepostRepository.DeleteAsyncMilepost(milepostDao).execute(obj);
    }

     class InsertAsyncMilepost extends AsyncTask<Milepost,Void,Void> {
        MilepostDao dao;
        public InsertAsyncMilepost(MilepostDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Milepost ...mileposts) {
            dao.insertMilepost(mileposts);
            netQueryMilepost(handler);
            return  null;

        }
    }

    class UpdateAsyncMilepost extends AsyncTask<Milepost,Void,Void> {
        MilepostDao dao;
        public UpdateAsyncMilepost(MilepostDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Milepost... mileposts) {
            dao.updateMilepost(mileposts);
            netQueryMilepost(handler);
            return null;
        }
    }

    class InsertAsyncMilepostCache extends AsyncTask<Milepost,Void,Void> {
        MilepostDao dao;
        public InsertAsyncMilepostCache(MilepostDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Milepost ...mileposts) {
            dao.insertMilepost(mileposts);

            return  null;

        }
    }

    class UpdateAsyncMilepostCache extends AsyncTask<Milepost,Void,Void> {
        MilepostDao dao;
        public UpdateAsyncMilepostCache(MilepostDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Milepost... mileposts) {
            dao.updateMilepost(mileposts);
            return null;
        }
    }

    private void netQueryMilepost(Handler handler){
        List<Milepost> notSYCMilepost = getNotSYCMilepost();
        netSYCNotSyMilepost(handler, StringUtil.getUser(), notSYCMilepost);
    }
    static class DeleteAsyncMilepost extends AsyncTask<Milepost,Void,Void>{
        MilepostDao dao;
        public DeleteAsyncMilepost(MilepostDao scheduleDao) {
            this.dao=scheduleDao;
        }
        @Override
        protected Void doInBackground(Milepost... mileposts) {
            dao.deleteMilepost(mileposts);
            return null;
        }
    }
}
