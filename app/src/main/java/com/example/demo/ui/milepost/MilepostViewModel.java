package com.example.demo.ui.milepost;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demo.data.model.Milepost;
import com.example.demo.data.repository.MilepostRepository;
import com.example.demo.util.StringUtil;

import java.util.List;

public class MilepostViewModel extends AndroidViewModel {

    private MilepostRepository milepostRepository;
    public MilepostViewModel(@NonNull Application application) {
        super(application);
        milepostRepository=MilepostRepository.getInstance(application);
    }

    public void insertMilepost(Milepost milepost) {
        milepost.setStatus(StringUtil.INSERT_STATUS);
        milepostRepository.insertMilepost(milepost);
    }
    public LiveData<List<Milepost>>getAllMilepostList(){
        return milepostRepository.getAllMilepost();
    }

    public void updateMilepost(Milepost milepost) {
        milepost.setStatus(StringUtil.UPDATE_STATUS);
        milepostRepository.updateMilepost(milepost);
    }

    public void statusDeleteMilepost(Milepost obj) {
        milepostRepository.statusDeleteMilepost( obj);
    }
}
