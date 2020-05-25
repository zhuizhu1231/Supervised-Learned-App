package com.example.demo.ui.diary.model.create;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demo.data.model.Schedule;
import com.example.demo.data.model.Schedule_in_label;
import com.example.demo.data.model.Schedule_label;
import com.example.demo.data.repository.DiaryRepository;

import java.util.List;

public class ScheduleEditViewModel extends AndroidViewModel {
    DiaryRepository diaryRepository;
    public ScheduleEditViewModel(@NonNull Application application) {
        super(application);
        diaryRepository=DiaryRepository.getInstance(application);
    }

    public LiveData<List<Schedule_label>> getAllScheduleLabel() {
        return diaryRepository.getAllScheduleLabel();
    }

    public List<Schedule_label> getAllScheduleLabelStatic() {
        return diaryRepository.getAllScheduleLabelStatic();
    }

    public long saveSchedule(Schedule schedule) {
        return diaryRepository.insertSchedule(schedule);
    }

    public void insertRelationAsync(Schedule_in_label relation) {
        diaryRepository.insertRelationAsync( relation);
    }

    public List<Schedule_label> getScheduleLabelByScheduleId(Integer scheduleId) {
        return diaryRepository.getScheduleLabelByScheduleIdStatic( scheduleId);
    }

    public void updateSchedule(Schedule schedule) {
        diaryRepository.updateSchedule(schedule);
    }

    public void statusDeleteRelationByScheduleIdLabelIdAsync(Integer scheduleId, Integer labelId) {
        Schedule_in_label relation=diaryRepository.getRelationByScheduleIdLabelIdStatic(scheduleId,labelId);
        diaryRepository.statusDeleteRelationAsync(relation);
    }

    public void statusDeleteScheduleAsync(Schedule schedule) {
        diaryRepository.statusDeleteScheduleAsync( schedule);
    }
}
