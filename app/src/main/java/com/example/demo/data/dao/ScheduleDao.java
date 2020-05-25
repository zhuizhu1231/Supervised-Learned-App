package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Schedule;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface ScheduleDao {


    @Insert
    long insertSchedule(Schedule Schedules);

    @Insert
    long[] insertScheduleList(Schedule ... Schedules);

    @Update
    void updateSchedule(Schedule... Schedules);

    @Delete
    void deleteSchedule(Schedule... Schedules);

    @Query("SELECT * FROM SCHEDULE WHERE status!="+ StringUtil.LOCAL_DELETE)
    LiveData<List<Schedule>> getAllSchedule();




    //net
    @Query("SELECT MAX(timeStamp) FROM SCHEDULE")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM SCHEDULE WHERE status!="+ StringUtil.SYC)
    List<Schedule> getNotSycSchedules();

    @Query("SELECT * FROM SCHEDULE WHERE id=:id")
    Schedule getScheduleByIdStatic(Integer id);

    @Query("DELETE FROM SCHEDULE WHERE id=:id")
    void deleteScheduleById(Integer id);
    @Query("SELECT * FROM SCHEDULE WHERE dbId=:scheduleId")
    Schedule getScheduleByDbIdStatic(Integer scheduleId);

    @Query("DELETE FROM SCHEDULE ")
    void clearScheduleStatic();

    @Query("SELECT * FROM SCHEDULE WHERE status!="+ StringUtil.LOCAL_DELETE + " AND category=:category AND belongTime>:returnTodayBeginTimestamp and belongTime<:returnTodayEndTimestamp" +//AND category=:category
            "" +//AND  timeStamp>= :returnTodayBeginTimestamp AND timeStamp<=:returnTodayEndTimestamp
            " ORDER BY belongTime DESC")
    LiveData<List<Schedule>> getScheduleListByCategoryTimestampBetween(int category,Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp);

    @Query("SELECT * FROM SCHEDULE WHERE status!="+ StringUtil.LOCAL_DELETE + " AND belongTime>:returnTodayBeginTimestamp and belongTime<:returnTodayEndTimestamp" +//AND category=:category
            "" +//AND  timeStamp>= :returnTodayBeginTimestamp AND timeStamp<=:returnTodayEndTimestamp
            " ORDER BY belongTime DESC")
    LiveData<List<Schedule>> getScheduleListByTimestampBetween(Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp);

    @Query("SELECT * FROM SCHEDULE WHERE id IN" +
            "(SELECT scheduleId FROM SCHEDULE_IN_LABEL WHERE scheduleLabelId=:labelId AND status!="+StringUtil.LOCAL_DELETE +")" +
            "ORDER BY belongTime DESC")
    LiveData<List<Schedule>> getScheduleListByTag(int labelId);

    @Query("SELECT * FROM SCHEDULE WHERE id IN" +
            "(SELECT scheduleId FROM SCHEDULE_IN_LABEL WHERE scheduleLabelId=:labelId AND status!="+StringUtil.LOCAL_DELETE +")" +
            "ORDER BY belongTime DESC")
    List<Schedule> getScheduleListByTagStatic(int labelId);
//"AND property="+StringUtil.CLOCK_SET+
    @Query("SELECT * FROM SCHEDULE WHERE status!="+ StringUtil.LOCAL_DELETE + " AND belongTime>:dateToTimeStamp and belongTime<:returnTodayEndTimestamp" +//AND category=:category
            "" +//AND  timeStamp>= :returnTodayBeginTimestamp AND timeStamp<=:returnTodayEndTimestamp
            " ORDER BY belongTime DESC")
    List<Schedule> getDayAlarmScheduleListByTimestampAfterStatic(Timestamp dateToTimeStamp, Timestamp returnTodayEndTimestamp);

    @Query("SELECT * FROM SCHEDULE WHERE status!="+ StringUtil.LOCAL_DELETE + " AND isDone=:done AND belongTime>:returnTodayBeginTimestamp and belongTime<:returnTodayEndTimestamp" +//AND category=:category
            "" +//AND  timeStamp>= :returnTodayBeginTimestamp AND timeStamp<=:returnTodayEndTimestamp
            " ORDER BY belongTime DESC")
    LiveData<List<Schedule>> getScheduleListByTimestampBetweenScheduleDone(Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp, int done);

    @Query("SELECT * FROM SCHEDULE WHERE status!="+ StringUtil.LOCAL_DELETE + " AND belongTime>:returnTodayBeginTimestamp and belongTime<:returnTodayEndTimestamp" +//AND category=:category
            "" +//AND  timeStamp>= :returnTodayBeginTimestamp AND timeStamp<=:returnTodayEndTimestamp
            " ORDER BY belongTime DESC")
    List<Schedule> getScheduleListByTimestampBetweenStatic(Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp);

    @Query("SELECT * FROM SCHEDULE WHERE status!="+ StringUtil.LOCAL_DELETE + " AND isDone=:done AND belongTime>:returnTodayBeginTimestamp and belongTime<:returnTodayEndTimestamp" +//AND category=:category
            "" +//AND  timeStamp>= :returnTodayBeginTimestamp AND timeStamp<=:returnTodayEndTimestamp
            " ORDER BY belongTime DESC")
    List<Schedule> getScheduleListByTimestampBetweenScheduleDoneStatic(Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp, Integer done);

    @Query("SELECT * FROM SCHEDULE WHERE title like :titleLike AND status!="+ StringUtil.LOCAL_DELETE)
    LiveData<List<Schedule>> getScheduleByTitleLike(String titleLike);
}
