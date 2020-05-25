package com.example.demo.ui.group.study;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.demo.data.model.Study_room_message;
import com.example.demo.data.repository.StudyRoomRepository;

public class StudyRoomViewModel extends AndroidViewModel {
    StudyRoomRepository studyRoomRepository;
    public StudyRoomViewModel(@NonNull Application application) {
        super(application);
        studyRoomRepository=StudyRoomRepository.getInstance(application);
    }

    public void saveMessage(Study_room_message message) {
        studyRoomRepository.saveMessage(message);
    }
}
