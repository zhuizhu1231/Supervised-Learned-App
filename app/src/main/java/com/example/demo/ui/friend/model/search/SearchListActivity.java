package com.example.demo.ui.friend.model.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.FriendRecyclerAdapter;
import com.example.demo.data.model.Friend;

import java.util.ArrayList;

public class SearchListActivity extends AppCompatActivity {

    ArrayList<Friend> list;
    RecyclerView recycler_search_list;
    FriendRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        getData();
        initView();
    }

    private void initView() {
        recycler_search_list=findViewById(R.id.recycler_search_list);
        recycler_search_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new FriendRecyclerAdapter(this,null);
        adapter.setType(FriendRecyclerAdapter.TYPE_ADD);
        recycler_search_list.setAdapter(adapter);
        adapter.setAllFriends(list);
        adapter.notifyDataSetChanged();
    }

    private void getData() {
        Intent intent = getIntent();
        list = (ArrayList<Friend>) intent.getSerializableExtra("list");
    }
}
