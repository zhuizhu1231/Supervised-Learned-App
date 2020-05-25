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
public class Study_room_message implements Serializable {

    @PrimaryKey
    private Integer id;
    private Integer status;
    private Integer studyRoomId;
    private Integer sendUserId;
    private Timestamp sendTime;
    private String content;
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

    public Integer getStudyRoomId() {
        return studyRoomId;
    }

    public void setStudyRoomId(Integer studyRoomId) {
        this.studyRoomId = studyRoomId;
    }

    public Integer getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Integer sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.entities.Study_room_message toEntity(com.example.demo.data.model.Study_room_message data) {
        com.example.demo.data.entities.Study_room_message message=new com.example.demo.data.entities.Study_room_message();
        message.setId(data.getDbId());
        message.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        message.setContent(data.getContent());
        message.setSendTime(MyConverter.timestampToDouble(data.getSendTime()));
        message.setSendUserId(data.getSendUserId());
        message.setStudyRoomId(data.getStudyRoomId());
        return message;
    }
    public static List<Bean<com.example.demo.data.entities.Study_room_message>> packageToBean(List<com.example.demo.data.model.Study_room_message> data){
        List<Bean<com.example.demo.data.entities.Study_room_message>> list=new ArrayList<>();
        for(com.example.demo.data.model.Study_room_message t:data){
            Bean<com.example.demo.data.entities.Study_room_message> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Study_room_message.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}


