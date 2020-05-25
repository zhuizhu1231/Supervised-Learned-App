package com.example.demo.data.entities;

import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskLog {
    private Integer id;
    private Integer userId;
    private Integer taskId;
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

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.TaskLog toModel(com.example.demo.data.entities.TaskLog data) {
        com.example.demo.data.model.TaskLog task=new com.example.demo.data.model.TaskLog();
        task.setDbId(data.getId());
        task.setTaskId(data.getTaskId());
        task.setStatus(StringUtil.SYC);
        task.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        return task;
    }
    public static List<com.example.demo.data.model.TaskLog> unpack(List<Bean<TaskLog>> data){
        List<com.example.demo.data.model.TaskLog> list=new ArrayList<>();
        for(Bean<TaskLog> t:data){
            com.example.demo.data.model.TaskLog a = TaskLog.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
