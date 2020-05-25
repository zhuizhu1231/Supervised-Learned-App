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
public class TaskLabel implements Serializable {
    @PrimaryKey
    private Integer id;
    private Integer status;
   
    private Timestamp timeStamp;
    private String title;
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
    

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public TaskLabel() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public static com.example.demo.data.entities.TaskLabel toEntity(TaskLabel data) {
        com.example.demo.data.entities.TaskLabel label=new com.example.demo.data.entities.TaskLabel();
        label.setId(data.getDbId());
        label.setTitle(data.getTitle());
        label.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
       
        return label;
    }
    public static List<Bean<com.example.demo.data.entities.TaskLabel>> packageToBean(List<TaskLabel> data){
        List<Bean<com.example.demo.data.entities.TaskLabel>> list=new ArrayList<>();
        for(TaskLabel t:data){
            Bean<com.example.demo.data.entities.TaskLabel> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(TaskLabel.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
