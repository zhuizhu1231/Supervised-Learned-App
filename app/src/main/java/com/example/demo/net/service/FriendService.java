package com.example.demo.net.service;

import com.example.demo.data.entities.Friend;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FriendService {


    @POST("/friend/user/insert")
    Call<JsonResult<Friend>> insertServiceFriend(@Query("sessionId") String sessionId, @Query("userId") int userId, @Body List<Bean<Friend>> list);
    @POST("/friend/user/update")
    Call<JsonResult<Friend>> updateServiceFriend(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Friend>> list);
    @POST("/friend/user/remove")
    Call<JsonResult<Friend>> removeServiceFriend(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Friend>> list);

    @POST("/friend/user/query/list")
    Call<JsonResult<Friend>> QueryServiceFriendList(@Query("sessionId") String sessionId, @Query("userId") int userId, @Query("timestamp") Double timestamp);

    @POST("/friend/user/query/like")
    Call<JsonResult<Friend>> QueryServiceFriendLikeList(@Query("sessionId") String sessionId, @Query("userId") int userId, @Query("key") String key);

    @POST("/friend/user/query/id")
    Call<JsonResult<Friend>> QueryServiceFriendByUserId(@Query("sessionId") String sessionId, @Query("userId") int userId, @Query("id") int id);

}
