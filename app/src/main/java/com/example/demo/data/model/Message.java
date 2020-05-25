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
public class Message implements Serializable {
    @PrimaryKey
    private Integer id;
    private Integer sendId;
    private Integer receiverId;
    private Timestamp sendTime;
    private Integer isPrompt;
    private Integer isRead;
    private String content;
    private Timestamp timeStamp;
    private Integer dbId;
    private Integer status;

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

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
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

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Message() {
    }

    public static com.example.demo.data.entities.Message toEntity(com.example.demo.data.model.Message data) {
        com.example.demo.data.entities.Message message=new com.example.demo.data.entities.Message();
        message.setId(data.getDbId());;
        message.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        message.setIsPrompt(data.getIsPrompt());
        message.setContent(data.getContent());
        message.setIsRead(data.getIsRead());
        message.setReceiverId(data.getReceiverId());;
        message.setSendId(data.getSendId());
        message.setSendTime(MyConverter.timestampToDouble(data.getSendTime()));
        return message;
    }
    public static List<Bean<com.example.demo.data.entities.Message>> packageToBean(List<com.example.demo.data.model.Message> data){
        List<Bean<com.example.demo.data.entities.Message>> list=new ArrayList<>();
        for(com.example.demo.data.model.Message t:data){
            Bean<com.example.demo.data.entities.Message> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Message.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
