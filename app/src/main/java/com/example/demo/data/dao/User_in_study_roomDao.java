package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.User_in_study_room;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface User_in_study_roomDao {
    @Insert
    void insertUser_in_study_room(User_in_study_room... User_in_study_rooms);

    @Update
    void updateUser_in_study_room(User_in_study_room... User_in_study_rooms);

    @Delete
    void deleteUser_in_study_room(User_in_study_room... User_in_study_rooms);

    @Query("SELECT * FROM USER_IN_STUDY_ROOM")
    LiveData<List<User_in_study_room>> getAllUser_in_study_room();

    @Query("SELECT MAX(timeStamp) FROM USER_IN_STUDY_ROOM  ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM USER_IN_STUDY_ROOM WHERE studyRoomId=:roomId AND userId=:sendUserId")
    User_in_study_room getUserByRoomIdUserId(Integer roomId, Integer sendUserId);

    @Query("DELETE FROM USER_IN_STUDY_ROOM ")
    void clearCacheStatic();

    @Query("SELECT * FROM USER_IN_STUDY_ROOM WHERE studyRoomId=:roomDbId ")
    LiveData<List<User_in_study_room>> getMemberByRoomId(Integer roomDbId);

    @Query("DELETE FROM USER_IN_STUDY_ROOM WHERE studyRoomId=:roomId")
    void clearCacheByRoomId(Integer roomId);

    @Query("SELECT * FROM USER_IN_STUDY_ROOM WHERE dbId=:dbId")
    User_in_study_room getStudyRoomMemberByDbIdStatic(Integer dbId);
}
