package com.example.demo.data.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import com.example.demo.converter.MyConverter;
import com.example.demo.data.dao.Study_roomDao;
import com.example.demo.data.dao.Study_room_messageDao;
import com.example.demo.data.dao.User_in_study_roomDao;
import com.example.demo.data.database.MyDataBase;
import com.example.demo.data.datasource.StudyRoomDataSource;
import com.example.demo.data.model.Study_room;
import com.example.demo.data.model.Study_room_message;
import com.example.demo.data.model.User;
import com.example.demo.data.model.User_in_study_room;
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
public class StudyRoomRepository {

    private static volatile StudyRoomRepository instance;

    private StudyRoomDataSource dataSource;

    private  MyDataBase myDataBase;
    private Study_roomDao studyRoomDao;
    private Study_room_messageDao messageDao;
    private User_in_study_roomDao roomMemberDao;
   
    // private constructor : singleton access
    private StudyRoomRepository(StudyRoomDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static StudyRoomRepository getInstance(StudyRoomDataSource dataSource) {
        if (instance == null) {
            instance = new StudyRoomRepository(dataSource);
        }
        return instance;
    }
    public StudyRoomRepository(Context context) {
       setCacheDataSource(context);
    }

    public static StudyRoomRepository getInstance(Application activity) {
        if (instance == null) {
            instance = new StudyRoomRepository(activity);
        }
        return instance;
    }
    public void setCacheDataSource(Context context) {
        myDataBase = MyDataBase.getInstance(context.getApplicationContext());
        studyRoomDao=myDataBase.getStudy_roomDao();
        messageDao=myDataBase.getStudy_room_messageDao();
        roomMemberDao=myDataBase.getUser_in_study_roomDao();
        dataSource=new StudyRoomDataSource();
    }

    public User_in_study_room getUserByRoomIdUserId(Integer roomId, Integer sendUserId) {
        return roomMemberDao.getUserByRoomIdUserId(roomId, sendUserId);
    }

    public LiveData<List<Study_room>> findStudyRoomList() {
        return studyRoomDao.getAllStudy_room();
    }

    public void saveMessage(Study_room_message message) {
        messageDao.insertStudy_room_message(message);
    }

    public void clearCache() {
        messageDao.clearCacheStatic();

        roomMemberDao.clearCacheStatic();
        studyRoomDao.clearCacheStatic();
    }

    public void netQueryStudyRoomList(Handler handler, User user) {
        dataSource.netQueryStudyRoomList(handler,User.toEntity(user), MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheStudyRoomSycTimeStamp())));

    }

    private Timestamp getMaxCacheStudyRoomSycTimeStamp() {
        return studyRoomDao.getMaxSycTimeStamp();
    }

    public void cacheRoomListData(List<Bean<com.example.demo.data.entities.Study_room>> obj) {
        List<Study_room> unpack = com.example.demo.data.entities.Study_room.unpack(obj);
        for(Study_room r:unpack){
            Study_room object = studyRoomDao.getStudyRoomByDbIdStatic(r.getDbId());

            if( object==null)
                new InsertAsyncStudy_roomCache(studyRoomDao).execute(r);
            else
                new UpdateAsyncStudy_roomCache(studyRoomDao).execute(r);
            //taskDao.insertTask(cacheTaskPrepareNetBack(r));//
        }
    }

    public LiveData<List<Study_room>> getRoomListByRemarkNameLikeOrDbIdLike(String titleLike, Integer id) {
        return studyRoomDao.getRoomListByRemarkLikeOrDbIdLike(titleLike,id);
    }

    public void netSearchRoomListByNameLikeOrDbIdLike(Handler handler, String key) {
        dataSource.netSearchRoomListByNameLikeOrDbIdLike(handler,key);
    }

    public void netUpdateRoom(Handler handler, Study_room room) {
        ArrayList<Study_room> roomList=new ArrayList<>();
        roomList.add(room);
        dataSource.netUpdateRoom(handler, User.toEntity(StringUtil.getUser()),Study_room.packageToBean(roomList));
    }

    public void netSaveRoom(Handler handler, Study_room studyRoom) {
        dataSource.netCreateRoom(handler, User.toEntity(StringUtil.getUser()),Study_room.toEntity(studyRoom));
    }

    public LiveData<List<User_in_study_room>> getMemberByRoomId(Integer roomDbId) {
        return roomMemberDao.getMemberByRoomId(roomDbId);
    }

    public void netQueryMemberListByRoomId(Handler handler, Integer dbId) {
        dataSource.netQueryMemberListByRoomId( handler,User.toEntity( StringUtil.getUser()), dbId);
    }

    public void clearCacheByRoomId(Integer roomId) {
        roomMemberDao.clearCacheByRoomId( roomId);
    }

    public void cacheRoomIdMemberListData(List<Bean<com.example.demo.data.entities.User_in_study_room>> obj) {
        List<User_in_study_room> unpack = com.example.demo.data.entities.User_in_study_room.unpack(obj);
        for(User_in_study_room r:unpack){
            User_in_study_room object = roomMemberDao.getStudyRoomMemberByDbIdStatic(r.getDbId());

            if( object==null)
                new InsertAsyncUser_in_study_roomCache(roomMemberDao).execute(r);
            else
                new UpdateAsyncUser_in_study_roomCache(roomMemberDao).execute(r);
            //taskDao.insertTask(cacheTaskPrepareNetBack(r));//
        }
    }

    public LiveData<List<Study_room_message>> getMessageListByRoomDbId(Integer dbId) {
        return messageDao.getMessageListByRoomDbId(dbId);
    }

    public void netRefleshRoomByDbId(Handler handler,Integer dbId) {
        dataSource.netRefleshRoomByDbId(handler,dbId);
    }

    public void netDeleteRoom(Handler handler, Study_room room) {
        ArrayList<Study_room> roomList=new ArrayList<>();
        roomList.add(room);
        dataSource.netDeleteRoom(handler, User.toEntity(StringUtil.getUser()),Study_room.packageToBean(roomList));
    }

    public Study_room getRoomByDbId(Integer dbId) {
        return studyRoomDao.getStudyRoomByDbIdStatic(dbId);
    }

    public void addRoomByDbId(Handler handler, Integer dbId) {
        dataSource.addRoomByDbId(handler, dbId);
    }

    public void removeRoom(Study_room room) {
        studyRoomDao.deleteStudy_room(room);
    }

    static class InsertAsyncUser_in_study_roomCache extends AsyncTask<User_in_study_room,Void,Void> {
        User_in_study_roomDao dao;

        public InsertAsyncUser_in_study_roomCache(User_in_study_roomDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(User_in_study_room ...friends) {
            dao.insertUser_in_study_room(friends);
            return null;

        }
    }

    static class UpdateAsyncUser_in_study_roomCache extends AsyncTask<User_in_study_room,Void,Void> {
        User_in_study_roomDao dao;

        public UpdateAsyncUser_in_study_roomCache(User_in_study_roomDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(User_in_study_room... Study_rooms) {
            dao.updateUser_in_study_room(Study_rooms);
            return null;
        }
    }

    class InsertAsyncStudy_room extends AsyncTask<Study_room,Void,Void> {
        Study_roomDao dao;

        public InsertAsyncStudy_room(Study_roomDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Study_room ...friends) {
//Todo:
            dao.insertStudy_room(friends);
            return null;

        }
    }

    static class UpdateAsyncStudy_room extends AsyncTask<Study_room,Void,Void> {
        Study_roomDao dao;

        public UpdateAsyncStudy_room(Study_roomDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Study_room... Study_rooms) {
            dao.updateStudy_room(Study_rooms);
            return null;
        }
    }

    class InsertAsyncStudy_roomCache extends AsyncTask<Study_room,Void,Void> {
        Study_roomDao dao;

        public InsertAsyncStudy_roomCache(Study_roomDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Study_room ...friends) {
            dao.insertStudy_room(friends);
            return null;

        }
    }

    static class UpdateAsyncStudy_roomCache extends AsyncTask<Study_room,Void,Void> {
        Study_roomDao dao;

        public UpdateAsyncStudy_roomCache(Study_roomDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Study_room... Study_rooms) {
            dao.updateStudy_room(Study_rooms);
            return null;
        }
    }

    static class DeleteAsyncStudy_room extends AsyncTask<Study_room,Void,Void>{
        Study_roomDao dao;

        public DeleteAsyncStudy_room(Study_roomDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Study_room... Study_rooms) {
            dao.deleteStudy_room(Study_rooms);
            return null;
        }
    }
}
