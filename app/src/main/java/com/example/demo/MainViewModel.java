package com.example.demo;

import android.app.Activity;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.data.model.Milepost;
import com.example.demo.data.model.Task;
import com.example.demo.data.model.User;
import com.example.demo.data.repository.MilepostRepository;
import com.example.demo.data.repository.TargetRepository;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

public class MainViewModel extends ViewModel {
    TargetRepository targetRepository;
    UserRepository userRepository;
    MilepostRepository milepostRepository;
    LiveData<User> user=new  MutableLiveData<>();

    public MainViewModel(UserRepository instance) {
        userRepository=instance;
    }

    public void setCacheDataSource(Activity activity) {
        targetRepository=TargetRepository.getInstance(activity.getApplication());
        targetRepository.setCacheDataSource(activity);
        userRepository=UserRepository.getInstance(activity.getApplication());
        userRepository.setCacheDataSource(activity);
        milepostRepository=MilepostRepository.getInstance(activity.getApplication());
    }
    public Integer checkUserLogin() {
        User user = userRepository.findUser();
        if(user!=null)
            if(Tool.testStringNotNULL(StringUtil.getSessionId()))
                return StringUtil.ONLINE;
            else
                return StringUtil.OFFLINE;
        return null;
    }
    public void addTask(Task task) {

        targetRepository.insertTask(task);
    }
    public User getUserInfo() {
        return userRepository.findUser();
    }
    public void login(Integer userNumber, String password, Handler handler) {
        // can be launched in a separate asynchronous job
        userRepository.quickLogin(userNumber, password,handler);//Result<User>

    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Milepost getLiveMilepostLately() {
        return milepostRepository.getLiveMilepostLately();
    }

    public void netUpdateUserBack(User u) {
        userRepository.netUpdateUserBack( u);
    }
}
