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
public class Milepost implements Serializable {

    @PrimaryKey
    private Integer id;
    private Integer status;


    private String title;
    private String remark;
    private Timestamp dieTime;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getDieTime() {
        return dieTime;
    }

    public void setDieTime(Timestamp dieTime) {
        this.dieTime = dieTime;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Milepost() {
    }

    public static com.example.demo.data.entities.Milepost toEntity(com.example.demo.data.model.Milepost data) {
        com.example.demo.data.entities.Milepost milepost=new com.example.demo.data.entities.Milepost();
        milepost.setId(data.getDbId());
        milepost.setRemark(data.getRemark());
        milepost.setTitle(data.getTitle());
        milepost.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        milepost.setDieTime(MyConverter.timestampToDouble(data.getDieTime()));
        return milepost;
    }
    public static List<Bean<com.example.demo.data.entities.Milepost>> packageToBean(List<com.example.demo.data.model.Milepost> data){
        List<Bean<com.example.demo.data.entities.Milepost>> list=new ArrayList<>();
        for(com.example.demo.data.model.Milepost t:data){
            Bean<com.example.demo.data.entities.Milepost> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Milepost.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
