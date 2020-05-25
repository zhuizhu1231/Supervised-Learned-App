package com.example.demo.ui.friend;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demo.data.model.Friend;
import com.example.demo.data.model.Message;
import com.example.demo.data.repository.FriendRepository;
import com.example.demo.net.json.Bean;

import java.util.List;

public class FriendViewModel extends AndroidViewModel {
    FriendRepository friendRepository;

    public FriendViewModel(@NonNull Application application) {
        super(application);
        friendRepository=FriendRepository.getInstance(application);
    }

    public void saveMessage(Message message) {
        friendRepository.saveMessage(message);
    }

    public LiveData<List<Friend>> getFriendList() {
        return  friendRepository.getFriendList();
    }

    public void netQueryMessageList(Handler handler) {
        friendRepository.netQueryMessageList( handler);

    }

    public void cacheMessageListData(List<Bean<com.example.demo.data.entities.Message>> obj) {
        friendRepository.cacheMessageListData(obj);
    }



//    public LiveData<List<Friend>> getFriendList() {
//        return friendRepository.getTaskAloneList();
//    }
}

