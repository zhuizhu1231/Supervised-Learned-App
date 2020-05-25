package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.entities.Data;
import com.example.demo.data.model.TaskLog;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface TaskLogDao {
    @Insert
    Long[] insertTaskLog(TaskLog... tasks);

    @Update
    void updateTaskLog(TaskLog... tasks);

    @Delete
    void deleteTaskLog(TaskLog... tasks);


    @Query("DELETE FROM TASKLOG WHERE taskId  =:task_id")
    void deleteTaskLogByTaskId(int task_id);


    @Query("SELECT * FROM TASKLOG WHERE taskId =:task_id AND status!="+ StringUtil.LOCAL_DELETE)
    List<TaskLog> getTaskLogsByTaskIdStatic(int task_id);

    @Query("SELECT MAX(timeStamp) FROM TASKLOG  ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM TASKLOG  WHERE status!="+ StringUtil.SYC)
    List<TaskLog> getNotSYCSTaskLog();

    @Query("SELECT * FROM TASKLOG  WHERE id=:id")
    TaskLog getTaskLogByIdStatic(Integer id);

    @Query("DELETE FROM TASKLOG")
    void clearTaskLogStatic();

//    @Query("SELECT SUM(workCount) FROM TASKLOG WHERE   status!="+ StringUtil.LOCAL_DELETE)
//    LiveData<Integer> sumAllWorkCount();
//
//    @Query("SELECT SUM(workCount*workTime) FROM TASKLOG WHERE   status!="+ StringUtil.LOCAL_DELETE)
//    LiveData<Timestamp> sumAllWorkTime();
//
//    @Query("SELECT * FROM TASKLOG WHERE workCount!=0 AND status!="+ StringUtil.LOCAL_DELETE)
//    LiveData<List<TaskLog>> getTaskLogListByWorkCountNotZero();
//
//    @Query("SELECT SUM(workCount*workTime) FROM TASKLOG WHERE taskId=:id AND   status!="+ StringUtil.LOCAL_DELETE)
//    Timestamp sumAllWorkTimeByTaskStatic(Integer id);
//
//    @Query("SELECT * FROM TASKLOG WHERE workCount!=0 AND taskId=:id AND  status!="+ StringUtil.LOCAL_DELETE)
//    List<TaskLog> getTaskLogListByTaskWorkCountNotZero(Integer id);

    @Query("SELECT * FROM TASKLOG WHERE  taskId=:id AND  status!="+ StringUtil.LOCAL_DELETE)
    List<TaskLog> getTaskLogListByTaskStatic(Integer id);

    @Query("SELECT * FROM TASKLOG  WHERE dbId=:dbId")
    TaskLog getTaskLogByDbIdStatic(Integer dbId);

    @Query("SELECT Count(*) FROM TASKLOG WHERE taskId=:id AND  timeStamp>=:returnTodayBeginTimestamp AND timeStamp<=:returnTodayEndTimestamp")
    Integer getTodayTaskCountByTaskId(Integer id, Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp);

    @Query("SELECT Count(*) FROM TASKLOG WHERE  timeStamp>=:returnTodayBeginTimestamp AND timeStamp<=:returnTodayEndTimestamp")
    LiveData<Integer> getDayWorkCount(Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp);
//    @Query("SELECT SUM(workCount)  FROM TASKLOG WHERE status!="+ StringUtil.DELETE_STATUS + "  AND belongTime>:returnTodayBeginTimestamp and belongTime<:returnTodayEndTimestamp" +//AND category=:category
//            "" //AND  timeStamp>= :returnTodayBeginTimestamp AND timeStamp<=:returnTodayEndTimestamp
//            )
//    LiveData<Integer> getWorkCountByTimestampBetween(Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp);

    //@Query("SELECT workTime FROM TASK WHERE id=")
    @Query("SELECT SUM(1*workTime) FROM TASKLOG,TASK WHERE  TASKLOG.timeStamp>=:returnTodayBeginTimestamp AND TASKLOG.timeStamp<=:returnTodayEndTimestamp AND taskId=TASK.id")
    LiveData<Timestamp> getDayWorkCountTime(Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp);

    @Query("SELECT * FROM TASKLOG  WHERE timeStamp>=:returnMonthBeginTimestamp AND timeStamp<=:dateToTimeStamp ORDER BY timeStamp ASC")
    LiveData<List<TaskLog>> getTaskLogTimeBetween(Timestamp returnMonthBeginTimestamp, Timestamp dateToTimeStamp);

    @Query("SELECT taskId,COUNT(*)*workTime AS time FROM TASKLOG,task  WHERE taskId=task.id AND tasklog.timeStamp>=:returnMonthBeginTimestamp AND  tasklog.timeStamp<=:dateToTimeStamp GROUP BY taskId ")
    LiveData<List<Data>> getDayTaskLogTimeBetween(Timestamp returnMonthBeginTimestamp, Timestamp dateToTimeStamp);

//    @Query("select COUNT(*)*workTime,taskId FROM tasklog,task WHERE taskId=task.id AND tasklog.timeStamp>=:returnTodayBeginTimestamp AND  tasklog.timeStamp<=:returnTodayEndTimestamp")
//    Timestamp getDayTaskLogTimeTimeBetween(Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp);
    @Query("select workTime*COUNT(*),taskId FROM tasklog,task WHERE taskId=task.id AND tasklog.timeStamp>=:returnTodayBeginTimestamp AND  tasklog.timeStamp<=:returnTodayEndTimestamp")
    long getDayTaskLogTimeTimeBetween(Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp);
}
