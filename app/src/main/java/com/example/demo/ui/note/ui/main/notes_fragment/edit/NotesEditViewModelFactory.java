package com.example.demo.ui.note.ui.main.notes_fragment.edit;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.demo.data.datasource.NotesDataSource;
import com.example.demo.data.repository.NotesRepository;

public class NotesEditViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NoteEditViewModel.class)) {
            return (T) new NoteEditViewModel(NotesRepository.getInstance(new NotesDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
