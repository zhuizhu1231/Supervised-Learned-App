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
public class Friend implements Serializable {
    @PrimaryKey
    private Integer id;
    private String name;
    private Integer friendId;
    private Integer sex;
    private String sign;
    private Integer isHide;
    private String remark;
    private Timestamp registerTime;
    private Integer status;
    private Timestamp timeStamp;
    private Integer dbId;
    public Friend() {
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Integer getIsHide() {
        return isHide;
    }

    public void setIsHide(Integer isHide) {
        this.isHide = isHide;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    public static  com.example.demo.data.entities.Friend toEntity(com.example.demo.data.model.Friend data) {
        com.example.demo.data.entities.Friend friend=new com.example.demo.data.entities.Friend();
        friend.setId(data.getDbId());;
        friend.setName(data.getName());
        friend.setFriendId(data.getFriendId());
        friend.setSign ( data.getSign());
        friend.setSex( data.getSex());
        friend.setIsHide( data.getIsHide());
        friend.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        friend.setRegisterTime(MyConverter.timestampToDouble(data.getRegisterTime()));
        friend.setRemark(data.getRemark());
        return friend;
    }

    public static List<Bean<com.example.demo.data.entities.Friend>> packageToBean(List<com.example.demo.data.model.Friend> data){
        List<Bean<com.example.demo.data.entities.Friend>> list=new ArrayList<>();
        for(com.example.demo.data.model.Friend t:data){
            Bean<com.example.demo.data.entities.Friend> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Friend.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
