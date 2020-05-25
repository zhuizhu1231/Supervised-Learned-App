package com.example.demo.data.datasource;

import android.os.Handler;

import com.example.demo.data.entities.Milepost;
import com.example.demo.data.entities.User;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonLastSycTime;
import com.example.demo.net.json.JsonResult;
import com.example.demo.net.service.MilepostService;
import com.example.demo.util.RetrofitManager;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.List;

import retrofit2.Call;

public class MilepostDataSource {
    private MilepostService milepostService ;
    public MilepostDataSource(){
        milepostService = RetrofitManager.getRetrofit().create(MilepostService.class);
    }
    public void QueryMilepostServiceTimeStamp(Handler handler, User toEntity) {
        Call<JsonLastSycTime> jsonLastSycTimeCall = milepostService.QueryServiceMilepostTimeStamp(StringUtil.getSessionId(), toEntity.getId());
        Tool.netTimeStampRequest(jsonLastSycTimeCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_QUERY_TIMESTAMP_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_QUERY_TIMESTAMP_FAIL);
    }

    public void insertNotSycMilepost(Handler handler, User toEntity, List<Bean<Milepost>> packageToBean) {
        Call<JsonResult<Milepost>> jsonResultCall = milepostService.insertServiceMilepost(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_INSERT_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_INSERT_FAIL);
    }

    public void updateNotSycMilepost(Handler handler, User toEntity, List<Bean<Milepost>> packageToBean) {
        Call<JsonResult<Milepost>> jsonResultCall = milepostService.updateServiceMilepost(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_UPDATE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_UPDATE_FAIL);
    }

    public void deleteNotSycMilepost(Handler handler, User toEntity, List<Bean<Milepost>> packageToBean) {
        Call<JsonResult<Milepost>> jsonResultCall = milepostService.removeServiceMilepost(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_DELETE_SUCCESS,
                StringUtil.TIMER_UPDATE_SERVICE_MILEPOST_DELETE_FAIL);
    }

    public void netQueryMilepostList(Handler handler, User toEntity, Double maxCacheScheduleSycTimeStamp) {
        Call<JsonResult<Milepost>> jsonResultCall = milepostService.QueryServiceMilepostList(StringUtil.getSessionId(), toEntity.getId(), maxCacheScheduleSycTimeStamp);
        Tool.netJsonResultRequest(jsonResultCall,handler,
                StringUtil.OVERRIDE_DATA_SERVICE_MILEPOST_QUERY_LIST_SUCCESS,
                StringUtil.OVERRIDE_DATA_SERVICE_MILEPOST_QUERY_LIST_FAIL);
    }
}
