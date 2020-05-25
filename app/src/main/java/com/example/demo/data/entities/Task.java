package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Task {

    private Integer id;
    private Integer userId;
    private Integer aimId;
    private String title;
    private String remark;
    private Double workTime;
    private Integer workCount;
    private Double timeStamp;
    private Integer labelId;
    public Integer getId() {
        return id;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
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

    public Integer getAimId() {
        return aimId;
    }

    public void setAimId(Integer aimId) {
        this.aimId = aimId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Double workTime) {
        this.workTime = workTime;
    }

    public Integer getWorkCount() {
        return workCount;
    }

    public void setWorkCount(Integer workCount) {
        this.workCount = workCount;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.Task toModel(com.example.demo.data.entities.Task data) {
        com.example.demo.data.model.Task task=new com.example.demo.data.model.Task();
        task.setDbId(data.getId());;
        task.setRemark(data.getRemark());
        task.setTitle(data.getTitle());
        task.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        task.setStatus(StringUtil.SYC);
        task.setLabelId(data.getLabelId());
        task.setAimId(data.getAimId());
        task.setWorkCount(data.getWorkCount());
        task.setWorkTime(MyConverter.doubleToTimestamp(data.getWorkTime()));
        return task;
    }
    public static List<com.example.demo.data.model.Task> unpack(List<Bean<Task>> data){
        List<com.example.demo.data.model.Task> list=new ArrayList<>();
        for(Bean<Task> t:data){
            com.example.demo.data.model.Task a = Task.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
