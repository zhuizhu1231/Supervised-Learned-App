package com.example.demo.ui.friend.model.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.FriendRecyclerAdapter;
import com.example.demo.data.model.Friend;
import com.example.demo.data.repository.FriendRepository;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.ArrayList;
import java.util.List;

public class FriendSearchActivity extends AppCompatActivity {

    ConstraintLayout net_search_friend;
    RecyclerView recycler_friend;
    FriendRecyclerAdapter adapter;
    EditText edit_text_key;
    TextView text_key;
    FriendRepository friendRepository;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtil.QUERY_FRIEND_LIKE_SUCCESS:
                    ArrayList<Friend> list= (ArrayList<Friend>) com.example.demo.data.entities.Friend.unpack((List<Bean< com.example.demo.data.entities.Friend >>)msg.obj);
                    Intent intent=new Intent(FriendSearchActivity.this, SearchListActivity.class);
                    intent.putExtra("list",list);
                    startActivity(intent);
                    break;
                case StringUtil.QUERY_FRIEND_LIKE_FAIL:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        friendRepository=FriendRepository.getInstance(this.getApplication());
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.friend_search_toolbar);
        setSupportActionBar(toolbar);
        initView();
        keyChangeListener();
        searchFriendListener();
    }

    private void searchFriendListener() {
        net_search_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendRepository.netSearchFriendListByNameLikeOrDbIdLike(handler,edit_text_key.getText().toString());
            }
        });
    }

    private void keyChangeListener() {
        edit_text_key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String title = s.toString();
                if(Tool.testStringNotNULL(title)){
                    net_search_friend.setVisibility(View.VISIBLE);
                    String titleLike=Tool.stringTurnToLike(title);
                    Integer id = null;
                    try {
                        id=Integer.valueOf(title);
                    }catch (Exception e){}
                    friendRepository.getFriendListByRemarkLikeOrDbIdLike(titleLike,id).observe(FriendSearchActivity.this, new Observer<List<Friend>>() {
                        @Override
                        public void onChanged(List<Friend> friends) {
                            adapter.setAllFriends(friends);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    text_key.setText(title);
                }else {
                    net_search_friend.setVisibility(View.GONE);
                }
            }
        });
    }


    private void initView() {
        net_search_friend=findViewById(R.id.net_search_friend);
        recycler_friend=findViewById(R.id.recycler_friend);
        recycler_friend.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new FriendRecyclerAdapter(this,friendRepository);
        recycler_friend.setAdapter(adapter);
        edit_text_key=findViewById(R.id.edit_text_key);
        text_key=findViewById(R.id.text_key);
    }
}
