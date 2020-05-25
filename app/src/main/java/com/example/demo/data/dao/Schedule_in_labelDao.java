package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Schedule_in_label;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface Schedule_in_labelDao {
    @Insert
    void insertSchedule_in_label(Schedule_in_label... relations);

    @Update
    void updateSchedule_in_label(Schedule_in_label... relations);

    @Delete
    void deleteSchedule_in_label(Schedule_in_label... relations);

    @Query("SELECT * FROM SCHEDULE_IN_LABEL WHERE status!="+ StringUtil.LOCAL_DELETE)
    LiveData<List<Schedule_in_label>> getAllRelation();

    @Query("SELECT MAX(timeStamp) FROM SCHEDULE_IN_LABEL  ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM SCHEDULE_IN_LABEL WHERE status!="+ StringUtil.SYC)
    List<Schedule_in_label> getNotSYCSchedulesRelation();

    @Query("SELECT * FROM SCHEDULE_IN_LABEL WHERE id=:id")
    Schedule_in_label getScheduleRelationByIdStatic(Integer id);

    @Query("DELETE FROM SCHEDULE_IN_LABEL WHERE id=:id")
    void deleteScheduleRelationById(Integer id);

    @Query("DELETE FROM SCHEDULE_IN_LABEL ")
    void clearScheduleRelationStatic();

    @Query("SELECT * FROM SCHEDULE_IN_LABEL WHERE scheduleId=:scheduleId AND scheduleLabelId=:labelId")
    Schedule_in_label getScheduleRelationByScheduleIdLabelIdStatic(Integer scheduleId, Integer labelId);

    @Query("DELETE FROM SCHEDULE_IN_LABEL WHERE scheduleLabelId=:id")
    void deleteSchedule_in_labelByLabelId(Integer id);

    @Query("SELECT * FROM SCHEDULE_IN_LABEL WHERE scheduleLabelId=:id")
    List<Schedule_in_label> getRelationListByScheduleLabelStatic(Integer id);
}
