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
public class Schedule_label implements Serializable {
    @PrimaryKey
    private Integer id;
    private Integer status;

    private String title;
    private Integer scheduleCount;
    private Timestamp timeStamp;
    private Integer dbId;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.entities.Schedule_label toEntity(com.example.demo.data.model.Schedule_label data) {
        com.example.demo.data.entities.Schedule_label label=new com.example.demo.data.entities.Schedule_label();
        label.setId(data.getDbId());;
        label.setTitle(data.getTitle());
        label.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        label.setScheduleCount(data.getScheduleCount());
        return label;
    }
    public static List<Bean<com.example.demo.data.entities.Schedule_label>> packageToBean(List<com.example.demo.data.model.Schedule_label> data){
        List<Bean<com.example.demo.data.entities.Schedule_label>> list=new ArrayList<>();
        for(com.example.demo.data.model.Schedule_label t:data){
            Bean<com.example.demo.data.entities.Schedule_label> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Schedule_label.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
