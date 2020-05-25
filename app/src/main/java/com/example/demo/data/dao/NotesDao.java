package com.example.demo.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.demo.data.model.Notes;
import com.example.demo.util.StringUtil;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface NotesDao {
    @Insert
    void insertNote(Notes... Notes);

    @Insert
    Long insertNote(Notes Notes);

    @Update
    void updateNote(Notes... Notes);

    @Delete
    void deleteNote(Notes... Notes);

    @Query("SELECT * FROM NOTES  WHERE status!="+ StringUtil.LOCAL_DELETE +" ORDER BY lastTime DESC")
    LiveData<List<Notes>> getAllNotes();

    @Query("SELECT * FROM NOTES where id=:id")
    LiveData<Notes> getNoteById(Integer[] id);

    @Query("SELECT * FROM NOTES WHERE id IN " +
            "(SELECT notesId FROM NOTES_IN_LABEL WHERE notesLabelId =:notes_label_id AND status!="+ StringUtil.LOCAL_DELETE +")" +
            " ORDER BY lastTime DESC")
    LiveData<List<Notes>> getNotesByNotesLabel(int notes_label_id);

    @Query("SELECT MAX(timeStamp) FROM NOTES ")
    Timestamp getMaxSycTimeStamp() ;

    @Query("SELECT * FROM NOTES where id=:notesId")
    Notes getNotesByIdStatic(Integer notesId);

    @Query("SELECT * FROM NOTES where dbId=:notesId")
    Notes getNotesByDbIdStatic(Integer notesId);

    @Query("SELECT * FROM NOTES WHERE status!="+ StringUtil.SYC)
    List<Notes> getNotSYCNotes();

    @Query("DELETE FROM NOTES where id=:id")
    void deleteNotesById(Integer id);

    @Query("DELETE FROM NOTES ")
    void clearNotesStatic();
}
