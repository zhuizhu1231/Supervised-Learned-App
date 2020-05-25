package com.example.demo.ui.friend.model.friend;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.FriendRecyclerAdapter;
import com.example.demo.data.model.Friend;
import com.example.demo.data.repository.FriendRepository;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class NoFriendActivity extends AppCompatActivity {

    RecyclerView recycler_not_friend;
    FriendRecyclerAdapter adapter;
    FriendRepository friendRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_friend);
        friendRepository=FriendRepository.getInstance(this.getApplication());
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.no_friend_toolbar);
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        recycler_not_friend=findViewById(R.id.recycler_not_friend);
        recycler_not_friend.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new FriendRecyclerAdapter(this,friendRepository);
        recycler_not_friend.setAdapter(adapter);
        friendRepository.getNotFriendIdListByMessage().observe(this, new Observer<List<Integer>>() {
            @Override
            public void onChanged(List<Integer> integers) {
                ArrayList<Friend> friendArrayList=new ArrayList<>();
                for(Integer id:integers) {
                    Friend friend = new Friend();
                    friend.setFriendId(id);
                    friendArrayList.add(friend);
                }
                adapter.setAllFriends(friendArrayList);
                adapter.notifyDataSetChanged();
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== StringUtil.Return&&resultCode==RESULT_OK){
           finish();
        }
    }
}
