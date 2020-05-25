package com.example.demo.ui.diary.model.tag;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.ScheduleRecyclerAdapter;
import com.example.demo.data.model.Schedule;
import com.example.demo.data.model.Schedule_label;
import com.example.demo.data.repository.DiaryRepository;

import java.util.List;

public class ScheduleTagDetailActivity extends AppCompatActivity {

    DiaryRepository diaryRepository;
    TextView text_schedule_tag_title;
    RecyclerView recycler_schedule_list;
    ScheduleRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.tag_detail_toolbar);
        setSupportActionBar(toolbar);
        diaryRepository=DiaryRepository.getInstance(this.getApplication());
        initView();
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        Schedule_label label= (Schedule_label) intent.getSerializableExtra("label");
        text_schedule_tag_title.setText(label.getTitle());

        List<Schedule> scheduleListByTagStatic = diaryRepository.getScheduleListByTagStatic(label);
//        adapter.setAll(scheduleListByTagStatic);
//        adapter.notifyDataSetChanged();
        diaryRepository.getScheduleListByTag(label).observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedules) {
                adapter.setAll(schedules);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        text_schedule_tag_title=findViewById(R.id.text_tag_title);
        recycler_schedule_list=findViewById(R.id.recycler_list);
        adapter=new ScheduleRecyclerAdapter(this);
        adapter.setTag(true);
        recycler_schedule_list.setLayoutManager(new LinearLayoutManager(this));
        recycler_schedule_list.setAdapter(adapter);
    }
}
