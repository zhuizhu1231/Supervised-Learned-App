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
public class Task implements Serializable {
    @PrimaryKey
    private Integer id;
    private Integer aimId;
    private String title;
    private String remark;
    private Timestamp workTime;
    private Integer workCount;
    private Timestamp timeStamp;
    private Integer status;
    private Integer labelId;
    private Integer dbId;

    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public Task() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Timestamp getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Timestamp workTime) {
        this.workTime = workTime;
    }

    public Integer getWorkCount() {
        return workCount;
    }

    public void setWorkCount(Integer workCount) {
        this.workCount = workCount;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public static com.example.demo.data.entities.Task toEntity(com.example.demo.data.model.Task data) {
        com.example.demo.data.entities.Task task=new com.example.demo.data.entities.Task();
        task.setId(data.getDbId());;
        task.setRemark(data.getRemark());
        task.setTitle(data.getTitle());
        task.setLabelId(data.getLabelId());
        task.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        task.setAimId(data.getAimId());
        task.setWorkCount(data.getWorkCount());
        task.setWorkTime(MyConverter.timestampToDouble(data.getWorkTime()));
        return task;
    }
    public static List<Bean<com.example.demo.data.entities.Task>> packageToBean(List<com.example.demo.data.model.Task> data){
        List<Bean<com.example.demo.data.entities.Task>> list=new ArrayList<>();
        for(com.example.demo.data.model.Task t:data){
            Bean<com.example.demo.data.entities.Task> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Task.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
