package com.example.demo.net.json;

public class Bean<T> {
    private Integer offlineId;
    private T data;

    public Integer getOfflineId() {
        return offlineId;
    }

    public void setOfflineId(Integer offlineId) {
        this.offlineId = offlineId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
