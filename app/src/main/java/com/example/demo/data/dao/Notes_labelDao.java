package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Notes_label;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface Notes_labelDao {
    @Insert
    Long[] insertNote(Notes_label... Notes_label);

    @Insert
    Long insertNote(Notes_label Notes_label);

    @Update
    void updateNote(Notes_label... Notes_label);

    @Delete
    void deleteNoteLabel(Notes_label... Notes_label);

    @Query("SELECT * FROM NOTES_LABEL WHERE STATUS!="+StringUtil.LOCAL_DELETE)
    LiveData<List<Notes_label>> getAllNotesLabel();

    @Query("SELECT * FROM NOTES_LABEL WHERE STATUS!="+StringUtil.LOCAL_DELETE)
    List<Notes_label> getAllNotesLabelStatic();

    @Query("SELECT * FROM NOTES_LABEL WHERE id=:id")
    LiveData<Notes_label> getNoteLabelById(int id);



    @Query("SELECT * FROM NOTES_LABEL WHERE id IN" +
            "(SELECT notesLabelId FROM NOTES_IN_LABEL WHERE notesId=:id AND STATUS!="+StringUtil.LOCAL_DELETE +")")
    List<Notes_label> getAllNotesLabelByNoteIdStatic(Integer id);

    @Query("SELECT * FROM NOTES_LABEL WHERE id=:id")
    Notes_label getNoteLabelByIdStatic(Integer id);


    @Query("SELECT MAX(timeStamp) FROM NOTES_LABEL")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM NOTES_LABEL where dbId=:notesLabelId")
    Notes_label getNoteLabelByDbIdStatic(Integer notesLabelId);

    @Query("SELECT * FROM NOTES_LABEL WHERE status!="+ StringUtil.SYC)
    List<Notes_label> getNotSYCNotesLabel();

    @Query("SELECT * FROM NOTES_LABEL where id=:id")
    Notes_label getNotesLabelByIdStatic(Integer id);

    @Query("DELETE FROM NOTES_LABEL where id=:id")
    void deleteNotesLabelById(Integer id);

    @Query("DELETE FROM NOTES_LABEL ")
    void clearNotesLabelStatic();


}
