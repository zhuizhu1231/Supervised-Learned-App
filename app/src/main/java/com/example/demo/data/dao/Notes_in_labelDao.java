package com.example.demo.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Notes_in_label;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface Notes_in_labelDao {
    @Insert
    void insertRelation(Notes_in_label ... relation);


    @Update
    void updateRelation(Notes_in_label ... relation);

    @Delete
    void deleteRelations(Notes_in_label ... relations);


    @Query("SELECT MAX(timeStamp) FROM NOTES_IN_LABEL ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM NOTES_IN_LABEL WHERE status !="+ StringUtil.SYC)
    List<Notes_in_label> getNotSYCNotesRelation();

    @Query("SELECT * FROM NOTES_IN_LABEL WHERE id=:id")
    Notes_in_label getNotesRelationByIdStatic(Integer id);

    @Query("DELETE FROM NOTES_IN_LABEL WHERE id=:id")
    void deleteNotesRelationById(Integer id);

    @Query("DELETE FROM NOTES_IN_LABEL")
    void clearNotesRelationStatic();

    @Query("SELECT * FROM NOTES_IN_LABEL WHERE notesId=:notesId AND notesLabelId=:labelId")
    Notes_in_label getRelationByScheduleIdLabelIdStatic(Integer notesId, Integer labelId);


    @Query("SELECT * FROM NOTES_IN_LABEL WHERE notesLabelId=:id")
    List<Notes_in_label> getRelationListByNotesLabelStatic(Integer id);




    @Query("DELETE FROM NOTES_IN_LABEL WHERE notesLabelId=:noteLabelId")
    void deleteRelationByNoteLabelId(int noteLabelId);
}
