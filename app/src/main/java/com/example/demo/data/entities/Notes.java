package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Notes {

    private Integer id;
    private Integer userId;
    private String content;
    private Double lastTime;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getLastTime() {
        return lastTime;
    }

    public void setLastTime(Double lastTime) {
        this.lastTime = lastTime;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.Notes toModel(com.example.demo.data.entities.Notes data) {
        com.example.demo.data.model.Notes note=new com.example.demo.data.model.Notes();
        note.setDbId(data.getId());;
        note.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        note.setStatus(StringUtil.SYC);
        note.setContent(data.getContent());
        note.setLastTime(MyConverter.doubleToTimestamp(data.getLastTime()));
        return note;
    }
    public static List<com.example.demo.data.model.Notes> unpack(List<Bean<Notes>> data){
        List<com.example.demo.data.model.Notes> list=new ArrayList<>();
        for(Bean<Notes> t:data){
            com.example.demo.data.model.Notes a = Notes.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
