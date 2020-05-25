package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Study_room;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface Study_roomDao {
    @Insert
    void insertStudy_room(Study_room... Study_rooms);

    @Update
    void updateStudy_room(Study_room... Study_rooms);

    @Delete
    void deleteStudy_room(Study_room... Study_rooms);

    @Query("SELECT * FROM STUDY_ROOM WHERE status!="+ StringUtil.LOCAL_DELETE)
    LiveData<List<Study_room>> getAllStudy_room();

    @Query("SELECT MAX(timeStamp) FROM STUDY_ROOM  ")
    Timestamp getMaxSycTimeStamp() ;


    @Query("DELETE FROM  STUDY_ROOM ")
    void clearCacheStatic();



    @Query("SELECT * FROM STUDY_ROOM WHERE dbId=:dbId")
    Study_room getStudyRoomByDbIdStatic(Integer dbId);

    @Query("SELECT * FROM STUDY_ROOM WHERE name LIKE :titleLike  OR dbId =:id AND status!="+ StringUtil.LOCAL_DELETE+" Order by name ASC")
    LiveData<List<Study_room>> getRoomListByRemarkLikeOrDbIdLike(String titleLike, Integer id);
}
