package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Schedule_in_label {


    private Integer id;
    private Integer userId;
    private Integer scheduleId;
    private Integer scheduleLabelId;
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

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getScheduleLabelId() {
        return scheduleLabelId;
    }

    public void setScheduleLabelId(Integer scheduleLabelId) {
        this.scheduleLabelId = scheduleLabelId;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.Schedule_in_label toModel(com.example.demo.data.entities.Schedule_in_label data) {
        com.example.demo.data.model.Schedule_in_label relation=new com.example.demo.data.model.Schedule_in_label();
        relation.setDbId(data.getId());;
        relation.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        relation.setStatus(StringUtil.SYC);
        relation.setScheduleId(data.getScheduleId());
        relation.setScheduleLabelId(data.getScheduleLabelId());
        return relation;
    }   
    public static List<com.example.demo.data.model.Schedule_in_label> unpack(List<Bean<Schedule_in_label>> data){
        List<com.example.demo.data.model.Schedule_in_label> list=new ArrayList<>();
        for(Bean<Schedule_in_label> t:data){
            com.example.demo.data.model.Schedule_in_label a = Schedule_in_label.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
