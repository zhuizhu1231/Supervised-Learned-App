package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Study_room_message;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface Study_room_messageDao {
    @Insert
    void insertStudy_room_message(Study_room_message... Study_room_messages);

    @Update
    void updateStudy_room_message(Study_room_message... Study_room_messages);

    @Delete
    void deleteStudy_room_message(Study_room_message... Study_room_messages);

    @Query("SELECT * FROM STUDY_ROOM_MESSAGE")
    LiveData<List<Study_room_message>> getAllStudy_room_message();

    @Query("SELECT MAX(timeStamp) FROM STUDY_ROOM_MESSAGE  ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("DELETE FROM STUDY_ROOM_MESSAGE ")
    void clearCacheStatic();

    @Query("SELECT * FROM STUDY_ROOM_MESSAGE WHERE studyRoomId=:dbId")
    LiveData<List<Study_room_message>> getMessageListByRoomDbId(Integer dbId);
}
