package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class TaskLabel {

    private Integer id;
    private Integer userId;
    private String title;
   
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



    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.TaskLabel toModel(TaskLabel data) {
        com.example.demo.data.model.TaskLabel label=new com.example.demo.data.model.TaskLabel();
        label.setDbId(data.getId());
        label.setTitle(data.getTitle());
        label.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        label.setStatus(StringUtil.SYC);
       
        return label;
    }
    public static List<com.example.demo.data.model.TaskLabel> unpack(List<Bean<TaskLabel>> data){
        List<com.example.demo.data.model.TaskLabel> list=new ArrayList<>();
        for(Bean<TaskLabel> t:data){
            com.example.demo.data.model.TaskLabel a = TaskLabel.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
