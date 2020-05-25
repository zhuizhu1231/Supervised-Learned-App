package com.example.demo.ui.note.ui.main.notes_label_fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.data.model.Notes_label;
import com.example.demo.data.repository.NotesRepository;

import java.util.List;

public class NoteLabelViewModel extends ViewModel {
    private NotesRepository repository;
    private LiveData<Notes_label> noteLabel;
    public NoteLabelViewModel(@NonNull Application application) {
        super();
        repository=new NotesRepository(application);
    }

    public NoteLabelViewModel() {
    }

    public  NoteLabelViewModel(NotesRepository NoteLabelRepository) {
        this.repository = NoteLabelRepository;
    }

    public LiveData<List<Notes_label>> getNoteLabelList() {
        return repository.getNoteLabelList();
    }

    public LiveData<Notes_label> getNoteLabel() {
        return noteLabel;
    }
    public NotesRepository getRepository() {
        return repository;
    }

    public void updateLabel(Notes_label obj) {
        repository. updateLabel(obj);
    }

    public void statusDeleteLabel(Notes_label obj) {
        repository.statusDeleteLabel(obj);
    }
}
