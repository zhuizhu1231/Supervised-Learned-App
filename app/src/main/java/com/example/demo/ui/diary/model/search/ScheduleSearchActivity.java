package com.example.demo.ui.diary.model.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.ScheduleLabelRecyclerAdapter;
import com.example.demo.adapter.ScheduleRecyclerAdapter;
import com.example.demo.data.model.Schedule;
import com.example.demo.data.model.Schedule_label;
import com.example.demo.data.repository.DiaryRepository;
import com.example.demo.util.Tool;

import java.util.List;

public class ScheduleSearchActivity extends AppCompatActivity {

    EditText edit_text_key;
    RecyclerView recycler_tag,recycler_schedule;

    ScheduleLabelRecyclerAdapter scheduleLabelRecyclerAdapter;
    ScheduleRecyclerAdapter scheduleRecyclerAdapter;
    DiaryRepository diaryRepository;
//  Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_search);
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.schedule_search_toolbar);
        setSupportActionBar(toolbar);
        diaryRepository=DiaryRepository.getInstance(this.getApplication());
        initView();
        keyChangeListener();
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
                    String titleLike=Tool.stringTurnToLike(title);
                    diaryRepository.getScheduleLabelByTitleLike(titleLike).observe(ScheduleSearchActivity.this, new Observer<List<Schedule_label>>() {
                        @Override
                        public void onChanged(List<Schedule_label> schedule_labels) {
                            scheduleLabelRecyclerAdapter.setAll(schedule_labels);
                            scheduleLabelRecyclerAdapter.notifyDataSetChanged();
                        }
                    });
                    diaryRepository.getScheduleByTitleLike(titleLike).observe(ScheduleSearchActivity.this, new Observer<List<Schedule>>() {
                        @Override
                        public void onChanged(List<Schedule> schedule) {
                            scheduleRecyclerAdapter.setAll(schedule);
                            scheduleRecyclerAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    private void initView() {
        edit_text_key=findViewById(R.id.edit_text_key);
        recycler_schedule=findViewById(R.id.recycler_schedule);
        recycler_tag=findViewById(R.id.recycler_tag);
        scheduleLabelRecyclerAdapter=new ScheduleLabelRecyclerAdapter(this,null,ScheduleLabelRecyclerAdapter.SCHEDULE_LABEL_SEARCH_LAYOUT);
        scheduleRecyclerAdapter=new ScheduleRecyclerAdapter(this);
        scheduleRecyclerAdapter.setTag(true);
        recycler_schedule.setLayoutManager(new LinearLayoutManager(this));
        recycler_tag.setLayoutManager(new LinearLayoutManager(this));
        recycler_tag.setAdapter(scheduleLabelRecyclerAdapter);
        recycler_schedule.setAdapter(scheduleRecyclerAdapter);

    }
}
