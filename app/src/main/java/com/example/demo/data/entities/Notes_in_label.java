package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Notes_in_label {

    private Integer id;
    private Integer userId;
    private Integer notesId;
    private Integer notesLabelId;
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

    public Double getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static com.example.demo.data.model.Notes_in_label toModel(com.example.demo.data.entities.Notes_in_label data) {
        com.example.demo.data.model.Notes_in_label relation=new com.example.demo.data.model.Notes_in_label();
        relation.setDbId(data.getId());;
        relation.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        relation.setStatus(StringUtil.SYC);
        relation.setNotesId(data.getNotesId());
        relation.setNotesLabelId(data.getNotesLabelId());
        return relation;
    }
    public static List<com.example.demo.data.model.Notes_in_label> unpack(List<Bean<Notes_in_label>> data){
        List<com.example.demo.data.model.Notes_in_label> list=new ArrayList<>();
        for(Bean<Notes_in_label> t:data){
            com.example.demo.data.model.Notes_in_label a = Notes_in_label.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
