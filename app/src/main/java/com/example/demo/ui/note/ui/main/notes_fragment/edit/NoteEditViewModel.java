package com.example.demo.ui.note.ui.main.notes_fragment.edit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.data.model.Notes;
import com.example.demo.data.model.Notes_in_label;
import com.example.demo.data.model.Notes_label;
import com.example.demo.data.repository.NotesRepository;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.sql.Timestamp;
import java.util.List;

public class NoteEditViewModel extends ViewModel {
    private  NotesRepository repository;
    private LiveData<Notes> note;
//    public NoteEditViewModel(@NonNull Application application) {
//        super();
//        repository=new NotesRepository(application);
//    }
    NoteEditViewModel(NotesRepository notesRepository) {
        this.repository = notesRepository;
    }
    public Long insertNoteMain(Notes notes){
        Timestamp time = Tool.createNewTimeStamp();
        notes.setLastTime(time);
        notes.setStatus(StringUtil.LOCAL_INSERT);
        return repository.insertNotesMain(notes);
    }

    public LiveData<Notes> getNote() {
        if(note==null){
            note=new MutableLiveData<Notes>();
        }
        return note;
    }

    public LiveData<List<Notes_label>> getAllNoteLabel(){
        return repository.getNoteLabelList();
    }
    public List<Notes_label> getAllNoteLabelStatic(){
        return (List<Notes_label>) repository.getNoteLabelListStatic();
    }
    public LiveData<Notes> getNoteById(int id) {
        return repository.findNotesById(id);
    }

    public NotesRepository getRepository() {
        return repository;
    }




    public void updateNoteLabelCountById(Integer notes_label_id ,int increment) {
        repository.updateNoteLabelCountById(notes_label_id,increment);
    }
    //repository中会更新label count数
    public void insertRelationAsync(Notes_in_label ... relations) {
        for(Notes_in_label r:relations){
            r.setStatus(StringUtil.LOCAL_INSERT);
            repository.insertRelationAsync(r);
        }
    }

    public List<Notes_label> getNotesLabelByNotesStatic(Notes notes) {
        return repository.getNotesLabelByNotesStatic(notes);
    }

    public void updateNotes(Notes notes) {
        repository.updateNotes( notes);
    }

    public void statusDeleteRelationByNotesIdLabelIdAsync(Integer notesId, Integer labelId) {
        Notes_in_label relation=repository.getRelationByScheduleIdLabelIdStatic(notesId,labelId);
        repository.statusDeleteRelationAsync(relation);
    }

    public void statusDeleteNotesAsync(Notes notes) {
        repository.statusDeleteNotesAsync( notes);
    }
}
