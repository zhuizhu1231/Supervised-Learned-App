package com.example.demo.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Schedule_in_label implements Serializable {
    @PrimaryKey
    private Integer id;
    private Integer status;
    private Integer scheduleId;
    private Integer scheduleLabelId;
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

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.entities.Schedule_in_label toEntity(com.example.demo.data.model.Schedule_in_label data) {
        com.example.demo.data.entities.Schedule_in_label relation=new com.example.demo.data.entities.Schedule_in_label();
        relation.setId(data.getDbId());
        relation.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        relation.setScheduleId(data.getScheduleId());
        relation.setScheduleLabelId(data.getScheduleLabelId());
        return relation;
    }
    public static List<Bean<com.example.demo.data.entities.Schedule_in_label>> packageToBean(List<com.example.demo.data.model.Schedule_in_label> data){
        List<Bean<com.example.demo.data.entities.Schedule_in_label>> list=new ArrayList<>();
        for(com.example.demo.data.model.Schedule_in_label t:data){
            Bean<com.example.demo.data.entities.Schedule_in_label> bean=new Bean<>();
            //System.out.println();
            if(t!=null) {
                bean.setOfflineId(t.getId());
                bean.setData(com.example.demo.data.model.Schedule_in_label.toEntity(t));
                list.add(bean);
            }
        }
        return list;
    }
}
