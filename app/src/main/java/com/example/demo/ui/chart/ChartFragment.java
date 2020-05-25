package com.example.demo.ui.chart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.TaskChartRecyclerAdapter;
import com.example.demo.converter.MyConverter;
import com.example.demo.data.entities.ChartItem;
import com.example.demo.data.entities.Data;
import com.example.demo.data.model.Task;
import com.example.demo.data.model.TaskLog;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import im.dacer.androidcharts.BarView;
import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;


public class ChartFragment extends Fragment {

    private ChartViewModel chartViewMode;
  //  private FragmentChartBinding binding;
    RecyclerView recycler_task_list,recycler_task_list_day;
    PieView pieView;
    PieView pieView_day;
    TextView text_recount,text_recount_time,text_recount_time_aver,no_data,no_data_day;
    TextView text_recount_day,text_recount_time_day;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        chartViewMode =
                ViewModelProviders.of(this).get(ChartViewModel.class);
//        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_chart,container,false);
//        binding.setLifecycleOwner(this);
        View view=inflater.inflate(R.layout.fragment_chart, container, false);
        initView(view);
        initData(view);
        return  view;
    }

    private void initData(View view) {
        TaskChartRecyclerAdapter  adapter=new TaskChartRecyclerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_task_list.setLayoutManager(linearLayoutManager);
        recycler_task_list.setAdapter(adapter);
        chartViewMode.sumAllWorkCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==null) text_recount.setText("0");
                else text_recount.setText(""+integer);
                recountTimeAverChange();
            }
        });

        chartViewMode.sumAllWorkTime().observe(getViewLifecycleOwner(), new Observer<Timestamp>() {
            @Override
            public void onChanged(Timestamp timestamp) {
                if(timestamp!=null) {
                    text_recount_time.setText("" + TimeUtils.timeStampParseToMinus(timestamp));
                    recountTimeAverChange();
                }else text_recount_time.setText("0" );
            }
        });
         chartViewMode.getDayWorkCount().observe(getViewLifecycleOwner(), new Observer<Integer>() {
             @Override
             public void onChanged(Integer integer) {
                 text_recount_day.setText(""+integer);
             }
         });
         chartViewMode.getDayWorkCountTime().observe(getViewLifecycleOwner(), new Observer<Timestamp>() {
             @Override
             public void onChanged(Timestamp integer) {
                 if(integer!=null)
                 text_recount_time_day.setText(""+TimeUtils.timeStampParseToMinus(integer));
                 else text_recount_time_day.setText("0");
             }
         });
        chartViewMode.getTaskListByWorkCountNotZero().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {

            @Override
            public void onChanged(List<Task> tasks) {
                ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
                ArrayList<ChartItem> chartItems=new ArrayList<>();
                int color=0xc0000000;
                if(tasks!=null) no_data.setVisibility(View.GONE);
                for(Task task:tasks){
                    color+=(Math.random()*1000000);
                    PieHelper helper=new PieHelper(getPercent(task),color);
                    pieHelperArrayList.add(helper);
                    ChartItem chartItem=new ChartItem(task.getTitle(),color);
                    chartItems.add(chartItem);
                    color+=0x01000000;
                }
                pieView.setDate(pieHelperArrayList);
                pieView.showPercentLabel(true);
                adapter.setAllChartItem(chartItems);
                adapter.notifyDataSetChanged();
            }
        });


        TaskChartRecyclerAdapter adapter_day=new TaskChartRecyclerAdapter();
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_task_list_day.setLayoutManager(linearLayoutManager);
        recycler_task_list_day.setAdapter(adapter_day);

       chartViewMode.getDayTaskLogTimeBetween().observe(getViewLifecycleOwner(), new Observer<List<Data>>() {
           @Override
           public void onChanged(List<Data> data) {
               ArrayList<PieHelper> pieHelperArrayList = new ArrayList<PieHelper>();
               ArrayList<ChartItem> chartItems=new ArrayList<>();
               long dayTaskLogTime = chartViewMode.getDayTaskLogTime();
               int color=0xc0000000;
               if(data!=null) no_data_day.setVisibility(View.GONE);
               for(Data task:data){
                   color+=(Math.random()*1000000);
                   PieHelper helper=new PieHelper(getPercent(task.getTime(),dayTaskLogTime),color);
                   pieHelperArrayList.add(helper);
                   ChartItem chartItem=new ChartItem(chartViewMode.getTaskById(task.getTaskId()).getTitle(),color);
                   chartItems.add(chartItem);
                   color+=0x01000000;
               }
               pieView_day.setDate(pieHelperArrayList);
               pieView_day.showPercentLabel(true);
               adapter_day.setAllChartItem(chartItems);
               adapter_day.notifyDataSetChanged();
           }
       });
       chartViewMode.getMonthTaskLog().observe(getViewLifecycleOwner(), new Observer<List<TaskLog>>() {
           @Override
           public void onChanged(List<TaskLog> taskLogs) {
               ArrayList<String> strList = new ArrayList<>();
              ArrayList<Integer> dataList = new ArrayList<>();
               int time=0;
               int max=0;
               if(taskLogs!=null&&taskLogs.size()>0) {

                   for (int i=0;i<taskLogs.size();) {

                       Date date = new Date();
                       Timestamp monthBeginTimestamp = TimeUtils.returnMonthDayOneTimestamp(date);
                       Timestamp todayBeginTimestamp = TimeUtils.addOneDay(TimeUtils.returnTodayBeginTimestamp(date));


                       while (todayBeginTimestamp.after(monthBeginTimestamp)) {
                           TaskLog log=null;
                           if(i<taskLogs.size())
                            log=taskLogs.get(i);


                           if (log!=null&&log.getTimeStamp().before(TimeUtils.returnTodayEndTimestamp(monthBeginTimestamp))) {
                               Task task = chartViewMode.getTaskById(log.getTaskId());
                               if(task!=null)
                                   time += TimeUtils.timeStampParseToMinus(task.getWorkTime());
                               i++;

                           } else {
                               if (max < time) max = time;
                               String dateTime = TimeUtils.DatePickerDateConverter.stampDateFormatMonthDay(MyConverter.longToTimestamp(monthBeginTimestamp.getTime()));
                               strList.add(dateTime + " " + time);
                               dataList.add(time);
                               time = 0;
                               if (log != null) {
                                   while (log.getTimeStamp().after(TimeUtils.returnTodayEndTimestamp(monthBeginTimestamp))){
                                       monthBeginTimestamp = TimeUtils.addOneDay(monthBeginTimestamp);
                                       if (log.getTimeStamp().after(TimeUtils.returnTodayEndTimestamp(monthBeginTimestamp))) {
                                           dateTime = TimeUtils.DatePickerDateConverter.stampDateFormatMonthDay(MyConverter.longToTimestamp(monthBeginTimestamp.getTime()));
                                           strList.add(dateTime + " " + time);
                                           dataList.add(time);
                                       }
                                   }
                                   Task task = chartViewMode.getTaskById(log.getTaskId());
                                   time += TimeUtils.timeStampParseToMinus(task.getWorkTime());
                                   i++;
                               }else monthBeginTimestamp = TimeUtils.addOneDay(monthBeginTimestamp);
                           }
                       }


                   }
                   BarView barView = view.findViewById(R.id.bar_view);
                   barView.setBottomTextList(strList);
                   barView.setDataList(dataList, max);

//               LineView lineView = view.findViewById(R.id.line_view);
//               lineView.setDrawDotLine(true); //optional
//               lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
//
//               lineView.setBottomTextList(strList);
//
//               lineView.setColorArray(new int[]{Color.BLACK,Color.GREEN,Color.GRAY,Color.CYAN});
//
//               lineView.setDataList(dataLists);
               }
           }
       });


    }

    private float getPercent(Timestamp date, long dayTaskLogTime) {
        Long time=MyConverter.timestampToLong(date);

        Float aFloat=(float)(time*1.0/dayTaskLogTime);
        return aFloat*100;
    }

    private float getPercent(Task task) {
        Long time=task.getWorkCount()*MyConverter.timestampToLong(task.getWorkTime());
        Long all = MyConverter.timestampToLong(TimeUtils.minusParseToTimeStamp(Integer.valueOf(text_recount_time.getText().toString())));
        Float aFloat=(float)(time*1.0/all);
        return aFloat*100;
    }

    private void recountTimeAverChange(){
        String recount_time = text_recount_time.getText().toString();
        String recount = text_recount.getText().toString();
        if(Tool.testStringNotNULL(recount)&&Tool.testStringNotNULL(recount_time)){
            if(Integer.valueOf(recount)!=0)
             text_recount_time_aver.setText(""+(Integer.valueOf(recount_time)/Integer.valueOf(recount)));
        }
    }
    private void initView(View view) {
        pieView = view.findViewById(R.id.pie_view);
        text_recount=view.findViewById(R.id.text_recount);
        text_recount_time=view.findViewById(R.id.text_recount_time);
        text_recount_time_aver=view.findViewById(R.id.text_recount_time_aver);
        pieView = view.findViewById(R.id.pie_view);
        recycler_task_list=view.findViewById(R.id.recycler_task_list);
        no_data=view.findViewById(R.id.no_data);
        pieView_day = view.findViewById(R.id.pie_view_day);
        recycler_task_list_day=view.findViewById(R.id.recycler_task_list_day);
        no_data_day=view.findViewById(R.id.no_data_day);
        text_recount_day=view.findViewById(R.id.text_recount_day);
        text_recount_time_day=view.findViewById(R.id.text_recount_time_day);
    }
}