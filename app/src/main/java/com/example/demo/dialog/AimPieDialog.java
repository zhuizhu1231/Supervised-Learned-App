package com.example.demo.dialog;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.TaskChartRecyclerAdapter;
import com.example.demo.converter.MyConverter;
import com.example.demo.data.entities.ChartItem;
import com.example.demo.data.model.Aim;
import com.example.demo.data.model.Task;
import com.example.demo.data.repository.TargetRepository;
import com.example.demo.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;

public class AimPieDialog extends AlertDialog {
    Aim aim;
    TargetRepository targetRepository;

    Application application;
    TextView aim_title,recount_time,no_data;
    PieView pie_view;
    RecyclerView recycler_task_list;
    long allTime=0;
    public AimPieDialog(@NonNull Context context, Application application,Aim aim) {
       super(context);
       this.application=application;
       this.aim=aim;
        targetRepository= TargetRepository.getInstance(application);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_aim_task_pie_data);
        initView();
        initData();
    }

    private void initData() {
        TaskChartRecyclerAdapter adapter=new TaskChartRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_task_list.setLayoutManager(linearLayoutManager);
        recycler_task_list.setAdapter(adapter);
        aim_title.setText(aim.getTitle());
       // Timestamp sumAllTimestamp = targetRepository.sumAllWorkTimeByAimStatic(aim);

        List<Task> taskList =targetRepository.getTaskListByAimWorkCountNotZeroStatic(aim);
        List<Integer> taskCount=new ArrayList<>();
        for(Task task:taskList){
            Integer count = targetRepository.getTodayTaskCountByTaskId(task.getId());
            taskCount.add(count);
            allTime+=count* MyConverter.timestampToLong(task.getWorkTime());
        }
        if(allTime==0) no_data.setVisibility(View.GONE);
        else recount_time.setText(""+ TimeUtils.timeStampParseToMinus(MyConverter.longToTimestamp(allTime)));
        ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();

        ArrayList<ChartItem> chartItems=new ArrayList<>();
       // Color colori= Color.valueOf(0xc0000000);
        int color=0xc0000000;
        if(taskList!=null) no_data.setVisibility(View.GONE);
        for(int i=0;i<taskList.size();i++){
            Integer integer = taskCount.get(i);
            if(integer!=null&&integer.intValue()>0) {
                Task task = taskList.get(i);
                color += (Math.random() * 1000000);
                PieHelper helper = new PieHelper(getPercent(task, integer), color);
                pieHelperArrayList.add(helper);
                ChartItem chartItem = new ChartItem(task.getTitle(), color);
                chartItems.add(chartItem);
                color += 0x01000000;
            }
        }
        pie_view.setDate(pieHelperArrayList);
        pie_view.showPercentLabel(true);
        adapter.setAllChartItem(chartItems);
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        aim_title=findViewById(R.id.aim_title);
        recount_time=findViewById(R.id.recount_time);
        pie_view=findViewById(R.id.pie_view);
        recycler_task_list=findViewById(R.id.recycler_task_list);
        no_data=findViewById(R.id.no_data);
    }

    private float getPercent(Task task, Integer integer) {
        Float aFloat=(float)((integer.intValue()*MyConverter.timestampToLong(task.getWorkTime()))*1.0/allTime);
//        Long time=task.getWorkCount()* MyConverter.timestampToLong(task.getWorkTime());
//        Long all = MyConverter.timestampToLong(TimeUtils.minusParseToTimeStamp(Integer.valueOf(recount_time.getText().toString())));
//        return aFloat*100;
        return aFloat*100;
    }
}
