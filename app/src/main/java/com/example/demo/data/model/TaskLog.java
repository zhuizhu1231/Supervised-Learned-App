package com.example.demo.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TaskLog {
    @PrimaryKey
    private Integer id;
    private Integer dbId;
    private Integer  taskId;
    private Timestamp timeStamp;
    private Integer status;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public Integer getTaskId() {
        return  taskId;
    }

    public void setTaskId(Integer logId) {
        this. taskId = logId;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.entities.TaskLog toEntity(com.example.demo.data.model.TaskLog data) {
        com.example.demo.data.entities.TaskLog log=new com.example.demo.data.entities.TaskLog();
        log.setId(data.getDbId());
        log.setTaskId(data.getTaskId());
        log.setTimeStamp(MyConverter.timestampToDouble(data.getTimeStamp()));
        return log;
    }
    public static List<Bean<com.example.demo.data.entities.TaskLog>> packageToBean(List<com.example.demo.data.model.TaskLog> data){
        List<Bean<com.example.demo.data.entities.TaskLog>> list=new ArrayList<>();
        for(com.example.demo.data.model.TaskLog t:data){
            Bean<com.example.demo.data.entities.TaskLog> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.TaskLog.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
