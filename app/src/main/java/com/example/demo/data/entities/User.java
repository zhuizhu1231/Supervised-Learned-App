package com.example.demo.data.entities;



import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class User implements Serializable {


//    private Integer id;
//    private String name;
//    private String password;
//    private String sign;//在个人设置里设置
//    private String registerTime;
//    private String lastLoginTime;
//    private Integer sex;
//    private Integer isHide;//在个人设置里设置
//    private Integer isLogin;
//    private String restTime;
//    private String workTime;
//    private String String;
//    private String timeStamp;

    private Integer id;

    private String name;
    private String password;
    private String sign;
    private Double registerTime;
    private Double lastLoginTime;
    private Integer sex;
    private Integer isHide;

    private Double restTime;
    private Double workTime;

    private Double timeStamp;



    public User() {
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

    public Double getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Double registerTime) {
        this.registerTime = registerTime;
    }

    public Double getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Double lastLoginTime) {
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



    public Double getRestTime() {
        return restTime;
    }

    public void setRestTime(Double restTime) {
        this.restTime = restTime;
    }

    public Double getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Double workTime) {
        this.workTime = workTime;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static  com.example.demo.data.model.User toModel(com.example.demo.data.entities.User data) {
        com.example.demo.data.model.User user=new com.example.demo.data.model.User();
        user.setDbId(data.getId());;
        user.setName(data.getName());
        user.setPassword (data.getPassword());
        user.setSign ( data.getSign());
        user.setSex( data.getSex());
        user.setIsHide( data.getIsHide());
        user.setRegisterTime (MyConverter.doubleToTimestamp(data.getRegisterTime()));
        user.setLastLoginTime(MyConverter.doubleToTimestamp(data.getLastLoginTime()));
        user.setWorkTime(MyConverter.doubleToTimestamp( data.getWorkTime()));
        user.setRestTime(MyConverter.doubleToTimestamp(data.getRestTime()));
        user.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        user.setStatus(StringUtil.SYC);
        return user;
    }

    public static List<com.example.demo.data.model.User> unpack(List<Bean<User>> data){
        List<com.example.demo.data.model.User> list=new ArrayList<>();
        for(Bean<User> t:data){
            com.example.demo.data.model.User a = User.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}

