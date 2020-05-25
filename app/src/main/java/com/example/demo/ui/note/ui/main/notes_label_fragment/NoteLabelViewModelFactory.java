package com.example.demo.ui.note.ui.main.notes_label_fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.example.demo.data.datasource.NotesDataSource;
import com.example.demo.data.repository.NotesRepository;


public class NoteLabelViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NoteLabelViewModel.class)) {
            return (T) new NoteLabelViewModel(NotesRepository.getInstance(new NotesDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
