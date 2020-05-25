package com.example.demo.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.StudyRoomRecyclerAdapter;
import com.example.demo.data.model.Study_room;
import com.example.demo.data.repository.StudyRoomRepository;
import com.example.demo.ui.group.other.SearchRoomActivity;

import java.util.List;

public class GroupsActivity extends AppCompatActivity {

    StudyRoomRepository studyRoomRepository;
    RecyclerView recycler_study_room_list;
    StudyRoomRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.group_toolbar);
        setSupportActionBar(toolbar);
        studyRoomRepository=StudyRoomRepository.getInstance(this.getApplication());
        initView();

    }

    private void initView() {
        recycler_study_room_list=findViewById(R.id.recycler_study_room_list);
        recycler_study_room_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new StudyRoomRecyclerAdapter(this,studyRoomRepository);
        recycler_study_room_list.setAdapter(adapter);
        studyRoomRepository.findStudyRoomList().observe(this, new Observer<List<Study_room>>() {
            @Override
            public void onChanged(List<Study_room> study_rooms) {
                adapter.setAllStudy_rooms(study_rooms);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:

                Intent intent=new Intent(this, SearchRoomActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
