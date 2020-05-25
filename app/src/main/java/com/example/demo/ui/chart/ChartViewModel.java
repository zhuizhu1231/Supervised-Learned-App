package com.example.demo.ui.chart;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demo.converter.MyConverter;
import com.example.demo.data.entities.Data;
import com.example.demo.data.model.Task;
import com.example.demo.data.model.TaskLog;
import com.example.demo.data.repository.TargetRepository;
import com.example.demo.util.TimeUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class ChartViewModel extends AndroidViewModel {
    TargetRepository targetRepository;


    public ChartViewModel(@NonNull Application application) {
        super(application);
        targetRepository=TargetRepository.getInstance(application);
    }

    public LiveData<Integer> sumAllWorkCount() {
        return targetRepository.sumAllWorkCount();
    }

    public LiveData<Timestamp> sumAllWorkTime() {
        return targetRepository.sumAllWorkTime();
    }


    public LiveData<List<Task>> getTaskListByWorkCountNotZero() {
        return targetRepository.getTaskListByWorkCountNotZero();
    }

    public LiveData<Integer> getDayWorkCount() {
        return targetRepository.getDayWorkCount(new Date());
    }

    public LiveData<Timestamp> getDayWorkCountTime() {
        return targetRepository.getDayWorkCountTime(new Date());
    }

    public LiveData<List<TaskLog>> getMonthTaskLog() {
        Date date = new Date();
        return targetRepository.getTaskLogTimeBetween(TimeUtils.returnMonthBeginTimestamp(date), MyConverter.dateToTimeStamp(date));
    }

    public Task getTaskById(Integer taskId) {
        return targetRepository.getTaskById(taskId);
    }

    public LiveData<List<Data>> getDayTaskLogTimeBetween() {
        return targetRepository. getDayTaskLogTimeBetween(new Date());
    }

    public long getDayTaskLogTime() {
        return targetRepository. getDayTaskLogTime(new Date());
    }
}