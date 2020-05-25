package com.example.demo.ui.diary.model.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.ScheduleRecyclerAdapter;
import com.example.demo.data.model.Schedule;
import com.example.demo.data.repository.DiaryRepository;
import com.example.demo.dialog.ScheduleDateChooseDialog;
import com.example.demo.ui.diary.model.create.ScheduleEditActivity;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;

import java.util.List;

public class DiaryHistoryActivity extends AppCompatActivity {
    ConstraintLayout toolbar_date_choose;
    ScheduleDateChooseDialog dialog;
    DiaryRepository diaryRepository;
    RecyclerView recycler_schedule_list;
    ScheduleRecyclerAdapter adapter;
    TextView schedule_date,text_date_choose;
    ImageView image_action_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_history);
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.diary_history_toolbar);
        setSupportActionBar(toolbar);
        diaryRepository=DiaryRepository.getInstance(this.getApplication());
        initView();
        initData();
        setDialogShowListener();
        setScheduleAddListener();

       // dialog.show();

    }

    private void setScheduleAddListener() {
        image_action_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openScheduleEditActivity=new Intent(DiaryHistoryActivity.this, ScheduleEditActivity.class);
                openScheduleEditActivity.putExtra("date",TimeUtils.DatePickerDateConverter.dateFormat(dialog.getDate()));
                startActivity(openScheduleEditActivity);
            }
        });
    }

    private void initData() {
        dialog=new ScheduleDateChooseDialog(DiaryHistoryActivity.this);
        dialog.show();
        dialog.dismiss();
        adapter=new ScheduleRecyclerAdapter(this);
        recycler_schedule_list.setLayoutManager(new LinearLayoutManager(this));
        recycler_schedule_list.setAdapter(adapter);
        adapter.setAll( diaryRepository.getDayScheduleListByTimestampBetweenStatic(dialog.getDate()));
        adapter.notifyDataSetChanged();
        Observer<List<Schedule>> observer = new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedules) {
                adapter.setAll(schedules);
                adapter.notifyDataSetChanged();
            }
        };
        dialog.setSureClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_date_choose.setText(TimeUtils.DatePickerDateConverter.dateFormat(dialog.getDate()));
                schedule_date.setText(TimeUtils.DatePickerDateConverter.dateFormat(dialog.getDate()));
                if(dialog.getRadioButtonChoose()==dialog.radioButton_all){
                    adapter.setAll( diaryRepository.getDayScheduleListByTimestampBetweenStatic(dialog.getDate()));
                    adapter.notifyDataSetChanged();
                    diaryRepository.getDayScheduleListByTimestampBetween(dialog.getDate()).observe(DiaryHistoryActivity.this, observer);
                }
                else {
                    int done;
                    if(dialog.getRadioButtonChoose()==dialog.radioButton_done)done=StringUtil.DONE;
                    else done=StringUtil.NOT_DONE;
                   // Integer done=dialog.getRadioButtonChoose()==dialog.radioButton_done? StringUtil.DONE:StringUtil.NOT_DONE;
                    adapter.setAll(diaryRepository.getDayScheduleListByTimestampBetweenScheduleDoneStatic(dialog.getDate(),done));
                    adapter.notifyDataSetChanged();
                    diaryRepository.getDayScheduleListByTimestampBetweenScheduleDone(dialog.getDate(), done).observe(DiaryHistoryActivity.this, observer);
                }
                dialog.dismiss();
            }

        });
        text_date_choose.setText(TimeUtils.DatePickerDateConverter.dateFormat(dialog.getDate()));
        schedule_date.setText(TimeUtils.DatePickerDateConverter.dateFormat(dialog.getDate()));
    }

    private void setDialogShowListener() {
        toolbar_date_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        text_date_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        schedule_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void initView() {
        toolbar_date_choose=findViewById(R.id.toolbar_date_choose);
        recycler_schedule_list=findViewById(R.id.recycler_list);
        schedule_date=findViewById(R.id.schedule_date);
        text_date_choose=findViewById(R.id.text_date_choose);
        image_action_add=findViewById(R.id.image_action_add);
    }
}
