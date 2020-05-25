package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Milepost;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface MilepostDao {
    @Insert
    void insertMilepost(Milepost... Mileposts);

    @Update
    void updateMilepost(Milepost... Mileposts);

    @Delete
    void deleteMilepost(Milepost... Mileposts);

    @Query("SELECT * FROM MILEPOST WHERE status!="+StringUtil.LOCAL_DELETE)
    LiveData<List<Milepost>> getAllMilepost();

    @Query("SELECT MAX(timeStamp) FROM MILEPOST ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM MILEPOST WHERE status!="+ StringUtil.SYC)
    List<Milepost> getNotSYCMilepost();

    @Query("SELECT * FROM MILEPOST WHERE id=:id")
    Milepost getMilepostByIdStatic(Integer id);

    @Query("DELETE FROM MILEPOST WHERE id=:id")
    void deleteMilepostById(Integer id);

    @Query("DELETE FROM MILEPOST ")
    void clearMilepostStatic();

    @Query("SELECT * FROM MILEPOST WHERE (dieTime >:timestamp AND status!="+StringUtil.LOCAL_DELETE+
            ")ORDER BY dieTime ASC")
    LiveData<List<Milepost>> getLiveMilepostList(Timestamp timestamp);

    @Query("SELECT * FROM MILEPOST WHERE (dieTime >:timestamp AND status!="+StringUtil.LOCAL_DELETE+")ORDER BY dieTime ASC")
    Milepost getLiveMilepostLately(Timestamp timestamp);
}
