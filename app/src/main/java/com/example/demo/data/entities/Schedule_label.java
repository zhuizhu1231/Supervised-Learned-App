package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Schedule_label {


    private Integer id;
    private Integer userId;
    private String title;
    private Integer scheduleCount;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getScheduleCount() {
        return scheduleCount;
    }

    public void setScheduleCount(Integer scheduleCount) {
        this.scheduleCount = scheduleCount;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.Schedule_label toModel(com.example.demo.data.entities.Schedule_label data) {
        com.example.demo.data.model.Schedule_label label=new com.example.demo.data.model.Schedule_label();
        label.setDbId(data.getId());;
        label.setTitle(data.getTitle());
        label.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        label.setStatus(StringUtil.SYC);
        label.setScheduleCount(data.getScheduleCount());
        return label;
    }
    public static List<com.example.demo.data.model.Schedule_label> unpack(List<Bean<Schedule_label>> data){
        List<com.example.demo.data.model.Schedule_label> list=new ArrayList<>();
        for(Bean<Schedule_label> t:data){
            com.example.demo.data.model.Schedule_label a = Schedule_label.toModel(t.getData());
            a.setId(t.getOfflineId());

            list.add(a);
        }
        return list;
    }
}
