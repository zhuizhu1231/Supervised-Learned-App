package com.example.demo.data.datasource;

import android.os.Handler;

import com.example.demo.data.entities.Study_room;
import com.example.demo.data.entities.User;
import com.example.demo.data.entities.User_in_study_room;
import com.example.demo.net.json.Bean;
import com.example.demo.net.json.JsonResult;
import com.example.demo.net.service.StudyRoomService;
import com.example.demo.util.RetrofitManager;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.List;

import retrofit2.Call;

public class StudyRoomDataSource {
    StudyRoomService studyRoomService;

    public StudyRoomDataSource(){
        studyRoomService = RetrofitManager.getRetrofit().create(StudyRoomService.class);
    }
    public void netQueryStudyRoomList(Handler handler, User toEntity, Double timestampToDouble) {
        Call<JsonResult<Study_room>> jsonResultCall = studyRoomService.QueryServiceStudy_roomList(StringUtil.getSessionId(), toEntity.getId(), timestampToDouble);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.ROOM_QUERY_LIST_SUCCESS,StringUtil.ROOM_QUERY_LIST_FAIL);
    }

    public void netSearchRoomListByNameLikeOrDbIdLike(Handler handler, String key) {
        Call<JsonResult<Study_room>> jsonResultCall = studyRoomService.QueryServiceStudy_roomLikeList(StringUtil.getSessionId(), com.example.demo.data.model.User.toEntity(StringUtil.getUser()).getId(), key);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.QUERY_ROOM_LIKE_SUCCESS,StringUtil.QUERY_ROOM_LIKE_FAIL);

    }

    public void netUpdateRoom(Handler handler, User toEntity, List<Bean<Study_room>> packageToBean) {
        Call<JsonResult<Study_room>> jsonResultCall = studyRoomService.updateServiceStudy_room(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.ROOM_UPDATE_SUCCESS,StringUtil.ROOM_UPDATE_FAIL);

    }

    public void netDeleteRoom(Handler handler, User toEntity, List<Bean<Study_room>> packageToBean) {
        Call<JsonResult<Study_room>> jsonResultCall = studyRoomService.removeServiceStudy_room(StringUtil.getSessionId(), toEntity.getId(), packageToBean);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.ROOM_DELETE_SUCCESS,StringUtil.ROOM_DELETE_FAIL);

    }

    public void netCreateRoom(Handler handler, User toEntity, Study_room room) {
        Call<JsonResult<Study_room>> jsonResultCall = studyRoomService.insertServiceStudy_room(StringUtil.getSessionId(), toEntity.getId(),  room);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.ROOM_CREATE_SUCCESS,StringUtil.ROOM_CREATE_FAIL);

    }

    public void netQueryMemberListByRoomId(Handler handler, User toEntity, Integer dbId) {
        Call<JsonResult<User_in_study_room>> jsonResultCall = studyRoomService.QueryServiceRoomMemberByRoomId(StringUtil.getSessionId(), toEntity.getId(), dbId);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.ROOM_QUERY_MEMBER_LIST_SUCCESS,StringUtil.ROOM_QUERY_MEMBER_LIST_FAIL);
    }

    public void netRefleshRoomByDbId(Handler handler,Integer dbId) {
        Call<JsonResult<Study_room>> jsonResultCall = studyRoomService.QueryServiceStudy_roomById(StringUtil.getSessionId(), StringUtil.getUser().getDbId(), dbId);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.QUERY_ROOM_ID_SUCCESS,StringUtil.QUERY_ROOM_ID_FAIL);

    }


    public void addRoomByDbId(Handler handler, Integer dbId) {
        Call<JsonResult<Study_room>> jsonResultCall = studyRoomService.joinServiceStudy_room(StringUtil.getSessionId(), StringUtil.getUser().getDbId(), dbId);
        Tool.netJsonResultRequest(jsonResultCall,handler,StringUtil.JOIN_ROOM_ID_SUCCESS,StringUtil.JOIN_ROOM_ID_FAIL);

    }
}
