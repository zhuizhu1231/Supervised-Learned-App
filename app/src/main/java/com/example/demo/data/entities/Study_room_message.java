package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Study_room_message {

    private Integer id;
    private Integer studyRoomId;
    private Integer sendUserId;
    private Double sendTime;
    private String content;
    private Double timeStamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getSendTime() {
        return sendTime;
    }

    public void setSendTime(Double sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.Study_room_message toModel(com.example.demo.data.entities.Study_room_message data) {
        com.example.demo.data.model.Study_room_message message=new com.example.demo.data.model.Study_room_message();
        message.setDbId(data.getId());
        message.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        message.setStatus(StringUtil.SYC);
        message.setContent(data.getContent());
        message.setSendTime(MyConverter.doubleToTimestamp(data.getSendTime()));
        message.setSendUserId(data.getSendUserId());
        message.setStudyRoomId(data.getStudyRoomId());
        return message;
    }
    public static List<com.example.demo.data.model.Study_room_message> unpack(List<Bean<Study_room_message>> data){
        List<com.example.demo.data.model.Study_room_message> list=new ArrayList<>();
        for(Bean<Study_room_message> t:data){
            com.example.demo.data.model.Study_room_message a = Study_room_message.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
