package com.example.demo.ui.note;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.demo.data.model.Notes_label;
import com.example.demo.data.repository.NotesRepository;
import com.example.demo.util.StringUtil;

public class NoteViewModel extends ViewModel {
    private NotesRepository repository;

    public NoteViewModel(@NonNull Application application) {
        super();
        repository=new NotesRepository(application);
    }

    public NoteViewModel() {
    }

    public NoteViewModel(NotesRepository NoteLabelRepository) {
        this.repository = NoteLabelRepository;
    }

    public void insertNoteLabelAsync(Notes_label noteLabel){
        noteLabel.setNotesCount(0);
        noteLabel.setStatus(StringUtil.LOCAL_INSERT);
        repository.insertNoteLabelAsync(noteLabel);
    }

    public NotesRepository getRepository() {
        return repository;
    }
}
