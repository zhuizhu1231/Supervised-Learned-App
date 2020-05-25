package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Schedule {


    private Integer userId;

    private String title;


    private Integer category;//import and
    private String remark;
    private Integer property;//assign or


    private Integer id;

    private Double lastModifyTime;

    private Integer isDone;
    private Double belongTime;

    private Double timeStamp;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getProperty() {
        return property;
    }

    public void setProperty(Integer property) {
        this.property = property;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public Integer getIsDone() {
        return isDone;
    }

    public void setIsDone(Integer isDone) {
        this.isDone = isDone;
    }

    public Double getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Double lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Double getBelongTime() {
        return belongTime;
    }

    public void setBelongTime(Double belongTime) {
        this.belongTime = belongTime;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static  com.example.demo.data.model.Schedule toModel(com.example.demo.data.entities.Schedule data) {
        com.example.demo.data.model.Schedule schedule=new com.example.demo.data.model.Schedule();
        schedule.setDbId(data.getId());
        schedule.setBelongTime(MyConverter.doubleToTimestamp(data.getBelongTime()));
        schedule.setCategory(data.getCategory());
        schedule.setIsDone(data.getIsDone());
        schedule.setLastModifyTime(MyConverter.doubleToTimestamp(data.getLastModifyTime()));
        schedule.setProperty(data.getProperty());
        schedule.setRemark(data.getRemark());
        schedule.setStatus(StringUtil.SYC);
        schedule.setTimeStamp(MyConverter.doubleToTimestamp(data.getTimeStamp()));
        schedule.setTitle(data.getTitle());
        return schedule;
    }
    public static List<com.example.demo.data.model.Schedule> unpack(List<Bean<Schedule>> data){
        List<com.example.demo.data.model.Schedule> list=new ArrayList<>();
        for(Bean<Schedule> t:data){
            com.example.demo.data.model.Schedule a = Schedule.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
