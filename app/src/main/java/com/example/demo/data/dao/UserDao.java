package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.User;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface UserDao {
    @Insert
    Long[] insertUser(User... Users);

    @Update
    void updateUser(User... Users);

    @Delete
    void deleteUser(User... Users);

    @Query("SELECT * FROM User")
    List<User> getUser();

    @Query("SELECT * FROM User")
    LiveData<User> getUserInfo();

    @Query("SELECT MAX(timeStamp) FROM USER  ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("DELETE FROM User ")
    void clearCacheStatic();

    @Query("SELECT * FROM User WHERE dbId=:dbId")
    User getUserBuDbId(Integer dbId);
}
