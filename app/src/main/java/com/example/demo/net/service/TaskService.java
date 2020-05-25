package com.example.demo.net.service;

import com.example.demo.net.json.JsonLastSycTime;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TaskService {
    @POST("/task/user/query/timestamp")
    Call<JsonLastSycTime> QueryServiceTaskTimeStamp(@Query("sessionId") String sessionId, @Query("userId") int userId);
}
