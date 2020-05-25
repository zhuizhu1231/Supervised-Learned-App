package com.example.demo.data.datasource;

import android.os.Handler;
import android.os.Message;

import com.example.demo.data.entities.User;
import com.example.demo.net.json.JsonResult;
import com.example.demo.net.service.UserService;
import com.example.demo.util.MD5Utils;
import com.example.demo.util.RetrofitManager;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.net.HttpURLConnection;

import retrofit2.Callback;





/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class UserDataSource {
    private UserService userService;


    public UserDataSource() {
       userService = RetrofitManager.getRetrofit().create(UserService.class);

    }

    public  void login(Integer user_number, String password, Handler handler) {
            User user = new User();
            user.setId(user_number);
            user.setPassword(new MD5Utils().MD5(password));
           loginBasic(handler,user);

    }
    public void quickLogin(Integer userNumber, String password, Handler handler) {
        User user = new User();
        user.setId(userNumber);
        user.setPassword(password);
        loginBasic(handler,user);
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public void register(User user,Handler handler) {
        retrofit2.Call<JsonResult<User>> jsonResultCall = userService.registerUser(user);
        jsonResultCall.enqueue(new Callback<JsonResult<User>>() {
            @Override
            public void onResponse(retrofit2.Call<JsonResult<User>> call, retrofit2.Response<JsonResult<User>> response) {
                if(HttpURLConnection.HTTP_OK==response.code()&&response.body().getCode()==StringUtil.CODE_REQUEST_SUCCESS) {
                    User data = response.body().getData().get(0).getData();
                    Message message=Message.obtain();
                    message.what= StringUtil.SUCCESS_REGISTER;
                    message.obj=data.getId();
                    handler.sendMessage(message);
                }else {

                    Message message=Message.obtain();
                    message.what= StringUtil.FAIL_REGISTER;
                    handler.sendMessage(message);
                }


            }

            @Override
            public void onFailure(retrofit2.Call<JsonResult<User>> call, Throwable t) {

                Message message=Message.obtain();
                message.what= StringUtil.FAIL_REQUEST;
                handler.sendMessage(message);
            }
        });
    }




    private void loginBasic(Handler handler,User user){
        retrofit2.Call<JsonResult<User>> login = userService.login(user);
        login.enqueue(new Callback<JsonResult<User>>() {
            @Override
            public void onResponse(retrofit2.Call<JsonResult<User>> call, retrofit2.Response<JsonResult<User>> response) {
                JsonResult<User> body = response.body();
                if(HttpURLConnection.HTTP_OK==response.code()&& body.getCode()==StringUtil.CODE_REQUEST_SUCCESS){
                    StringUtil.setSessionId(body.getSessionId());
                    Tool.sendMessage(handler,StringUtil.ONLINE,User.toModel(body.getData().get(0).getData()));
                }else {
                    Tool.sendMessage(handler,StringUtil.OFFLINE,body.getMessage());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonResult<User>> call, Throwable t) {
                Tool.sendMessage(handler,StringUtil.FAIL_REQUEST,null);
            }
        });
    }
}
