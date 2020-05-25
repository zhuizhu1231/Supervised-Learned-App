package com.example.demo.data.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User_in_study_room implements Serializable {

    @PrimaryKey
    private Integer id;
    private Integer status;
    private Integer userId;
    private Integer studyRoomId;

    private Timestamp joinTime;
    private String nameInRoom;
    private Integer isCreate;
    private Timestamp timeStamp;

    private Integer dbId;

    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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



    public Timestamp getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Timestamp joinTime) {
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

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.entities.User_in_study_room toEntity(com.example.demo.data.model.User_in_study_room data) {
        com.example.demo.data.entities.User_in_study_room member=new com.example.demo.data.entities.User_in_study_room();
        member.setId(data.getDbId());
        member.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        member.setIsCreate(data.getIsCreate());
        member.setJoinTime(MyConverter.timestampToDouble(data.getJoinTime()));
        member.setNameInRoom(data.getNameInRoom());
        member.setStudyRoomId(data.getStudyRoomId());
        member.setUserId(data.getUserId());
        return member;
    }
    public static List<Bean<com.example.demo.data.entities.User_in_study_room>> packageToBean(List<com.example.demo.data.model.User_in_study_room> data){
        List<Bean<com.example.demo.data.entities.User_in_study_room>> list=new ArrayList<>();
        for(com.example.demo.data.model.User_in_study_room t:data){
            Bean<com.example.demo.data.entities.User_in_study_room> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.User_in_study_room.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
