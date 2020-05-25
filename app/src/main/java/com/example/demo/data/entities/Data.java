package com.example.demo.data.entities;

import java.sql.Timestamp;

public class Data {
    private Integer taskId;
    private Timestamp time;


    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
