package com.example.demo.net.json;

import java.util.List;

public class JsonResult<T> {
    private Boolean isSuccess;
    private Integer code;
    private String sessionId;
    private String message;

    private List<Bean<T>> data;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Bean<T>> getData() {
        return data;
    }

    public void setData(List<Bean<T>> data) {
        this.data = data;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
