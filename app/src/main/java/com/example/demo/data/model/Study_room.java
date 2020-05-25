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
public class Study_room implements Serializable {

    @PrimaryKey
    private Integer id;
    private Integer status;
    private Integer userCreateId;
    private String name;
    private String detail;
    private Integer userCount;
    private Timestamp createTime;
    private Timestamp leastWorkTime;
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

    public Integer getUserCreateId() {
        return userCreateId;
    }

    public void setUserCreateId(Integer userCreateId) {
        this.userCreateId = userCreateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getLeastWorkTime() {
        return leastWorkTime;
    }

    public void setLeastWorkTime(Timestamp leastWorkTime) {
        this.leastWorkTime = leastWorkTime;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.entities.Study_room toEntity(com.example.demo.data.model.Study_room data) {
        com.example.demo.data.entities.Study_room room=new com.example.demo.data.entities.Study_room();
        room.setId(data.getDbId());;
        room.setCreateTime(MyConverter.timestampToDouble(data.getCreateTime()));
        room.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        room.setDetail(data.getDetail());
        room.setLeastWorkTime(MyConverter.timestampToDouble(data.getLeastWorkTime()));
        room.setName(data.getName());
        room.setUserCount(data.getUserCount());
        room.setUserCreateId(data.getUserCreateId());
        return room;
    }
    public static List<Bean<com.example.demo.data.entities.Study_room>> packageToBean(List<com.example.demo.data.model.Study_room> data){
        List<Bean<com.example.demo.data.entities.Study_room>> list=new ArrayList<>();
        for(com.example.demo.data.model.Study_room t:data){
            Bean<com.example.demo.data.entities.Study_room> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Study_room.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
