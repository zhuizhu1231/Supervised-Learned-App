package com.example.demo.converter;

import androidx.lifecycle.LiveData;
import androidx.room.TypeConverter;

import com.example.demo.data.model.Task;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class MyConverter {
    @TypeConverter
    public static Date longToDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Timestamp dateToTimeStamp(Date date) {
        return date == null ? null : new Timestamp(date.getTime());
    }

    @TypeConverter
    public static Date timeStampToDate(Timestamp value) {
        return value == null ? null : new Date(value.getTime());
    }

    @TypeConverter
    public static Long dateToLong(Date date) {
        return date == null ? null : date.getTime();
    }



    @TypeConverter
    public static Timestamp longToTimestamp(Long value) {
        return value == null ? null : new Timestamp(value);
    }

    @TypeConverter
    public static Long timestampToLong(Timestamp date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Double timestampToDouble(Timestamp date) {
       //Double.parseDouble(String.valueOf(date.getTime()))
        return date == null ? null : (double)date.getTime();
    }

    @TypeConverter
    public static Timestamp doubleToTimestamp(Double date) {
        return date == null ? null :  new Timestamp(date.longValue());
    }





    @TypeConverter
    public static int longToInt(long date) {
        return  Integer.valueOf(String.valueOf(date));
    }




//做一个客户端与服务端数据相互转换的转换器
//    @TypeConverter
//    public static LiveData<List<com.example.demo.data.entities.Task>> modelTaskToEntityTask(LiveData<List<Task>> tasks) {
//        List<Task> values = tasks.getValue();
//
//        LiveData<List<com.example.demo.data.entities.Task>> converter=
//
//
//        return null;
//    }
}
