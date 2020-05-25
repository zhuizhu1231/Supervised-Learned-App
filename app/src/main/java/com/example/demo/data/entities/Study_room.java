package com.example.demo.data.entities;

import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Study_room {

    private Integer id;
    private Integer userCreateId;
    private String name;
    private String detail;
    private Integer userCount;
    private Double createTime;
    private Double leastWorkTime;
    private Double timeStamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Double createTime) {
        this.createTime = createTime;
    }

    public Double getLeastWorkTime() {
        return leastWorkTime;
    }

    public void setLeastWorkTime(Double leastWorkTime) {
        this.leastWorkTime = leastWorkTime;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.Study_room toModel(com.example.demo.data.entities.Study_room data) {
        com.example.demo.data.model.Study_room room=new com.example.demo.data.model.Study_room();
        room.setDbId(data.getId());;
        room.setCreateTime(MyConverter.doubleToTimestamp(data.getCreateTime()));
        room.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        room.setStatus(StringUtil.SYC);
        room.setDetail(data.getDetail());
        room.setLeastWorkTime(MyConverter.doubleToTimestamp(data.getLeastWorkTime()));
        room.setName(data.getName());
        room.setUserCount(data.getUserCount());
        room.setUserCreateId(data.getUserCreateId());
        return room;
    }
    public static List<com.example.demo.data.model.Study_room> unpack(List<Bean<Study_room>> data){
        List<com.example.demo.data.model.Study_room> list=new ArrayList<>();
        for(Bean<Study_room> t:data){
            com.example.demo.data.model.Study_room a = Study_room.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
