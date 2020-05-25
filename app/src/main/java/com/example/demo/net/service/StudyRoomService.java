package com.example.demo.net.service;

import com.example.demo.data.entities.Study_room;
import com.example.demo.data.entities.User_in_study_room;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface StudyRoomService {
    @POST("/room/create")
    Call<JsonResult<Study_room>> insertServiceStudy_room(@Query("sessionId") String sessionId, @Query("userId") int userId, @Body Study_room room);
    @POST("/room/add")
    Call<JsonResult<Study_room>> joinServiceStudy_room(@Query("sessionId") String sessionId, @Query("userId") int userId, @Query("id") int id);

    @POST("/room/user/update")
    Call<JsonResult<Study_room>> updateServiceStudy_room(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Study_room>> list);
    @POST("/room/user/remove")
    Call<JsonResult<Study_room>> removeServiceStudy_room(@Query("sessionId") String sessionId, @Query("userId") int userId,  @Body List<Bean<Study_room>> list);

    @POST("/room/user/query/list")
    Call<JsonResult<Study_room>> QueryServiceStudy_roomList(@Query("sessionId") String sessionId, @Query("userId") int userId, @Query("timestamp") Double timestamp);

    @POST("/room/query/like")
    Call<JsonResult<Study_room>> QueryServiceStudy_roomLikeList(@Query("sessionId") String sessionId, @Query("userId") int userId, @Query("roomName") String roomName);

    @POST("/room/query/id")
    Call<JsonResult<Study_room>> QueryServiceStudy_roomById(@Query("sessionId") String sessionId, @Query("userId") int userId, @Query("id") int id);

    @POST("/room/user/room/member")
    Call<JsonResult<User_in_study_room>> QueryServiceRoomMemberByRoomId(@Query("sessionId") String sessionId, @Query("userId") int userId, @Query("roomId") int roomId);


}
