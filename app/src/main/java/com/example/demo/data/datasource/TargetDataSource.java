package com.example.demo.data.datasource;

import android.os.Handler;

import com.example.demo.data.entities.Aim;
import com.example.demo.data.entities.Task;
import com.example.demo.data.entities.TaskLog;
import com.example.demo.data.entities.User;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonLastSycTime;
import com.example.demo.net.json.JsonResult;
import com.example.demo.net.service.TargetService;
import com.example.demo.net.service.TaskService;
import com.example.demo.util.RetrofitManager;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.List;

import retrofit2.Call;

public class TargetDataSource {
    private TargetService targetService;
    public TargetDataSource(){
        targetService = RetrofitManager.getRetrofit().create(TargetService.class);
    }
    public void QueryTargetServiceTimeStamp(Handler handler, User toEntity) {
        Call<JsonLastSycTime> jsonLastSycTimeCall = targetService.QueryServiceTargetTimeStamp(StringUtil.getSessionId(), toEntity.getId());
        Tool.netTimeStampRequest(jsonLastSycTimeCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_TARGET_QUERY_TIMESTAMP_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_TARGET_QUERY_TIMESTAMP_FAIL);
    }

    public void insertNotSycAim(Handler handler, User toEntity, List<Bean<Aim>> packageToBean) {
        Call<JsonResult<Aim>> jsonResultCall = targetService.insertServiceAim(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_AIM_INSERT_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_AIM_INSERT_FAIL);
    }

    public void updateNotSycAim(Handler handler, User toEntity, List<Bean<Aim>> packageToBean) {
        Call<JsonResult<Aim>> jsonResultCall = targetService.updateServiceAim(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_AIM_UPDATE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_AIM_UPDATE_FAIL);
    }

    public void deleteNotSycAim(Handler handler, User toEntity, List<Bean<Aim>> packageToBean) {
        Call<JsonResult<Aim>> jsonResultCall = targetService.removeServiceAim(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_AIM_DELETE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_AIM_DELETE_FAIL);
    }

    public void insertNotSycNotesRelation(Handler handler, User toEntity, List<Bean<Task>> packageToBean) {
        Call<JsonResult<Task>> jsonResultCall = targetService.insertServiceTask(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_TASK_INSERT_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_TASK_INSERT_FAIL);
    }

    public void updateNotSycNotesRelation(Handler handler, User toEntity, List<Bean<Task>> packageToBean) {
        Call<JsonResult<Task>> jsonResultCall = targetService.updateServiceTask(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_TASK_UPDATE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_TASK_UPDATE_FAIL);
    }

    public void deleteNotSycNotesRelation(Handler handler, User toEntity, List<Bean<Task>> packageToBean) {
        Call<JsonResult<Task>> jsonResultCall = targetService.removeServiceTask(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_TASK_DELETE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_TASK_DELETE_FAIL);
    }

    public void netQueryTargetList(Handler handler, User toEntity, Double maxCacheScheduleSycTimeStamp) {
        Call<JsonResult<Aim>> jsonResultCall = targetService.QueryServiceAimList(StringUtil.getSessionId(), toEntity.getId(), maxCacheScheduleSycTimeStamp);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.OVERRIDE_DATA_SERVICE_AIM_QUERY_LIST_SUCCESS,
                StringUtil.OVERRIDE_DATA_SERVICE_AIM_QUERY_LIST_FAIL);
    }

    public void netQueryTaskList(Handler handler, User toEntity, Double maxCacheScheduleSycTimeStamp) {
        Call<JsonResult<Task>> jsonResultCall = targetService.QueryServiceTaskList(StringUtil.getSessionId(), toEntity.getId(),maxCacheScheduleSycTimeStamp);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.OVERRIDE_DATA_SERVICE_TASK_QUERY_LIST_SUCCESS,
                StringUtil.OVERRIDE_DATA_SERVICE_TASK_QUERY_LIST_FAIL);
    }

    public void QueryTaskServiceTimeStamp(Handler handler, User toEntity) {
        TaskService taskService = RetrofitManager.getRetrofit().create(TaskService.class);
        Call<JsonLastSycTime> jsonLastSycTimeCall = taskService.QueryServiceTaskTimeStamp(StringUtil.getSessionId(), toEntity.getId());
        Tool.netTimeStampRequest(jsonLastSycTimeCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_TASK_QUERY_TIMESTAMP_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_TASK_QUERY_TIMESTAMP_FAIL);
    }

    public void insertNotTaskLog(Handler handler, User toEntity, List<Bean<TaskLog>> packageToBean) {
        Call<JsonResult<TaskLog>> jsonResultCall = targetService.insertServiceTaskLog(StringUtil.getSessionId(), toEntity.getId(), packageToBean);

        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TASKLOG_INSERT_SUCCESS,
                StringUtil.TASKLOG_INSERT_FAIL);

    }

    public void netQueryLogList(Handler handler, User toEntity, Double timestampToDouble) {
        Call<JsonResult<TaskLog>> jsonResultCall = targetService.QueryServiceTaskLogList(StringUtil.getSessionId(), toEntity.getId(),timestampToDouble);

        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.QUERY_TASKLOG_LIST_SUCCESS,
                StringUtil.QUERY_TASKLOG_LIST_FAIL);
    }
}
