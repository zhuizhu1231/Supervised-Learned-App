package com.example.demo.data.datasource;

import android.os.Handler;

import com.example.demo.data.entities.Schedule;
import com.example.demo.data.entities.Schedule_in_label;
import com.example.demo.data.entities.Schedule_label;
import com.example.demo.data.entities.User;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonLastSycTime;
import com.example.demo.net.json.JsonResult;
import com.example.demo.net.service.ScheduleService;
import com.example.demo.util.RetrofitManager;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.List;

import retrofit2.Call;

public class DiaryDataSource {
    private  ScheduleService scheduleService;
    public DiaryDataSource(){
        scheduleService= RetrofitManager.getRetrofit().create(ScheduleService.class);
    }

    public void QueryScheduleServiceTimeStamp(Handler handler,com.example.demo.data.entities.User user) {

        Call<JsonLastSycTime> jsonLastSycTimeCall = scheduleService.QueryServiceScheduleTimeStamp(StringUtil.getSessionId(),user.getId());
        Tool.netTimeStampRequest(jsonLastSycTimeCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_QUERY_TIMESTAMP_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_QUERY_TIMESTAMP_FAIL);
    }

    public void QueryScheduleLabelServiceTimeStamp(Handler handler, com.example.demo.data.entities.User user) {
        Call<JsonLastSycTime> jsonLastSycTimeCall = scheduleService.QueryServiceScheduleLabelTimeStamp(StringUtil.getSessionId(),user.getId());
        Tool.netTimeStampRequest(jsonLastSycTimeCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_QUERY_TIMESTAMP_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_QUERY_TIMESTAMP_FAIL);
    }


    public void QueryScheduleRelationServiceTimeStamp(Handler handler, com.example.demo.data.entities.User user) {
        Call<JsonLastSycTime> jsonLastSycTimeCall = scheduleService.QueryServiceScheduleRelationTimeStamp(StringUtil.getSessionId(), user.getId());
        Tool.netTimeStampRequest(jsonLastSycTimeCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_QUERY_TIMESTAMP_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_QUERY_TIMESTAMP_FAIL);
    }

    public void insertNotSycSchedule(Handler handler, User user, List<Bean<Schedule>> schedules) {//MutableLiveData<ScheduleRelationPrepare> schedule_relation_prepare
        Call<JsonResult<Schedule>> jsonResultCall = scheduleService.insertServiceSchedule(StringUtil.getSessionId(), user.getId(),schedules);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_INSERT_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_INSERT_FAIL);
     //   schedule_relation_prepare.getValue().timerRelationScheduleLabelPrepare= true;
    }


    public void updateNotSycSchedule(Handler handler, com.example.demo.data.entities.User user, List<Bean<Schedule>> packageToBean) {
        Call<JsonResult<Schedule>> jsonResultCall = scheduleService.updateServiceSchedule(StringUtil.getSessionId(), user.getId(),packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_UPDATE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_UPDATE_FAIL);
    }

    public void deleteNotSycSchedule(Handler handler, User toEntity, List<Bean<Schedule>> packageToBean) {
        ScheduleService scheduleService = RetrofitManager.getRetrofit().create(ScheduleService.class);
        Call<JsonResult<Schedule>> jsonResultCall = scheduleService.removeServiceSchedule(StringUtil.getSessionId(), toEntity.getId(),packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_DELETE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_DELETE_FAIL);
    }

    public void insertNotSycScheduleLabel(Handler handler, User toEntity, List<Bean<Schedule_label>> packageToBean) {//, MutableLiveData<ScheduleRelationPrepare> schedule_relation_prepare
        ScheduleService scheduleService = RetrofitManager.getRetrofit().create(ScheduleService.class);
        Call<JsonResult<Schedule_label>> jsonResultCall = scheduleService.insertServiceScheduleLabel(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_INSERT_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_INSERT_FAIL);
       // schedule_relation_prepare.getValue().timerRelationSchedulePrepare= true;
    }

    public void updateNotSycScheduleLabel(Handler handler, User toEntity, List<Bean<Schedule_label>> packageToBean) {
        Call<JsonResult<Schedule_label>> jsonResultCall = scheduleService.updateServiceScheduleLabel(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_UPDATE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_UPDATE_FAIL);
    }

    public void deleteNotSycScheduleLabel(Handler handler, User toEntity, List<Bean<Schedule_label>> packageToBean) {
        Call<JsonResult<Schedule_label>> jsonResultCall = scheduleService.removeServiceScheduleLabel(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_DELETE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_LABEL_DELETE_FAIL);
    }

    public void insertNotSycScheduleRelation(Handler handler, User toEntity, List<Bean<Schedule_in_label>> packageToBean) {
        Call<JsonResult<Schedule_in_label>> jsonResultCall = scheduleService.insertServiceScheduleRelation(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_INSERT_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_INSERT_FAIL);
    }

    public void updateNotSycScheduleRelation(Handler handler, User toEntity, List<Bean<Schedule_in_label>> packageToBean) {
        Call<JsonResult<Schedule_in_label>> jsonResultCall = scheduleService.updateServiceScheduleRelation(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_UPDATE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_UPDATE_FAIL);
    }

    public void deleteNotSycScheduleRelation(Handler handler, User toEntity, List<Bean<Schedule_in_label>> packageToBean) {
        Call<JsonResult<Schedule_in_label>> jsonResultCall = scheduleService.removeServiceScheduleRelation(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_DELETE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_SCHEDULE_RELATION_DELETE_FAIL);
    }

    public void netQueryScheduleList(Handler handler, User toEntity, Double maxCacheScheduleSycTimeStamp) {
        Call<JsonResult<Schedule>> jsonResultCall = scheduleService.QueryServiceScheduleList(StringUtil.getSessionId(), toEntity.getId(), maxCacheScheduleSycTimeStamp);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.OVERRIDE_DATA_SERVICE_SCHEDULE_QUERY_LIST_SUCCESS,
                StringUtil.OVERRIDE_DATA_SERVICE_SCHEDULE_QUERY_LIST_FAIL);

    }

    public void netQueryScheduleScheduleLabelList(Handler handler, User toEntity,Double maxCacheScheduleSycTimeStamp) {
        Call<JsonResult<Schedule_label>> jsonResultCall = scheduleService.QueryServiceScheduleLabelList(StringUtil.getSessionId(), toEntity.getId(),maxCacheScheduleSycTimeStamp);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.OVERRIDE_DATA_SERVICE_SCHEDULE_LABEL_QUERY_LIST_SUCCESS,
                StringUtil.OVERRIDE_DATA_SERVICE_SCHEDULE_LABEL_QUERY_LIST_FAIL);
    }

    public void netQueryScheduleScheduleRelationList(Handler handler, User toEntity, Double maxCacheScheduleSycTimeStamp) {
        Call<JsonResult<Schedule_in_label>> jsonResultCall = scheduleService.QueryServiceScheduleRelationList(StringUtil.getSessionId(), toEntity.getId(),maxCacheScheduleSycTimeStamp);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.OVERRIDE_DATA_SERVICE_SCHEDULE_RELATION_QUERY_LIST_SUCCESS,
                StringUtil.OVERRIDE_DATA_SERVICE_SCHEDULE_RELATION_QUERY_LIST_FAIL);
    }
}
