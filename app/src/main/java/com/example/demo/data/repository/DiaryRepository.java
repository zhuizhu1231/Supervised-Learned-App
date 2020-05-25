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
import com.example.demo.data.dao.ScheduleDao;
import com.example.demo.data.dao.Schedule_in_labelDao;
import com.example.demo.data.dao.Schedule_labelDao;
import com.example.demo.data.database.MyDataBase;
import com.example.demo.data.datasource.DiaryDataSource;
import com.example.demo.data.model.Schedule;
import com.example.demo.data.model.Schedule_in_label;
import com.example.demo.data.model.Schedule_label;
import com.example.demo.data.model.User;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class DiaryRepository {

    private static volatile DiaryRepository instance;

    private DiaryDataSource dataSource;

    private  MyDataBase myDataBase;
    private ScheduleDao scheduleDao;
    private Schedule_labelDao labelDao;
    private Schedule_in_labelDao relationDao;
    private   Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                //Schedule_in_label
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_UPDATE_SUCCESS:
                    updateScheduleRelationStatusById((List<Bean<com.example.demo.data.entities.Schedule_in_label>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_DELETE_SUCCESS:
                    removeScheduleRelationById((List<Bean<com.example.demo.data.entities.Schedule_in_label>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_DELETE_FAIL:
                    System.out.println("x");
                    break;

                //Schedule_label
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_UPDATE_SUCCESS:
                    updateScheduleLabelStatusById((List<Bean<com.example.demo.data.entities.Schedule_label>>) msg.obj);
                    
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_DELETE_SUCCESS:
                    removeScheduleLabelById((List<Bean<com.example.demo.data.entities.Schedule_label>>) msg.obj);
                    
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_DELETE_FAIL:
                    break;

                //Schedule
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_INSERT_SUCCESS:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_UPDATE_SUCCESS:
                    updateScheduleStatusById((List<Bean<com.example.demo.data.entities.Schedule>>) msg.obj);
               //     sycRelation();
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_DELETE_SUCCESS:
                    removeScheduleById((List<Bean<com.example.demo.data.entities.Schedule>>) msg.obj);
                    break;
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_INSERT_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_UPDATE_FAIL:
                case StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_DELETE_FAIL:
                    break;

            }
        }

    };
//    List<Schedule> notSYCSchedules = diaryRepository.getNotSYCSchedules();
//        diaryRepository.netSYCNotSycSchedule(handler, user, notSYCSchedules);
//    List<com.example.demo.data.model.Schedule_label> notSYCSchedulesLabel = diaryRepository.getNotSYCSchedulesLabel();
//        diaryRepository.netSYCNotSycScheduleLabel(handler, user,notSYCSchedulesLabel );
//List<Schedule_in_label> notSYCSchedulesRelation = diaryRepository.getNotSYCSchedulesRelation();
//        diaryRepository.netSYCNotSycScheduleRelation(handler, user, notSYCSchedulesRelation);

    // private constructor : singleton access
    private DiaryRepository(DiaryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static DiaryRepository getInstance(DiaryDataSource dataSource) {
        if (instance == null) {
            instance = new DiaryRepository(dataSource);

        }
        return instance;
    }
    public DiaryRepository(Context context) {
       setCacheDataSource(context);
    }
    public static DiaryRepository getInstance(Application application) {
        if (instance == null) {
            instance = new DiaryRepository(application);
            instance.dataSource=new DiaryDataSource();
        };
        return instance;
    }
    public void setCacheDataSource(Context context) {
        myDataBase = MyDataBase.getInstance(context.getApplicationContext());
        scheduleDao= myDataBase.getScheduleDao();
        labelDao=myDataBase.getSchedule_labelDao();
        relationDao=myDataBase.getSchedule_in_labelDao();
    }

    public Timestamp getMaxCacheScheduleSycTimeStamp() {
        return scheduleDao.getMaxSycTimeStamp();

    }

    public void netQueryScheduleServiceTimeStamp(Handler handler, User user) {
         dataSource.QueryScheduleServiceTimeStamp(handler, User.toEntity(user));
    }

    public void clearCache() {
        //Todo:清除数据库数据
        scheduleDao.clearScheduleStatic();
        labelDao.clearScheduleLabelStatic();
        relationDao.clearScheduleRelationStatic();
    }

    public List<Schedule> getNotSYCSchedules() {
        return scheduleDao.getNotSycSchedules();
    }

    public void netSYCNotSycSchedule(Handler handler, User user, List<Schedule> notSYCSchedules ) {//MutableLiveData<ScheduleRelationPrepare> schedule_relation_prepare
        List<Schedule> insertSchedules=new ArrayList<>();
        List<Schedule> updateSchedules=new ArrayList<>();
        List<Schedule> deleteSchedules=new ArrayList<>();
        for(Schedule schedule:notSYCSchedules){
            if(schedule.getStatus()== StringUtil.LOCAL_INSERT)
                insertSchedules.add(schedule);
            else if(schedule.getStatus()==StringUtil.LOCAL_UPDATE)
                updateSchedules.add(schedule);
            else
                deleteSchedules.add(schedule);
        }

            dataSource.insertNotSycSchedule(handler,User.toEntity(user),Schedule.packageToBean(insertSchedules));//schedule_relation_prepare
       // else schedule_relation_prepare.getValue().timerRelationScheduleLabelPrepare= true;

        //Todo:
          dataSource.updateNotSycSchedule(handler,User.toEntity(user),Schedule.packageToBean(updateSchedules));

            dataSource.deleteNotSycSchedule(handler,User.toEntity(user),Schedule.packageToBean(deleteSchedules));

    }

    public void updateScheduleStatusById(List<Bean<com.example.demo.data.entities.Schedule>> beans) {
        for(Bean<com.example.demo.data.entities.Schedule> bean:beans){
            Integer offlineId = bean.getOfflineId();
            Schedule schedule=this.getScheduleById(offlineId);
            schedule.setStatus(StringUtil.SYC);
            schedule.setDbId(bean.getData().getId());
            schedule.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
            //new DiaryRepository.UpdateAsyncSchedule(scheduleDao).execute(schedule);
            scheduleDao.updateSchedule(schedule);
        }
    }

    private Schedule getScheduleById(Integer offlineId) {
        return scheduleDao.getScheduleByIdStatic(offlineId);
    }

    public void netQueryScheduleLabelServiceTimeStamp(Handler handler, User user) {
        dataSource.QueryScheduleLabelServiceTimeStamp(handler, User.toEntity(user));
    }

    public void netQueryScheduleRelationServiceTimeStamp(Handler handler, User user) {
        dataSource.QueryScheduleRelationServiceTimeStamp(handler, User.toEntity(user));
    }

    public Timestamp getMaxCacheScheduleLabelSycTimeStamp() {
        return labelDao.getMaxSycTimeStamp();
    }

    public Timestamp getMaxCacheScheduleRelationSycTimeStamp() {
        return relationDao.getMaxSycTimeStamp();
    }

    public void removeScheduleById(List<Bean<com.example.demo.data.entities.Schedule>> obj) {
        for(Bean<com.example.demo.data.entities.Schedule>bean:obj){
            scheduleDao.deleteScheduleById(bean.getOfflineId());
        }
    }

    public List<Schedule_label> getNotSYCSchedulesLabel() {
        return labelDao.getNotSYCSchedulesLabel();
    }

    public void netSYCNotSycScheduleLabel(Handler handler, User user, List<Schedule_label> notSYCSchedulesLabel) {//, MutableLiveData<ScheduleRelationPrepare> schedule_relation_prepare
        List<Schedule_label> insertSchedulesLabel=new ArrayList<>();
        List<Schedule_label> updateSchedulesLabel=new ArrayList<>();
        List<Schedule_label> deleteSchedulesLabel=new ArrayList<>();
        for(Schedule_label schedule:notSYCSchedulesLabel){
            if(schedule.getStatus()== StringUtil.LOCAL_INSERT)
                insertSchedulesLabel.add(schedule);
            else if(schedule.getStatus()==StringUtil.LOCAL_UPDATE)
                updateSchedulesLabel.add(schedule);
            else
                deleteSchedulesLabel.add(schedule);
        }

            dataSource.insertNotSycScheduleLabel(handler,User.toEntity(user),Schedule_label.packageToBean(insertSchedulesLabel));//,schedule_relation_prepare

            dataSource.updateNotSycScheduleLabel(handler,User.toEntity(user),Schedule_label.packageToBean(updateSchedulesLabel));

            dataSource.deleteNotSycScheduleLabel(handler,User.toEntity(user),Schedule_label.packageToBean(deleteSchedulesLabel));
    }

    public void updateScheduleLabelStatusById(List<Bean<com.example.demo.data.entities.Schedule_label>> obj) {
        for(Bean<com.example.demo.data.entities.Schedule_label> bean:obj){
            Integer offlineId = bean.getOfflineId();
            Schedule_label label=this.getScheduleLabelById(offlineId);
            label.setStatus(StringUtil.SYC);
            label.setDbId(bean.getData().getId());
            label.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
           // new DiaryRepository.UpdateAsyncScheduleLabel(labelDao).execute(label);
            labelDao.updateSchedule_label(label);
        }
    }

    private Schedule_label getScheduleLabelById(Integer offlineId) {
        return labelDao.getLabelByIdStatic(offlineId);
    }

    public void removeScheduleLabelById(List<Bean<com.example.demo.data.entities.Schedule_label>> obj) {
        for(Bean<com.example.demo.data.entities.Schedule_label>bean:obj){
            labelDao.deleteScheduleLabelById(bean.getOfflineId());
        }
    }

    public List<Schedule_in_label> getNotSYCSchedulesRelation() {
        return relationDao.getNotSYCSchedulesRelation();
    }

    public void netSYCNotSycScheduleRelation(Handler handler, User user, List<Schedule_in_label> notSYCRelations) {
        List<Schedule_in_label> insertRelations=new ArrayList<>();
        List<Schedule_in_label> updateRelations=new ArrayList<>();
        List<Schedule_in_label> deleteRelations=new ArrayList<>();
        for(Schedule_in_label schedule:notSYCRelations){

            if(schedule.getStatus()== StringUtil.LOCAL_INSERT&&schedule.getId()!=null)
                insertRelations.add(netRelationPrepare(schedule));
            else if(schedule.getStatus()==StringUtil.LOCAL_UPDATE)
                updateRelations.add(netRelationPrepare(schedule));
            else
                deleteRelations.add(netRelationPrepare(schedule));
        }
        if(insertRelations.size()>0)
            dataSource.insertNotSycScheduleRelation(handler,User.toEntity(user),Schedule_in_label.packageToBean(insertRelations));
        if(updateRelations.size()>0);
        //Todo:
        dataSource.updateNotSycScheduleRelation(handler,User.toEntity(user),Schedule_in_label.packageToBean(updateRelations));
        if(deleteRelations.size()>0);
        dataSource.deleteNotSycScheduleRelation(handler,User.toEntity(user),Schedule_in_label.packageToBean(deleteRelations));
    }

    public void updateScheduleRelationStatusById(List<Bean<com.example.demo.data.entities.Schedule_in_label>> obj) {
        for(Bean<com.example.demo.data.entities.Schedule_in_label> bean:obj){
            Integer offlineId = bean.getOfflineId();
            Schedule_in_label relation=this.getScheduleRelationById(offlineId);
            relation.setStatus(StringUtil.SYC);
            relation.setDbId(bean.getData().getId());
            relation.setTimeStamp(MyConverter.doubleToTimestamp(bean.getData().getTimeStamp()));
            new DiaryRepository.UpdateAsyncRelation(relationDao).execute(relation);
        }
    }

    private Schedule_in_label getScheduleRelationById(Integer id) {
        return relationDao.getScheduleRelationByIdStatic(id);
    }

    public void removeScheduleRelationById(List<Bean<com.example.demo.data.entities.Schedule_in_label>> obj) {
        for(Bean<com.example.demo.data.entities.Schedule_in_label>bean:obj){
            relationDao.deleteScheduleRelationById(bean.getOfflineId());
        }
    }


    public void netQueryScheduleList(Handler handler, User user) {
        dataSource.netQueryScheduleList(handler,User.toEntity(user),MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheScheduleSycTimeStamp())));
    }

    public void netQueryScheduleLabelList(Handler handler, User user) {
        dataSource.netQueryScheduleScheduleLabelList(handler,User.toEntity(user),MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheScheduleLabelSycTimeStamp())));
    }

    public void cacheScheduleListData(List<Bean<com.example.demo.data.entities.Schedule>> obj) {
        List<Schedule> unpack = com.example.demo.data.entities.Schedule.unpack(obj);
        for(Schedule schedule:unpack){
           // new DiaryRepository.InsertAsyncSchedule(scheduleDao).execute(schedule);
            Schedule scheduleByDbIdStatic = scheduleDao.getScheduleByDbIdStatic(schedule.getDbId());
            if(scheduleByDbIdStatic==null)
              scheduleDao.insertSchedule(schedule);
            else
              scheduleDao.updateSchedule(schedule);
        }

    }

    public void cacheScheduleLabelListData(List<Bean<com.example.demo.data.entities.Schedule_label>> obj) {

        List<Schedule_label> unpack = com.example.demo.data.entities.Schedule_label.unpack(obj);
        for(Schedule_label label:unpack){
            Schedule_label object = labelDao.getScheduleLabelByDbIdStatic(label.getDbId());
            if( object==null)
                labelDao.insertSchedule_label(label);
            else
                labelDao.updateSchedule_label( label);
          //  new DiaryRepository.InsertAsyncScheduleLabel(labelDao).execute(label);
           // labelDao.insertSchedule_label(label);
        }
    }

    public void cacheScheduleRelationListData(List<Bean<com.example.demo.data.entities.Schedule_in_label>> obj) {
        List<Schedule_in_label> unpack = com.example.demo.data.entities.Schedule_in_label.unpack(obj);
        for(Schedule_in_label r:unpack){
            Schedule_in_label object = relationDao.getScheduleRelationByIdStatic(r.getDbId());
            if( object==null)
                new InsertAsyncRelationCache(relationDao).execute(cacheRelationPrepareNetBack(r));
            else
                new UpdateAsyncRelationCache(relationDao).execute(cacheRelationPrepareNetBack( r));
          //  relationDao.insertSchedule_in_label(cacheRelationPrepareNetBack(r));//
        }
    }

    public void netQueryScheduleRelationList(Handler handler, User user) {
        dataSource.netQueryScheduleScheduleRelationList(handler,User.toEntity(user),MyConverter.timestampToDouble(Tool.makeTimestampNotNull(getMaxCacheScheduleRelationSycTimeStamp())));
    }

    public LiveData<List<Schedule>> getDayScheduleListByCategoryTimestampBetween(int category,Date date) {
        return scheduleDao.getScheduleListByCategoryTimestampBetween(category,TimeUtils.returnTodayBeginTimestamp(date),TimeUtils.returnTodayEndTimestamp(date));
    }

    public LiveData<List<Schedule>> getDayScheduleListByTimestampBetween(Date date) {
        return scheduleDao.getScheduleListByTimestampBetween(TimeUtils.returnTodayBeginTimestamp(date),TimeUtils.returnTodayEndTimestamp(date));
    }

    public LiveData<List<Schedule>> getDayScheduleListByTimestampBetweenScheduleDone(Date date,int done) {
        return scheduleDao.getScheduleListByTimestampBetweenScheduleDone(TimeUtils.returnTodayBeginTimestamp(date),TimeUtils.returnTodayEndTimestamp(date),done);
    }

    public LiveData<List<Schedule>> getDayScheduleListByTag(Schedule_label label) {
        return scheduleDao.getScheduleListByTag(label.getId());
    }

    public LiveData<List<Schedule_label>> getAllScheduleLabel() {
        return labelDao.getAllScheduleLabel();
    }

    public List<Schedule_label> getAllScheduleLabelStatic() {
        return labelDao.getAllScheduleLabelStatic();
    }

    public long insertSchedule(Schedule schedule) {
        schedule.setStatus(StringUtil.INSERT_STATUS);
        long id = scheduleDao.insertSchedule(schedule);
        List<Schedule> notSYCSchedules = getNotSYCSchedules();
        netSYCNotSycSchedule(handler, StringUtil.getUser(), notSYCSchedules);
        return id;
    }

    public void insertRelationAsync(Schedule_in_label relation) {
        relation.setStatus(StringUtil.LOCAL_INSERT);
        new DiaryRepository.InsertAsyncRelation(relationDao).execute(relation);
        updateScheduleLabelCountById(relation.getScheduleLabelId(),StringUtil.COUNT_ADD);
    }

    public void updateScheduleLabelCountById(Integer id,int increment) {
        Schedule_label label = labelDao.getScheduleLabelByIdStatic(id);
        label.setScheduleCount(label.getScheduleCount()+increment);
        if(label.getStatus()!=StringUtil.LOCAL_INSERT)label.setStatus(StringUtil.LOCAL_UPDATE);
        new DiaryRepository.UpdateAsyncScheduleLabel(labelDao).execute(label);
    }

    public List<Schedule_label> getScheduleLabelByScheduleIdStatic(Integer scheduleId) {
        return labelDao.getScheduleLabelByScheduleIdStatic(scheduleId);
    }

    public List<Schedule> getDayAlarmScheduleListByTimestampAfterStatic(Date date) {
        return scheduleDao.getDayAlarmScheduleListByTimestampAfterStatic(MyConverter.dateToTimeStamp(date),TimeUtils.returnTodayEndTimestamp(date));

    }

    public void updateSchedule(Schedule schedule) {
        if(schedule.getStatus()!=StringUtil.LOCAL_INSERT) schedule.setStatus(StringUtil.LOCAL_UPDATE);
        new DiaryRepository.UpdateAsyncSchedule(scheduleDao).execute(schedule);
    }

    public Schedule_in_label getRelationByScheduleIdLabelIdStatic(Integer scheduleId, Integer labelId) {
        return relationDao.getScheduleRelationByScheduleIdLabelIdStatic(scheduleId,labelId);
    }

    public void statusDeleteRelationAsync(Schedule_in_label relation) {

        if(relation.getStatus()!=StringUtil.LOCAL_INSERT){
            relation.setStatus(StringUtil.LOCAL_DELETE);
            new DiaryRepository.UpdateAsyncRelation(relationDao).execute(relation);
        }else {
            new DiaryRepository.DeleteAsyncRelation(relationDao).execute(relation);
        }
        this.updateScheduleLabelCountById(relation.getScheduleLabelId(),-1);
    }

    public void statusDeleteScheduleAsync(Schedule schedule) {
        if(schedule.getStatus()!=StringUtil.LOCAL_INSERT){
            schedule.setStatus(StringUtil.LOCAL_DELETE);
            new DiaryRepository.UpdateAsyncSchedule(scheduleDao).execute(schedule);
        }else {
            new DiaryRepository.DeleteAsyncSchedule(scheduleDao).execute(schedule);
        }
    }

    public List<Schedule> getDayScheduleListByTimestampBetweenStatic(Date date) {
        return scheduleDao.getScheduleListByTimestampBetweenStatic(TimeUtils.returnTodayBeginTimestamp(date),TimeUtils.returnTodayEndTimestamp(date));

    }

    public List<Schedule> getDayScheduleListByTimestampBetweenScheduleDoneStatic(Date date, Integer done) {
        return scheduleDao.getScheduleListByTimestampBetweenScheduleDoneStatic(TimeUtils.returnTodayBeginTimestamp(date),TimeUtils.returnTodayEndTimestamp(date),done);
    }

    public void insertScheduleLabel(Schedule_label label) {
        label.setScheduleCount(0);
        label.setStatus(StringUtil.LOCAL_INSERT);
        new DiaryRepository.InsertAsyncScheduleLabel(labelDao).execute(label);
    }

    public LiveData<List<Schedule>> getScheduleListByTag(Schedule_label label) {
        return scheduleDao.getScheduleListByTag(label.getId());
    }

    public List<Schedule> getScheduleListByTagStatic(Schedule_label label) {
        return scheduleDao.getScheduleListByTagStatic(label.getId());
    }

    public LiveData<List<Schedule_label>> getScheduleLabelByTitleLike(String titleLike) {
        return labelDao.getScheduleLabelByTitleLike(titleLike);
    }

    public LiveData<List<Schedule>> getScheduleByTitleLike(String titleLike) {
        return scheduleDao.getScheduleByTitleLike(titleLike);
    }

    public void updateScheduleLabel(Schedule_label obj) {
        if(obj.getStatus()!=StringUtil.LOCAL_INSERT)
            obj.setStatus(StringUtil.UPDATE_STATUS);
        new UpdateAsyncScheduleLabel(labelDao).execute(obj);
    }

    public void statusDeleteScheduleLabel(Schedule_label obj) {
        if(obj.getStatus()!=StringUtil.LOCAL_INSERT){
            List<Schedule_in_label> relationList=relationDao.getRelationListByScheduleLabelStatic(obj.getId());
            for(Schedule_in_label relation:relationList){
                relation.setStatus(StringUtil.LOCAL_DELETE);
                new UpdateAsyncRelation(relationDao).execute(relation);
            }
            obj.setStatus(StringUtil.LOCAL_DELETE);
            new UpdateAsyncScheduleLabel(labelDao).execute(obj);
        }else {
            new DeleteAsyncScheduleLabel(labelDao,relationDao).execute(obj);
        }
    }


      class InsertAsyncScheduleCache extends AsyncTask<Schedule,Void,Void> {
        ScheduleDao dao;
        public InsertAsyncScheduleCache(ScheduleDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Schedule ...schedules) {
             dao.insertScheduleList(schedules);

             return  null;

        }
      }
      class UpdateAsyncScheduleCache extends AsyncTask<Schedule,Void,Void> {
        ScheduleDao dao;
        public UpdateAsyncScheduleCache(ScheduleDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Schedule... schedules) {
            dao.updateSchedule(schedules);
            return null;
        }
    }

    class UpdateAsyncSchedule extends AsyncTask<Schedule,Void,Void> {
        ScheduleDao dao;
        public UpdateAsyncSchedule(ScheduleDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Schedule... schedules) {
            dao.updateSchedule(schedules);
            List<Schedule> notSYCSchedules = getNotSYCSchedules();
            netSYCNotSycSchedule(handler, StringUtil.getUser(), notSYCSchedules);
            return null;
        }
    }

    static class DeleteAsyncSchedule extends AsyncTask<Schedule,Void,Void>{
        ScheduleDao dao;
        public DeleteAsyncSchedule(ScheduleDao scheduleDao) {
            this.dao=scheduleDao;
        }
        @Override
        protected Void doInBackground(Schedule... schedules) {
            dao.deleteSchedule(schedules);
            return null;
        }
    }

    class InsertAsyncScheduleLabel extends AsyncTask<Schedule_label,Void,Void> {
        Schedule_labelDao dao;

        public InsertAsyncScheduleLabel(Schedule_labelDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Schedule_label ...label) {

              dao.insertSchedule_label(label);
            List<com.example.demo.data.model.Schedule_label> notSYCSchedulesLabel =getNotSYCSchedulesLabel();
            netSYCNotSycScheduleLabel(handler, StringUtil.getUser(),notSYCSchedulesLabel );
              return null;

        }
    }

     class UpdateAsyncScheduleLabel extends AsyncTask<Schedule_label,Void,Void> {
        Schedule_labelDao dao;

        public UpdateAsyncScheduleLabel(Schedule_labelDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Schedule_label... labels) {
            dao.updateSchedule_label(labels);
            List<com.example.demo.data.model.Schedule_label> notSYCSchedulesLabel = getNotSYCSchedulesLabel();
            netSYCNotSycScheduleLabel(handler, StringUtil.getUser(),notSYCSchedulesLabel );
            return null;
        }
    }

    class InsertAsyncScheduleLabelCache extends AsyncTask<Schedule_label,Void,Void> {
        Schedule_labelDao dao;

        public InsertAsyncScheduleLabelCache(Schedule_labelDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Schedule_label ...label) {

            dao.insertSchedule_label(label);

            return null;

        }
    }

    class UpdateAsyncScheduleLabelCache extends AsyncTask<Schedule_label,Void,Void> {
        Schedule_labelDao dao;

        public UpdateAsyncScheduleLabelCache(Schedule_labelDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Schedule_label... labels) {
            dao.updateSchedule_label(labels);
            return null;
        }
    }

    static class DeleteAsyncScheduleLabel extends AsyncTask<Schedule_label,Void,Void>{
        Schedule_labelDao dao;
        Schedule_in_labelDao scheduleInLabelDao;
        public DeleteAsyncScheduleLabel(Schedule_labelDao dao,Schedule_in_labelDao scheduleInLabelDao) {
            this.dao = dao;
            this.scheduleInLabelDao=scheduleInLabelDao;
        }

        @Override
        protected Void doInBackground(Schedule_label... labels) {
            for(Schedule_label label:labels){
                scheduleInLabelDao.deleteSchedule_in_labelByLabelId(label.getId());
            }
            dao.deleteSchedule_label(labels);
            return null;
        }
    }

    class InsertAsyncRelation extends AsyncTask<Schedule_in_label,Void,Void> {
        Schedule_in_labelDao relationDao;


        public InsertAsyncRelation(Schedule_in_labelDao relationDao) {
            this.relationDao = relationDao;
        }

        @Override
        protected Void doInBackground(Schedule_in_label ...relations) {
            relationDao.insertSchedule_in_label(relations);
            sycRelation();
            return null;
        }
    }

     class UpdateAsyncRelation extends AsyncTask<Schedule_in_label,Void,Void> {
        Schedule_in_labelDao relationDao;

        public UpdateAsyncRelation(Schedule_in_labelDao relationDao) {
            this.relationDao = relationDao;
        }


        @Override
        protected Void doInBackground(Schedule_in_label... labels) {
            relationDao.updateSchedule_in_label(labels);
            sycRelation();
            return null;
        }
    }

    private void sycRelation(){
        List<Schedule_in_label> notSYCSchedulesRelation = getNotSYCSchedulesRelation();
        netSYCNotSycScheduleRelation(handler, StringUtil.getUser(), notSYCSchedulesRelation);
    }
    class InsertAsyncRelationCache extends AsyncTask<Schedule_in_label,Void,Void> {
        Schedule_in_labelDao relationDao;


        public InsertAsyncRelationCache(Schedule_in_labelDao relationDao) {
            this.relationDao = relationDao;
        }

        @Override
        protected Void doInBackground(Schedule_in_label ...relations) {
            relationDao.insertSchedule_in_label(relations);
            return null;
        }
    }

    class UpdateAsyncRelationCache extends AsyncTask<Schedule_in_label,Void,Void> {
        Schedule_in_labelDao relationDao;

        public UpdateAsyncRelationCache(Schedule_in_labelDao relationDao) {
            this.relationDao = relationDao;
        }


        @Override
        protected Void doInBackground(Schedule_in_label... labels) {
            relationDao.updateSchedule_in_label(labels);

            return null;
        }
    }

    static class DeleteAsyncRelation extends AsyncTask<Schedule_in_label,Void,Void>{
        Schedule_in_labelDao relationDao;

        public DeleteAsyncRelation(Schedule_in_labelDao relationDao) {
            this.relationDao = relationDao;
        }

        @Override
        protected Void doInBackground(Schedule_in_label... relations) {
            relationDao.deleteSchedule_in_label(relations);
            return null;
        }
    }
    public  Schedule_in_label netRelationPrepare(Schedule_in_label relation){
        Integer ScheduleDbId = scheduleDao.getScheduleByIdStatic(relation.getScheduleId()).getDbId();
        Integer LabelDbId = labelDao.getLabelByIdStatic(relation.getScheduleLabelId()).getDbId();
        if(ScheduleDbId!=null&&LabelDbId!=null) {
            relation.setScheduleId(ScheduleDbId);
            relation.setScheduleLabelId(LabelDbId);
            return relation;
        }
        return null;
    }
    public  Schedule_in_label cacheRelationPrepareNetBack(Schedule_in_label relation){
        relation.setScheduleId(scheduleDao.getScheduleByDbIdStatic(relation.getScheduleId()).getId());
        relation.setScheduleLabelId(labelDao.getScheduleLabelByDbIdStatic(relation.getScheduleLabelId()).getId());
        return relation;
    }
}
