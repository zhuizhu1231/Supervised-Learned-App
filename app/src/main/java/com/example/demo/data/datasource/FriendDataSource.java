package com.example.demo.data.datasource;

import android.os.Handler;

import com.example.demo.data.entities.Friend;
import com.example.demo.data.entities.Message;
import com.example.demo.data.entities.User;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonResult;
import com.example.demo.net.service.FriendService;
import com.example.demo.net.service.MessageService;
import com.example.demo.util.RetrofitManager;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.List;

import retrofit2.Call;

public class FriendDataSource {
    MessageService messageService;
    FriendService friendService;
    public FriendDataSource(){
        messageService = RetrofitManager.getRetrofit().create(MessageService.class);
        friendService= RetrofitManager.getRetrofit().create(FriendService.class);
    }
    public void QueryFriendServiceTimeStamp(Handler handler, User toEntity) {
    }

    public void netQueryMessageList(Handler handler, User toEntity, Double timestampToDouble) {
        Call<JsonResult<Message>> jsonResultCall = messageService.QueryServiceMessageList(StringUtil.getSessionId(), toEntity.getId(), timestampToDouble);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.MESSAGE_QUERY_LIST_SUCCESS,StringUtil.MESSAGE_QUERY_LIST_FAIL);
    }

    public void updateNotSycMessage(Handler handler, User toEntity, List<Bean<Message>> packageToBean) {
        Call<JsonResult<Message>> jsonResultCall = messageService.updateServiceMilepost(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.MESSAGE_UPDATE_SUCCESS,StringUtil.MESSAGE_UPDATE_FAIL);
    }

    public void netSearchFriendListByNameLikeOrDbIdLike(Handler handler, String key) {
        Call<JsonResult<Friend>> jsonResultCall = friendService.QueryServiceFriendLikeList(StringUtil.getSessionId(), com.example.demo.data.model.User.toEntity(StringUtil.getUser()).getId(), key);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.QUERY_FRIEND_LIKE_SUCCESS,StringUtil.QUERY_FRIEND_LIKE_FAIL);

    }

    public void netQueryServiceFriendByFriendDbId(Handler handler, Integer dbId) {
        Call<JsonResult<Friend>> jsonResultCall = friendService.QueryServiceFriendByUserId(StringUtil.getSessionId(), com.example.demo.data.model.User.toEntity(StringUtil.getUser()).getId(), dbId);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.QUERY_FRIEND_ID_SUCCESS,StringUtil.QUERY_FRIEND_ID_FAIL);
    }

    public void netQueryServiceFriendList(Handler handler, User toEntity, Double timestampToDouble) {
        Call<JsonResult<Friend>> jsonResultCall = friendService.QueryServiceFriendList(StringUtil.getSessionId(), toEntity.getId(), timestampToDouble);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.FRIEND_QUERY_LIST_SUCCESS,StringUtil.FRIEND_QUERY_LIST_FAIL);
    }

    public void insertNotSycFriend(Handler handler, User toEntity, List<Bean<Friend>> packageToBean) {
        Call<JsonResult<Friend>> jsonResultCall = friendService.insertServiceFriend(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.FRIEND_INSERT_SUCCESS,StringUtil.FRIEND_INSERT_FAIL);
    }

    public void updateNotSycFriend(Handler handler, User toEntity, List<Bean<Friend>> packageToBean) {
        Call<JsonResult<Friend>> jsonResultCall = friendService.updateServiceFriend(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.FRIEND_UPDATE_SUCCESS,StringUtil.FRIEND_UPDATE_FAIL);

    }

    public void deleteNotSycFriend(Handler handler, User toEntity, List<Bean<Friend>> packageToBean) {
        Call<JsonResult<Friend>> jsonResultCall = friendService.removeServiceFriend(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.FRIEND_DELETE_SUCCESS,StringUtil.FRIEND_DELETE_FAIL);

    }
}
