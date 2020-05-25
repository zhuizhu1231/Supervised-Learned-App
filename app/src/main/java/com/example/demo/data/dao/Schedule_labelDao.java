package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Schedule_label;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface Schedule_labelDao {
    @Insert
    void insertSchedule_label(Schedule_label... Schedule_labels);

    @Update
    void updateSchedule_label(Schedule_label... Schedule_labels);

    @Delete
    void deleteSchedule_label(Schedule_label... Schedule_labels);

    @Query("SELECT * FROM SCHEDULE_LABEL WHERE STATUS!="+StringUtil.LOCAL_DELETE)
    LiveData<List<Schedule_label>> getAllScheduleLabel();

    @Query("SELECT * FROM SCHEDULE_LABEL WHERE STATUS!="+StringUtil.LOCAL_DELETE)
    List<Schedule_label> getAllScheduleLabelStatic();

    @Query("SELECT MAX(timeStamp) FROM SCHEDULE_LABEL  WHERE status="+ StringUtil.SYC)
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM SCHEDULE_LABEL WHERE status!="+ StringUtil.SYC)
    List<Schedule_label> getNotSYCSchedulesLabel();

    @Query("SELECT * FROM SCHEDULE_LABEL WHERE id=:id")
    Schedule_label getLabelByIdStatic(Integer id);

    @Query("DELETE FROM SCHEDULE_LABEL WHERE id=:id")
    void deleteScheduleLabelById(Integer id);
    @Query("SELECT * FROM SCHEDULE_LABEL WHERE dbId=:scheduleLabelId")
    Schedule_label getScheduleLabelByDbIdStatic(Integer scheduleLabelId);

    @Query("DELETE FROM SCHEDULE_LABEL ")
    void clearScheduleLabelStatic();

    @Query("SELECT * FROM SCHEDULE_LABEL WHERE id=:id")
    Schedule_label getScheduleLabelByIdStatic(Integer id);
    @Query("SELECT * FROM SCHEDULE_LABEL WHERE id IN" +
            "(SELECT scheduleLabelId FROM SCHEDULE_IN_LABEL WHERE scheduleId=:scheduleId AND STATUS!="+StringUtil.LOCAL_DELETE +")")
    List<Schedule_label> getScheduleLabelByScheduleIdStatic(Integer scheduleId);

    @Query("SELECT * FROM SCHEDULE_LABEL WHERE title like :titleLike AND STATUS!="+StringUtil.LOCAL_DELETE)
    LiveData<List<Schedule_label>> getScheduleLabelByTitleLike(String titleLike);
//    @Query("SELECT MAX(timeStamp) FROM SCHEDULE_LABEL WHERE status="+ StringUtil.SYC)
//    Timestamp getMaxSycTimeStamp() ;
}
