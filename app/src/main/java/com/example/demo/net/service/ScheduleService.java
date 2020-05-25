package com.example.demo.net.service;

import com.example.demo.data.entities.Schedule;
import com.example.demo.data.entities.Schedule_in_label;
import com.example.demo.data.entities.Schedule_label;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonLastSycTime;
import com.example.demo.net.json.JsonResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ScheduleService {

    @POST("/schedule/user/query/timestamp")
    Call<JsonLastSycTime> QueryServiceScheduleTimeStamp(@Query("sessionId") String sessionId,@Query("userId") int userId);

    @POST("/schedule/label/user/query/timestamp")
    Call<JsonLastSycTime> QueryServiceScheduleLabelTimeStamp(@Query("sessionId") String sessionId,@Query("userId") int userId);

    @POST("/schedule/relation/user/query/timestamp")
    Call<JsonLastSycTime> QueryServiceScheduleRelationTimeStamp(@Query("sessionId") String sessionId,@Query("userId") int userId);

    @POST("/schedule/user/insert")
    Call<JsonResult<Schedule>> insertServiceSchedule(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Schedule>> list);
    @POST("/schedule/user/update")
    Call<JsonResult<Schedule>> updateServiceSchedule(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Schedule>> list);
    @POST("/schedule/user/remove")
    Call<JsonResult<Schedule>> removeServiceSchedule(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Schedule>> list);

    @POST("/schedule/label/user/insert")
    Call<JsonResult<Schedule_label>> insertServiceScheduleLabel(@Query("sessionId") String sessionId, @Query("userId") int userId, @Body List<Bean<Schedule_label>> list);
    @POST("/schedule/label/user/update")
    Call<JsonResult<Schedule_label>> updateServiceScheduleLabel(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Schedule_label>> list);
    @POST("/schedule/label/user/remove")
    Call<JsonResult<Schedule_label>> removeServiceScheduleLabel(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Schedule_label>> list);

    @POST("/schedule/relation/user/insert")
    Call<JsonResult<Schedule_in_label>> insertServiceScheduleRelation(@Query("sessionId") String sessionId, @Query("userId") int userId, @Body List<Bean<Schedule_in_label>> list);
    @POST("/schedule/relation/user/update")
    Call<JsonResult<Schedule_in_label>> updateServiceScheduleRelation(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Schedule_in_label>> list);
    @POST("/schedule/relation/user/remove")
    Call<JsonResult<Schedule_in_label>> removeServiceScheduleRelation(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Schedule_in_label>> list);

    @POST("/schedule/user/query/list")
    Call<JsonResult<Schedule>> QueryServiceScheduleList(@Query("sessionId") String sessionId,@Query("userId") int userId,@Query("timestamp") Double timestamp);

    @POST("/schedule/label/user/query/list")
    Call<JsonResult<Schedule_label>> QueryServiceScheduleLabelList(@Query("sessionId") String sessionId,@Query("userId") int userId,@Query("timestamp") Double timestamp);

    @POST("/schedule/relation/user/query/list")
    Call<JsonResult<Schedule_in_label>> QueryServiceScheduleRelationList(@Query("sessionId") String sessionId,@Query("userId") int userId,@Query("timestamp") Double timestamp);
}
