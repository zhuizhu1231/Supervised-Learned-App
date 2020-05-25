package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class User_in_study_room {

    private Integer id;
    private Integer userId;
    private Integer studyRoomId;
    private Double joinTime;
    private String nameInRoom;
    private Integer isCreate;
    private Double timeStamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStudyRoomId() {
        return studyRoomId;
    }

    public void setStudyRoomId(Integer studyRoomId) {
        this.studyRoomId = studyRoomId;
    }

    public Double getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Double joinTime) {
        this.joinTime = joinTime;
    }

    public String getNameInRoom() {
        return nameInRoom;
    }

    public void setNameInRoom(String nameInRoom) {
        this.nameInRoom = nameInRoom;
    }

    public Integer getIsCreate() {
        return isCreate;
    }

    public void setIsCreate(Integer isCreate) {
        this.isCreate = isCreate;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.User_in_study_room toModel(com.example.demo.data.entities.User_in_study_room data) {
        com.example.demo.data.model.User_in_study_room member=new com.example.demo.data.model.User_in_study_room();
        member.setDbId(data.getId());
        member.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        member.setStatus(StringUtil.SYC);
        member.setIsCreate(data.getIsCreate());
        member.setJoinTime(MyConverter.doubleToTimestamp(data.getJoinTime()));
        member.setNameInRoom(data.getNameInRoom());
        member.setStudyRoomId(data.getStudyRoomId());
        member.setUserId(data.getUserId());
        return member;
    }
    public static List<com.example.demo.data.model.User_in_study_room> unpack(List<Bean<User_in_study_room>> data){
        List<com.example.demo.data.model.User_in_study_room> list=new ArrayList<>();
        for(Bean<User_in_study_room> t:data){
            com.example.demo.data.model.User_in_study_room a = User_in_study_room.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
