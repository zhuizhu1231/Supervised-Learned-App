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
public class Schedule implements Serializable {

    @PrimaryKey
    private Integer id;
    private Integer status;
    private Timestamp lastModifyTime;
    private String title;
    private Integer isDone;
    private Timestamp belongTime;
    private Integer category;//import and
    private String remark;
    private Integer property;//assign or
    private Timestamp timeStamp;
    private Integer dbId;
    public Schedule() {
    }

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

    public Timestamp getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Timestamp lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIsDone() {
        return isDone;
    }

    public void setIsDone(Integer isDone) {
        this.isDone = isDone;
    }

    public Timestamp getBelongTime() {
        return belongTime;
    }

    public void setBelongTime(Timestamp belongTime) {
        this.belongTime = belongTime;
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

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
    public static  com.example.demo.data.entities.Schedule toEntity(com.example.demo.data.model.Schedule data) {
        com.example.demo.data.entities.Schedule schedule=new com.example.demo.data.entities.Schedule();
        schedule.setId(data.getDbId());
        schedule.setBelongTime(MyConverter.timestampToDouble(data.getBelongTime()));
        schedule.setCategory(data.getCategory());
        schedule.setIsDone(data.getIsDone());
        schedule.setLastModifyTime(MyConverter.timestampToDouble(data.getLastModifyTime()));
        schedule.setProperty(data.getProperty());
        schedule.setRemark(data.getRemark());
        schedule.setTimeStamp(MyConverter.timestampToDouble(data.getTimeStamp()));
        schedule.setTitle(data.getTitle());
        return schedule;
    }

    public static List<Bean<com.example.demo.data.entities.Schedule>> packageToBean(List<com.example.demo.data.model.Schedule> data){
        List<Bean<com.example.demo.data.entities.Schedule>> list=new ArrayList<>();
        for(com.example.demo.data.model.Schedule t:data){
            Bean<com.example.demo.data.entities.Schedule> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Schedule.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
