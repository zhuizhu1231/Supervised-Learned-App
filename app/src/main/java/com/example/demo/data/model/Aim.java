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
public class Aim implements Serializable {
    @PrimaryKey
    private Integer id;
    private Integer status;
    private String title;
    private String remark;
    private Timestamp createTime;
    private Timestamp restTime;
    private Timestamp timeStamp;
    private Integer labelId;
    private Integer dbId;

    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public Aim() {
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getRestTime() {
        return restTime;
    }

    public void setRestTime(Timestamp restTime) {
        this.restTime = restTime;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
    public static com.example.demo.data.entities.Aim toEntity(com.example.demo.data.model.Aim data) {
        com.example.demo.data.entities.Aim aim=new com.example.demo.data.entities.Aim();
        aim.setId(data.getDbId());;
        aim.setCreateTime(MyConverter.timestampToDouble(data.getCreateTime()));
        aim.setRemark(data.getRemark());
        aim.setTitle(data.getTitle());
        aim.setLabelId(data.getLabelId());
        aim.setRestTime(MyConverter.timestampToDouble(data.getRestTime()));
        aim.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        return aim;
    }

    public static List<Bean<com.example.demo.data.entities.Aim>> packageToBean(List<com.example.demo.data.model.Aim> data){
        List<Bean<com.example.demo.data.entities.Aim>> list=new ArrayList<>();
        for(com.example.demo.data.model.Aim t:data){
            Bean<com.example.demo.data.entities.Aim> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Aim.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
