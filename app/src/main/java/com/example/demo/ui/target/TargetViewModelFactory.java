package com.example.demo.ui.target;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.demo.data.datasource.TargetDataSource;
import com.example.demo.data.repository.TargetRepository;

public class TargetViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TargetViewModel.class)) {
            return (T) new TargetViewModel(TargetRepository.getInstance(new TargetDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
 }