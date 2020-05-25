package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.TaskLabel;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface TaskLabelDao {
    @Insert
    Long[] insertNote(TaskLabel... TaskLabel);

    @Insert
    Long insertNote(TaskLabel TaskLabel);

    @Update
    void updateNote(TaskLabel... TaskLabel);

    @Delete
    void deleteNoteLabel(TaskLabel... TaskLabel);

    @Query("SELECT * FROM TASKLABEL WHERE STATUS!="+StringUtil.LOCAL_DELETE)
    LiveData<List<TaskLabel>> getAllNotesLabel();

    @Query("SELECT * FROM TASKLABEL WHERE STATUS!="+StringUtil.LOCAL_DELETE)
    List<TaskLabel> getAllNotesLabelStatic();

    @Query("SELECT * FROM TASKLABEL WHERE id=:id")
    LiveData<TaskLabel> getNoteLabelById(int id);



    @Query("SELECT * FROM TASKLABEL WHERE id IN" +
            "(SELECT notesLabelId FROM NOTES_IN_LABEL WHERE notesId=:id AND STATUS!="+StringUtil.LOCAL_DELETE +")")
    List<TaskLabel> getAllNotesLabelByNoteIdStatic(Integer id);

    @Query("SELECT * FROM TASKLABEL WHERE id=:id")
    TaskLabel getNoteLabelByIdStatic(Integer id);


    @Query("SELECT MAX(timeStamp) FROM TASKLABEL")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM TASKLABEL where dbId=:notesLabelId")
    TaskLabel getNoteLabelByDbIdStatic(Integer notesLabelId);

    @Query("SELECT * FROM TASKLABEL WHERE status!="+ StringUtil.SYC)
    List<TaskLabel> getNotSYCNotesLabel();

    @Query("SELECT * FROM TASKLABEL where id=:id")
    TaskLabel getNotesLabelByIdStatic(Integer id);

    @Query("DELETE FROM TASKLABEL where id=:id")
    void deleteNotesLabelById(Integer id);

    @Query("DELETE FROM TASKLABEL ")
    void clearNotesLabelStatic();


}
