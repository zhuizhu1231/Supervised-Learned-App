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
public class Notes implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer status;
    private Timestamp timeStamp;
    private String content;
    private Timestamp lastTime;
    private Integer dbId;

    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public Notes() {
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Timestamp getLastTime() {
        return lastTime;
    }

    public void setLastTime(Timestamp lastTime) {
        this.lastTime = lastTime;
    }

    public static com.example.demo.data.entities.Notes toEntity(com.example.demo.data.model.Notes data) {
        com.example.demo.data.entities.Notes note=new com.example.demo.data.entities.Notes();
        note.setId(data.getDbId());
        note.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        note.setContent(data.getContent());
        note.setLastTime(MyConverter.timestampToDouble(data.getLastTime()));
        return note;
    }
    public static List<Bean<com.example.demo.data.entities.Notes>> packageToBean(List<com.example.demo.data.model.Notes> data){
        List<Bean<com.example.demo.data.entities.Notes>> list=new ArrayList<>();
        for(com.example.demo.data.model.Notes t:data){
            Bean<com.example.demo.data.entities.Notes> bean=new Bean<>();
            bean.setOfflineId(t.getId());
            bean.setData(com.example.demo.data.model.Notes.toEntity(t));
            list.add(bean);
        }
        return list;
    }
}
