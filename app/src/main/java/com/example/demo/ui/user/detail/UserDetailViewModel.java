package com.example.demo.ui.user.detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demo.R;
import com.example.demo.data.model.User;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.util.TimeUtils;

import java.sql.Timestamp;

public class UserDetailViewModel extends AndroidViewModel {
    UserRepository userRepository;
    Application application;
    public UserDetailViewModel(@NonNull Application application) {
        super(application);
        this.application=application;
        userRepository=UserRepository.getInstance(application);
    }
    public LiveData<User> getUser(){
       return userRepository.getUserInfo();
    }
    public String MinusConverter(Timestamp data){
        return TimeUtils.timeStampParseToMinus(data)+application.getString(R.string.minus_dimen);
    }
    public String DateConverter(Timestamp data){
        return TimeUtils.DatePickerDateConverter.timeFormat(data);
    }
}
