package com.example.demo.net.service;

import com.example.demo.data.entities.User;
import com.example.demo.net.json.JsonResult;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("/login")
    Call<JsonResult<User>> login(@Body User user);

    @POST("/register")
    Call<JsonResult<User>> registerUser(@Body User user);
//    @POST("/login")
//    Call<ResponseBody> login(@Body User user);
}
