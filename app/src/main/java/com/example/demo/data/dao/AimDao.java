package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Aim;
import com.example.demo.data.model.Task;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface AimDao {
    @Insert
    Long[] insertAim(Aim... Aims);

    @Update
    void updateAim(Aim... Aims);

    @Delete
    void deleteAim(Aim... Aims);

    @Query("SELECT * FROM AIM WHERE status!="+ StringUtil.LOCAL_DELETE)
    LiveData<List<Aim>> getAllAim();

    @Query("SELECT * FROM TASK WHERE aimId =:aim_id and STATUS !="+StringUtil.LOCAL_DELETE)
    LiveData<List<Task>> getTasksByAimId(int aim_id);

    @Query("SELECT MAX(timeStamp) FROM AIM ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT* FROM AIM WHERE status!="+ StringUtil.SYC)
    List<Aim> getNotSYCTarget();

    @Query("SELECT * FROM AIM WHERE id=:id")
    Aim getAimByIdStatic(Integer id);

    @Query("DELETE FROM AIM WHERE id=:id")
    void deleteAimById(Integer id);

    @Query("SELECT * FROM AIM WHERE dbId=:aimId")
    Aim getAimByDbIdStatic(Integer aimId);

    @Query("DELETE FROM AIM ")
    void clearAimStatic();
}
