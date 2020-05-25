package com.example.demo.ui.close;

import androidx.lifecycle.ViewModel;

import com.example.demo.data.datasource.TargetDataSource;
import com.example.demo.data.model.Task;
import com.example.demo.data.repository.TargetRepository;

public class LockViewModel extends ViewModel {
    private TargetRepository taskRepository;
    public LockViewModel(){
        taskRepository=TargetRepository.getInstance(new TargetDataSource());
    }

    public void addTaskCount(Task task) {
        taskRepository.addTaskCount(task);
    }
}
