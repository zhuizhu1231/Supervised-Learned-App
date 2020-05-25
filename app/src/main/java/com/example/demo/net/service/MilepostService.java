package com.example.demo.net.service;

import com.example.demo.data.entities.Milepost;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonLastSycTime;
import com.example.demo.net.json.JsonResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MilepostService {
    @POST("/milepost/user/query/timestamp")
    Call<JsonLastSycTime> QueryServiceMilepostTimeStamp(@Query("sessionId") String sessionId, @Query("userId") int userId);

    @POST("/milepost/user/insert")
    Call<JsonResult<Milepost>> insertServiceMilepost(@Query("sessionId") String sessionId, @Query("userId") int userId, @Body List<Bean<Milepost>> list);
    @POST("/milepost/user/update")
    Call<JsonResult<Milepost>> updateServiceMilepost(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Milepost>> list);
    @POST("/milepost/user/remove")
    Call<JsonResult<Milepost>> removeServiceMilepost(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Milepost>> list);

    @POST("/milepost/user/query/list")
    Call<JsonResult<Milepost>> QueryServiceMilepostList(@Query("sessionId") String sessionId, @Query("userId") int userId,@Query("timestamp") Double timestamp);
}
