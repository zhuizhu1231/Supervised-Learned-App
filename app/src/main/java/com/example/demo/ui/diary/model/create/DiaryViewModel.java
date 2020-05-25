package com.example.demo.ui.diary.model.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.data.model.Schedule;
import com.example.demo.data.repository.DiaryRepository;
import com.example.demo.util.StringUtil;

import java.util.Date;
import java.util.List;

public class DiaryViewModel extends ViewModel {
    private DiaryRepository diaryRepository;
    private MutableLiveData<String> mText;

    DiaryViewModel(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public DiaryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is diary fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Schedule>> getTodayScheduleListImportNoEmerge() {
        return  diaryRepository.getDayScheduleListByCategoryTimestampBetween(StringUtil.CATEGORY_IMPORT_NO_EMERGE,new Date());
    }
    public LiveData<List<Schedule>> getTodayScheduleListNoImportEmerge() {
        return  diaryRepository.getDayScheduleListByCategoryTimestampBetween(StringUtil.CATEGORY_NO_IMPORT_EMERGE,new Date());
    }
    public LiveData<List<Schedule>> getTodayScheduleListNoImportNoEmerge() {
        return  diaryRepository.getDayScheduleListByCategoryTimestampBetween(StringUtil.CATEGORY_NO_IMPORT_NO_EMERGE,new Date());
    }
    public LiveData<List<Schedule>> getTodayScheduleListImportEmerge() {
        return  diaryRepository.getDayScheduleListByCategoryTimestampBetween(StringUtil.CATEGORY_IMPORT_EMERGE,new Date());
    }

    public DiaryRepository getDiaryRepository() {
        return diaryRepository;
    }
}