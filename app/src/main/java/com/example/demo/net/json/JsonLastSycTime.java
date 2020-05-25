package com.example.demo.net.json;

import com.example.demo.converter.MyConverter;

import java.sql.Timestamp;

public class JsonLastSycTime {
    private Boolean isSuccess;
    private Integer code;
    private String sessionId;
    private Double lastSycTime;

    public JsonLastSycTime() {
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Double getLastSycTime() {
        return lastSycTime;
    }

    public void setLastSycTime(Double lastSycTime) {
        this.lastSycTime = lastSycTime;
    }
    public Timestamp returnToTimestamp(){
        return MyConverter.doubleToTimestamp(lastSycTime);
    }
}
