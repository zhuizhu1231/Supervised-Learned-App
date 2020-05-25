package com.example.demo.ui.note.ui.main.notes_fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.data.model.Notes;
import com.example.demo.data.repository.NotesRepository;

import java.util.List;

public class NoteViewModel extends ViewModel {
    private  NotesRepository repository;
    private LiveData<Notes> notes;
    public NoteViewModel(@NonNull Application application) {
        super();
        repository=new NotesRepository(application);
    }

        NoteViewModel(NotesRepository notesRepository) {
        this.repository = notesRepository;
    }

    public LiveData<List<Notes>> getNotesList() {
        return repository.getNotesList();
    }

    public LiveData<Notes> getNotes() {
        return notes;
    }
    public NotesRepository getRepository() {
        return repository;
    }
}
