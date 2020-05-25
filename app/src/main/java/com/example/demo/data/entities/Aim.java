package com.example.demo.data.entities;


import com.example.demo.converter.MyConverter;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Aim {

    private Integer id;

    private String title;
    private String remark;
    private Double createTime;
    private Double restTime;
    private Double timeStamp;
    private Integer labelId;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Double getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Double createTime) {
        this.createTime = createTime;
    }

    public Double getRestTime() {
        return restTime;
    }

    public void setRestTime(Double restTime) {
        this.restTime = restTime;
    }

    public Double getTimeStamp() {
        return timeStamp;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public void setTimeStamp(Double timeStamp) {
        this.timeStamp = timeStamp;
    }
    public static com.example.demo.data.model.Aim toModel(com.example.demo.data.entities.Aim data) {
        com.example.demo.data.model.Aim aim=new com.example.demo.data.model.Aim();
        aim.setDbId(data.getId());;
        aim.setCreateTime(MyConverter.doubleToTimestamp(data.getCreateTime()));
        aim.setRemark(data.getRemark());
        aim.setLabelId(data.getLabelId());
        aim.setTitle(data.getTitle());
        aim.setRestTime(MyConverter.doubleToTimestamp(data.getRestTime()));
        aim.setTimeStamp (MyConverter.doubleToTimestamp(data.getTimeStamp()));
        aim.setStatus(StringUtil.SYC);
        return aim;
    }
    public static List<com.example.demo.data.model.Aim> unpack(List<Bean<Aim>> data){
        List<com.example.demo.data.model.Aim> list=new ArrayList<>();
        for(Bean<Aim> t:data){
            com.example.demo.data.model.Aim a = Aim.toModel(t.getData());
            a.setId(t.getOfflineId());
            list.add(a);
        }
        return list;
    }
}
