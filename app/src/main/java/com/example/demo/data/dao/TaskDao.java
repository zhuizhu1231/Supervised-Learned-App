package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Task;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    Long[] insertTask(Task... tasks);

    @Update
    void updateTask(Task... tasks);

    @Delete
    void deleteTask(Task... tasks);

    @Query("DELETE FROM TASK WHERE aimId IS NULL")
    void clearAllAloneTask();

    @Query("DELETE FROM TASK WHERE aimId  =:aim_id")
    void deleteTaskByAimId(int aim_id);

    @Query("SELECT * FROM TASK WHERE aimId IS NULL  AND status!="+ StringUtil.LOCAL_DELETE +" ORDER BY ID DESC")
    LiveData<List<Task>> getAllAloneTask();


    @Query("SELECT * FROM TASK WHERE aimId =:aim_id AND status!="+ StringUtil.LOCAL_DELETE)
    List<Task> getTasksByAimIdStatic(int aim_id);

    @Query("SELECT MAX(timeStamp) FROM TASK  ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM TASK  WHERE status!="+ StringUtil.SYC)
    List<Task> getNotSYCSTask();

    @Query("SELECT * FROM TASK  WHERE id=:id")
    Task getTaskByIdStatic(Integer id);

    @Query("DELETE FROM TASK  WHERE id=:id")
    void deleteTaskById(Integer id);

    @Query("DELETE FROM TASK")
    void clearTaskStatic();

    @Query("SELECT SUM(workCount) FROM TASK WHERE   status!="+ StringUtil.LOCAL_DELETE)
    LiveData<Integer> sumAllWorkCount();

    @Query("SELECT SUM(workCount*workTime) FROM TASK WHERE   status!="+ StringUtil.LOCAL_DELETE)
    LiveData<Timestamp> sumAllWorkTime();

    @Query("SELECT * FROM TASK WHERE workCount!=0 AND status!="+ StringUtil.LOCAL_DELETE)
    LiveData<List<Task>> getTaskListByWorkCountNotZero();

    @Query("SELECT SUM(workCount*workTime) FROM TASK WHERE aimId=:id AND   status!="+ StringUtil.LOCAL_DELETE)
    Timestamp sumAllWorkTimeByAimStatic(Integer id);

    @Query("SELECT * FROM TASK WHERE workCount!=0 AND aimId=:id AND  status!="+ StringUtil.LOCAL_DELETE)
    List<Task> getTaskListByAimWorkCountNotZero(Integer id);

    @Query("SELECT * FROM TASK WHERE  aimId=:id AND  status!="+ StringUtil.LOCAL_DELETE)
    List<Task> getTaskListByAimStatic(Integer id);

    @Query("SELECT * FROM TASK  WHERE dbId=:dbId")
    Task getTaskByDbIdStatic(Integer dbId);
    @Query("SELECT dbId FROM TASK  WHERE id=:taskId")
    Integer getDbIdById(Integer taskId);
//    @Query("SELECT SUM(workCount)  FROM TASK WHERE status!="+ StringUtil.DELETE_STATUS + "  AND belongTime>:returnTodayBeginTimestamp and belongTime<:returnTodayEndTimestamp" +//AND category=:category
//            "" //AND  timeStamp>= :returnTodayBeginTimestamp AND timeStamp<=:returnTodayEndTimestamp
//            )
//    LiveData<Integer> getWorkCountByTimestampBetween(Timestamp returnTodayBeginTimestamp, Timestamp returnTodayEndTimestamp);
}
