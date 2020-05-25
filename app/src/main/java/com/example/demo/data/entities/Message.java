package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Message {

    private Integer id;
    private Integer sendId;
    private Integer receiverId;
    private Double sendTime;
    private Integer isPrompt;
    private Integer isRead;
    private String content;
    private Double timeStamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Double getSendTime() {
        return sendTime;
    }

    public void setSendTime(Double sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getIsPrompt() {
        return isPrompt;
    }

    public void setIsPrompt(Integer isPrompt) {
        this.isPrompt = isPrompt;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
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

    public static com.example.demo.data.model.Message toModel(com.example.demo.data.entities.Message data) {
        com.example.demo.data.model.Message message=new com.example.demo.data.model.Message();
        message.setDbId(data.getId());;
        message.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        message.setStatus(StringUtil.SYC);
        message.setIsPrompt(data.getIsPrompt());
        message.setContent(data.getContent());
        message.setIsRead(data.getIsRead());
        message.setReceiverId(data.getReceiverId());;
        message.setSendId(data.getSendId());
        message.setSendTime(MyConverter.doubleToTimestamp(data.getSendTime()));
        return message;
    }
    public static List<com.example.demo.data.model.Message> unpack(List<Bean<Message>> data){
        List<com.example.demo.data.model.Message> list=new ArrayList<>();
        for(Bean<Message> t:data){
            com.example.demo.data.model.Message a = Message.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
