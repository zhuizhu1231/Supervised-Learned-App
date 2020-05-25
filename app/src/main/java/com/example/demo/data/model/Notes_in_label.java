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
public class Notes_in_label implements Serializable {
    @PrimaryKey
    private Integer id;
    private Integer status;
    private Integer notesId;
    private Integer notesLabelId;
    private Timestamp timeStamp;
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

    public Integer getNotesId() {
        return notesId;
    }

    public void setNotesId(Integer notesId) {
        this.notesId = notesId;
    }

    public Integer getNotesLabelId() {
        return notesLabelId;
    }

    public void setNotesLabelId(Integer notesLabelId) {
        this.notesLabelId = notesLabelId;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Notes_in_label() {
    }

    public static com.example.demo.data.entities.Notes_in_label toEntity(com.example.demo.data.model.Notes_in_label data) {
        com.example.demo.data.entities.Notes_in_label label=new com.example.demo.data.entities.Notes_in_label();
        label.setId(data.getDbId());;
        label.setTimeStamp (MyConverter.timestampToDouble(data.getTimeStamp()));
        label.setNotesId(data.getNotesId());
        label.setNotesLabelId(data.getNotesLabelId());
        return label;
    }

    public static List<Bean<com.example.demo.data.entities.Notes_in_label>> packageToBean(List<com.example.demo.data.model.Notes_in_label> data){
        List<Bean<com.example.demo.data.entities.Notes_in_label>> list=new ArrayList<>();
        for(com.example.demo.data.model.Notes_in_label t:data){
            Bean<com.example.demo.data.entities.Notes_in_label> bean=new Bean<>();
            if(t!=null){
                bean.setOfflineId(t.getId());
                bean.setData(com.example.demo.data.model.Notes_in_label.toEntity(t));
                list.add(bean);
            }
        }
        return list;
    }
}
