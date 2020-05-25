package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Notes_label {

    private Integer id;
    private Integer userId;
    private String title;
    private Integer notesCount;
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

    public Integer getNotesCount() {
        return notesCount;
    }

    public void setNotesCount(Integer notesCount) {
        this.notesCount = notesCount;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.Notes_label toModel(com.example.demo.data.entities.Notes_label data) {
        com.example.demo.data.model.Notes_label label=new com.example.demo.data.model.Notes_label();
        label.setDbId(data.getId());
        label.setTitle(data.getTitle());
        label.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        label.setStatus(StringUtil.SYC);
        label.setNotesCount(data.getNotesCount());
        return label;
    }
    public static List<com.example.demo.data.model.Notes_label> unpack(List<Bean<Notes_label>> data){
        List<com.example.demo.data.model.Notes_label> list=new ArrayList<>();
        for(Bean<Notes_label> t:data){
            com.example.demo.data.model.Notes_label a = Notes_label.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
