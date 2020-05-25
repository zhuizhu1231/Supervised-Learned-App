package com.example.demo.ui.diary.model.create;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.ScheduleCreateRecyclerAdapter;
import com.example.demo.data.model.Schedule;

import java.util.List;

public class DiaryCreateTodayActivity extends AppCompatActivity {
    private DiaryViewModel diaryViewModel;

    RecyclerView import_urgent_recycler_view,import_no_urgent_recycler_view
            ,no_import_urgent_recycler_view,no_import_no_urgent_recycler_view;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_create_today);
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.diary_today_toolbar);
        setSupportActionBar(toolbar);
        diaryViewModel = ViewModelProviders.of(this,new DiaryViewModelFactory()).get(DiaryViewModel.class);
        diaryViewModel.getDiaryRepository().setCacheDataSource(this);

        initFindViewById();
    }
//    public static final int CATEGORY_IMPORT_EMERGE=1;
//    public static final int CATEGORY_IMPORT_NO_EMERGE=2;
//    public static final int CATEGORY_NO_IMPORT_EMERGE=3;
//    public static final int CATEGORY_NO_IMPORT_NO_EMERGE=4;
    private void initFindViewById() {
        import_urgent_recycler_view=findViewById(R.id.import_urgent_recycler_view);
        import_no_urgent_recycler_view=findViewById(R.id.import_no_urgent_recycler_view);
        no_import_urgent_recycler_view=findViewById(R.id.no_import_urgent_recycler_view);
        no_import_no_urgent_recycler_view=findViewById(R.id.no_import_no_urgent_recycler_view);

        ScheduleCreateRecyclerAdapter adapterImportUrgent=new ScheduleCreateRecyclerAdapter(this,handler);
        import_urgent_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        import_urgent_recycler_view.setAdapter(adapterImportUrgent);
        diaryViewModel.getTodayScheduleListImportEmerge().observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedules) {
                adapterImportUrgent.setAll(schedules);
                adapterImportUrgent.notifyDataSetChanged();
            }
        });

        ScheduleCreateRecyclerAdapter adapterImportNoUrgent=new ScheduleCreateRecyclerAdapter(this,handler);
        import_no_urgent_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        import_no_urgent_recycler_view.setAdapter(adapterImportNoUrgent);
        diaryViewModel.getTodayScheduleListImportNoEmerge().observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedules) {
                adapterImportNoUrgent.setAll(schedules);
                adapterImportNoUrgent.notifyDataSetChanged();
            }
        });

        ScheduleCreateRecyclerAdapter adapterNoImportUrgent=new ScheduleCreateRecyclerAdapter(this,handler);
        no_import_urgent_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        no_import_urgent_recycler_view.setAdapter(adapterNoImportUrgent);
        diaryViewModel.getTodayScheduleListNoImportEmerge().observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedules) {
                adapterNoImportUrgent.setAll(schedules);
                adapterNoImportUrgent.notifyDataSetChanged();
            }
        });

        ScheduleCreateRecyclerAdapter adapterNoImportNoUrgent=new ScheduleCreateRecyclerAdapter(this,handler);
        no_import_no_urgent_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        no_import_no_urgent_recycler_view.setAdapter(adapterNoImportNoUrgent);
        diaryViewModel.getTodayScheduleListNoImportNoEmerge().observe(this, new Observer<List<Schedule>>() {
            @Override
            public void onChanged(List<Schedule> schedules) {
                adapterNoImportNoUrgent.setAll(schedules);
                adapterNoImportNoUrgent.notifyDataSetChanged();
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
                Intent openScheduleEditActivity=new Intent(this,ScheduleEditActivity.class);
                startActivity(openScheduleEditActivity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
