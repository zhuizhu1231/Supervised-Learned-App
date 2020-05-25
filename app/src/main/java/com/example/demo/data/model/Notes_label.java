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
public class Notes_label implements Serializable {
    @PrimaryKey
    private Integer id;
    private Integer status;
    private Integer notesCount;
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


    public Integer getNotesCount() {
        return notesCount;
    }

    public void setNotesCount(Integer notesCount) {
        this.notesCount = notesCount;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Notes_label() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public static com.example.demo.data.entities.Notes_label toEntity(com.example.demo.data.model.Notes_label data) {
        com.example.demo.data.entities.Notes_label label=new com.example.demo.data.entities.Notes_label();
        label.setId(data.getDbId());
        label.setTitle(data.getTitle());
        label.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        label.setNotesCount(data.getNotesCount());
        return label;
    }
    public static List<Bean<com.example.demo.data.entities.Notes_label>> packageToBean(List<com.example.demo.data.model.Notes_label> data){
        List<Bean<com.example.demo.data.entities.Notes_label>> list=new ArrayList<>();
        for(com.example.demo.data.model.Notes_label t:data){
            Bean<com.example.demo.data.entities.Notes_label> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Notes_label.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
