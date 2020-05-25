package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Milepost {

    private Integer id;
    private Integer userId;
    private String title;
    private String remark;
    private Double dieTime;
    private Double timeStamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Double getDieTime() {
        return dieTime;
    }

    public void setDieTime(Double dieTime) {
        this.dieTime = dieTime;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.Milepost toModel(com.example.demo.data.entities.Milepost data) {
        com.example.demo.data.model.Milepost milepost=new com.example.demo.data.model.Milepost();
        milepost.setDbId(data.getId());
        milepost.setRemark(data.getRemark());
        milepost.setTitle(data.getTitle());
        milepost.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        milepost.setStatus(StringUtil.SYC);
        milepost.setDieTime(MyConverter.doubleToTimestamp(data.getDieTime()));
        return milepost;
    }
    public static List<Bean<Milepost>> packageToBean(List<com.example.demo.data.model.Milepost> data){
        List<Bean<com.example.demo.data.entities.Milepost>> list=new ArrayList<>();
        for(com.example.demo.data.model.Milepost t:data){
            Bean<com.example.demo.data.entities.Milepost> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Milepost.toEntity(t));
            list.add(bean);
        }
        return list;
    }
    public static List<com.example.demo.data.model.Milepost> unpack(List<Bean<Milepost>> data){
        List<com.example.demo.data.model.Milepost> list=new ArrayList<>();
        for(Bean<Milepost> t:data){
            com.example.demo.data.model.Milepost a = Milepost.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}

