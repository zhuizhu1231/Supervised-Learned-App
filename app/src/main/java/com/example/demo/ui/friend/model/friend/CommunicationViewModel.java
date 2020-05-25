package com.example.demo.ui.friend.model.friend;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.demo.data.model.Friend;
import com.example.demo.data.model.Message;
import com.example.demo.data.repository.FriendRepository;

import java.util.List;

public class CommunicationViewModel extends AndroidViewModel {
    FriendRepository friendRepository;
    public CommunicationViewModel(@NonNull Application application) {
        super(application);
        friendRepository=FriendRepository.getInstance(application);
    }

    public LiveData<List<Message>> getMessageByFriend(Friend friend) {
        return friendRepository.getMessageByFriend(friend);
    }

    public void readMessage(List<Message> messages) {
        friendRepository.readMessage( messages);
    }
}
