package com.example.demo.ui.task;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demo.data.model.Task;
import com.example.demo.data.repository.TargetRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {



    private TargetRepository targetRepository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        targetRepository=TargetRepository.getInstance(application);
    }

    public LiveData<List<Task>> getTaskAloneList() {
        return targetRepository.getTaskAloneList();
    }


    public void insertTask(Task ...tasks){

        targetRepository.insertTask(tasks);
    }

    public void UpdateTask(Task ...tasks){
        targetRepository.updateTask(tasks);
    }



    public void deleteTaskByStatus(Task obj) {
        targetRepository.statusDeleteTask(obj);
    }
}