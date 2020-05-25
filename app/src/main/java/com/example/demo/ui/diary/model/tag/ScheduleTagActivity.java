package com.example.demo.ui.diary.model.tag;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.ScheduleLabelRecyclerAdapter;
import com.example.demo.data.model.Schedule_label;
import com.example.demo.data.repository.DiaryRepository;
import com.example.demo.dialog.ScheduleLabelEditDialog;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.List;

public class ScheduleTagActivity extends AppCompatActivity {

    RecyclerView recycler_label_list;
    ScheduleLabelRecyclerAdapter adapter;
    DiaryRepository diaryRepository;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case StringUtil.SCHEDULE_LABEL_UPDATE:
                    diaryRepository.updateScheduleLabel((Schedule_label)msg.obj);
                    break;
                case StringUtil. SCHEDULE_LABEL_DELETE:
                    diaryRepository.statusDeleteScheduleLabel((Schedule_label)msg.obj);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_tag);
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.schedule_tag_toolbar);
        setSupportActionBar(toolbar);
        diaryRepository=DiaryRepository.getInstance(this.getApplication());
        setSupportActionBar(toolbar);
        initView();
        initData();
    }

    private void initData() {
        adapter.setAll(diaryRepository.getAllScheduleLabelStatic());
        adapter.notifyDataSetChanged();
        diaryRepository.getAllScheduleLabel().observe(this, new Observer<List<Schedule_label>>() {
            @Override
            public void onChanged(List<Schedule_label> schedule_labels) {
                adapter.setAll(schedule_labels);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        recycler_label_list=findViewById(R.id.recycler_label_list);
        adapter=new ScheduleLabelRecyclerAdapter(this,handler,ScheduleLabelRecyclerAdapter.SCHEDULE_LABEL_BIG_LAYOUT);
        recycler_label_list.setLayoutManager(new LinearLayoutManager(this));
        recycler_label_list.setAdapter(adapter);

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
                ScheduleLabelEditDialog dialog=new ScheduleLabelEditDialog(this,null);
                dialog.show();
                dialog.getAction_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Schedule_label label = dialog.getLabel();
                        if(Tool.testStringNotNULL(label.getTitle()))
                            diaryRepository.insertScheduleLabel(label);
                        dialog.dismiss();
                    }

                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
