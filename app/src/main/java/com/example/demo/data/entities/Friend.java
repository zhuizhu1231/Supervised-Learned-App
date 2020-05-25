package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Friend {
    private Integer id;
    private Integer isHide;
    private String remark;
    private String name;
    private Integer sex;
    private String sign;
    private Double timeStamp;
    private Double registerTime;
    private Integer friendId;
    public Integer getId() {
        return id;
    }

    public void setDbId(Integer id) {
        this.id = id;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsHide() {
        return isHide;
    }

    public void setIsHide(Integer isHide) {
        this.isHide = isHide;
    }

    public String getRemark() {
        return remark;
    }

    public Double getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Double registerTime) {
        this.registerTime = registerTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static  com.example.demo.data.model.Friend toModel(com.example.demo.data.entities.Friend data) {
        com.example.demo.data.model.Friend friend=new com.example.demo.data.model.Friend();
        friend.setDbId(data.getId());;
        friend.setName(data.getName());
        friend.setSign ( data.getSign());
        friend.setSex( data.getSex());
        friend.setFriendId(data.getFriendId());
        friend.setIsHide( data.getIsHide());
        friend.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        friend.setRegisterTime(MyConverter.doubleToTimestamp(data.getRegisterTime()));
        friend.setStatus(StringUtil.SYC);
        friend.setRemark(data.getRemark());
        return friend;
    }
    public static List<com.example.demo.data.model.Friend> unpack(List<Bean<Friend>> data){
        List<com.example.demo.data.model.Friend> list=new ArrayList<>();
        for(Bean<Friend> t:data){
            com.example.demo.data.model.Friend a = Friend.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
