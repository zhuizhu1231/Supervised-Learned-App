package com.example.demo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.converter.MyConverter;
import com.example.demo.data.model.Schedule;
import com.example.demo.ui.diary.model.create.ScheduleEditActivity;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.MyViewHolder> {
    List<Schedule>  allSchedules=new ArrayList<Schedule>();
    Activity mActivity;
    Calendar calendar=Calendar.getInstance();
    public Boolean tag;
    public ScheduleRecyclerAdapter(Activity activity) {
        mActivity=activity;
    }

    public void setAll(List<Schedule> allSchedules) {
        this.allSchedules = allSchedules;

    }

    public void setTag(Boolean tag) {
        this.tag = tag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.recycler_schedule_time_axis_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
         final Schedule schedule = allSchedules.get(position);
        holder.schedule_title.setText(schedule.getTitle());

        if(schedule.getIsDone()!=null&&schedule.getIsDone()== StringUtil.DONE)
            holder.text_schedule_done.setText(mActivity.getString(R.string.done));
        else
            holder.text_schedule_done.setText(mActivity.getString(R.string.not_done));
        calendar.setTime(MyConverter.timeStampToDate(schedule.getBelongTime()));
        holder.schedule_time.setText(format(calendar.get(Calendar.HOUR_OF_DAY))+":"+format(calendar.get(Calendar.MINUTE)));
        if(schedule.getCategory()==StringUtil.CATEGORY_IMPORT_EMERGE)
            holder.image_degree.setImageResource(R.drawable.icon_schedule_red);
        else if(schedule.getCategory()==StringUtil.CATEGORY_IMPORT_NO_EMERGE)
            holder.image_degree.setImageResource(R.drawable.icon_schedule_yellow);
        else if(schedule.getCategory()==StringUtil.CATEGORY_NO_IMPORT_EMERGE)
            holder.image_degree.setImageResource(R.drawable.icon_schedule_blue);
        else if(schedule.getCategory()==StringUtil.CATEGORY_NO_IMPORT_NO_EMERGE)
            holder.image_degree.setImageResource(R.drawable.icon_schedule_green);
        if(schedule.getProperty()==StringUtil.CLOCK_SET) holder.clock.setVisibility(View.VISIBLE);
        String s = TimeUtils.DatePickerDateConverter.dateFormat(calendar.getTime());
        if(tag!=null) {
            holder.schedule_date.setText(s);holder.schedule_date.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editSchedule =new Intent(mActivity, ScheduleEditActivity.class);
                editSchedule.putExtra("schedule",schedule);
                mActivity.startActivity(editSchedule);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allSchedules.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView schedule_title,text_schedule_done,schedule_time,schedule_date;
        View view;

        ImageView image_degree,clock;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            schedule_title=itemView.findViewById(R.id.schedule_title);
            text_schedule_done=itemView.findViewById(R.id.text_schedule_done);
            schedule_time=itemView.findViewById(R.id.schedule_time);
            image_degree=itemView.findViewById(R.id.image_degree);
            schedule_date=itemView.findViewById(R.id.schedule_date);
            clock=itemView.findViewById(R.id.clock);
            view=itemView.findViewById(R.id.view);
        }
    }

    private String format(int x) {
        String s = Integer.toString(x);
        if (s.length()== 1)
            return "0" + s;
        return s;
    }
}
