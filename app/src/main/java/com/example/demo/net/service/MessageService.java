package com.example.demo.net.service;

import com.example.demo.data.entities.Message;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MessageService {



    @POST("/message/user/query/list")
    Call<JsonResult<Message>> QueryServiceMessageList(@Query("sessionId") String sessionId, @Query("userId") int userId, @Query("timestamp") Double timestamp);

    @POST("/message/user/update")
    Call<JsonResult<Message>> updateServiceMilepost(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Message>> list);

}
