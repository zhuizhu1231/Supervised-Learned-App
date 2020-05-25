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
public class User implements Serializable {
    @PrimaryKey
    private Integer id;
    private String name;
    private String password;
    private String sign;
    private Timestamp registerTime;
    private Timestamp lastLoginTime;
    private Integer sex;
    private Integer isHide;
    private Timestamp restTime;
    private Timestamp workTime;
    private Integer status;
    private Timestamp timeStamp;
    private Integer dbId;
    public User() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getIsHide() {
        return isHide;
    }

    public void setIsHide(Integer isHide) {
        this.isHide = isHide;
    }

    public Timestamp getRestTime() {
        return restTime;
    }

    public void setRestTime(Timestamp restTime) {
        this.restTime = restTime;
    }

    public Timestamp getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Timestamp workTime) {
        this.workTime = workTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public static com.example.demo.data.entities.User toEntity(com.example.demo.data.model.User data) {
        com.example.demo.data.entities.User user=new com.example.demo.data.entities.User();
        user.setId(data.getDbId());;
        user.setName(data.getName());
        user.setPassword (data.getPassword());
        user.setSign ( data.getSign());
        user.setSex( data.getSex());
        user.setIsHide( data.getIsHide());
        user.setRegisterTime (MyConverter.timestampToDouble(data.getRegisterTime()));
        user.setLastLoginTime(MyConverter.timestampToDouble(data.getLastLoginTime()));
        user.setWorkTime(MyConverter.timestampToDouble( data.getWorkTime()));
        user.setRestTime(MyConverter.timestampToDouble(data.getRestTime()));
        user.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        return user;
    }
    public static List<Bean<com.example.demo.data.entities.User>> packageToBean(List<User> data){
        List<Bean<com.example.demo.data.entities.User>> list=new ArrayList<>();
        for(com.example.demo.data.model.User t:data){
            Bean<com.example.demo.data.entities.User> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.User.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
