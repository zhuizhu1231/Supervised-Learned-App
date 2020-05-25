package com.example.demo.net.service;

import com.example.demo.data.entities.Aim;
import com.example.demo.data.entities.Task;
import com.example.demo.data.entities.TaskLog;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonLastSycTime;
import com.example.demo.net.json.JsonResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TargetService {
    @POST("/aim/user/query/timestamp")
    Call<JsonLastSycTime> QueryServiceTargetTimeStamp(@Query("sessionId") String sessionId, @Query("userId") int userId);

    @POST("/aim/user/insert")
    Call<JsonResult<Aim>> insertServiceAim(@Query("sessionId") String sessionId, @Query("userId") int userId, @Body List<Bean<Aim>> list);
    @POST("/aim/user/update")
    Call<JsonResult<Aim>> updateServiceAim(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Aim>> list);
    @POST("/aim/user/remove")
    Call<JsonResult<Aim>> removeServiceAim(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Aim>> list);

    @POST("/task/user/insert")
    Call<JsonResult<Task>> insertServiceTask(@Query("sessionId") String sessionId, @Query("userId") int userId, @Body List<Bean<Task>> list);
    @POST("/task/user/update")
    Call<JsonResult<Task>> updateServiceTask(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Task>> list);
    @POST("/task/user/remove")
    Call<JsonResult<Task>> removeServiceTask(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Task>> list);

    @POST("/aim/user/query/list")
    Call<JsonResult<Aim>> QueryServiceAimList(@Query("sessionId") String sessionId,@Query("userId") int userId,@Query("timestamp") Double timestamp);
    @POST("/task/user/query/list")
    Call<JsonResult<Task>> QueryServiceTaskList(@Query("sessionId") String sessionId, @Query("userId") int userId,@Query("timestamp") Double timestamp);




    @POST("/task/log/user/insert")
    Call<JsonResult<TaskLog>> insertServiceTaskLog(@Query("sessionId") String sessionId, @Query("userId") int userId, @Body List<Bean<TaskLog>> list);
    @POST("/task/log/user/query/list")
    Call<JsonResult<TaskLog>> QueryServiceTaskLogList(@Query("sessionId") String sessionId, @Query("userId") int userId,@Query("timestamp") Double timestamp);

}
