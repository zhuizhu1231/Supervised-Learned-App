package com.example.demo.ui.group.other;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.StudyRoomRecyclerAdapter;
import com.example.demo.data.model.Study_room;
import com.example.demo.data.repository.StudyRoomRepository;

import java.util.ArrayList;

public class RoomSearchListActivity extends AppCompatActivity {

    RecyclerView recycler_search_list;
    StudyRoomRecyclerAdapter adapter;
    StudyRoomRepository studyRoomRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        studyRoomRepository=StudyRoomRepository.getInstance(this.getApplication());
        initView();
    }

    private void initView() {
        recycler_search_list=findViewById(R.id.recycler_search_list);
        recycler_search_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new  StudyRoomRecyclerAdapter(this,studyRoomRepository);
        recycler_search_list.setAdapter(adapter);
        Intent intent = getIntent();
        ArrayList<Study_room> list = (ArrayList<Study_room>) intent.getSerializableExtra("list");
        adapter.setAllStudy_rooms(list);
        adapter.notifyDataSetChanged();
    }


}
