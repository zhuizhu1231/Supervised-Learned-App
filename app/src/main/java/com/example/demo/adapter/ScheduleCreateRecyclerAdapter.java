package com.example.demo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.model.Schedule;
import com.example.demo.ui.diary.model.create.ScheduleEditActivity;

import java.util.ArrayList;
import java.util.List;


public class ScheduleCreateRecyclerAdapter extends RecyclerView.Adapter<ScheduleCreateRecyclerAdapter.MyViewHolder> {
    List<Schedule>  allSchedules=new ArrayList<Schedule>();
    Activity mActivity;
    Handler handler;
    public ScheduleCreateRecyclerAdapter(Activity activity, Handler handler) {
        mActivity=activity;
    }

    public void setAll(List<Schedule> allSchedules) {
        this.allSchedules = allSchedules;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.recycler_schedule_create_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
         final Schedule schedule = allSchedules.get(position);
        holder.schedule_title.setText(schedule.getTitle());

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
        TextView schedule_title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            schedule_title=itemView.findViewById(R.id.schedule_title);
        }
    }
}
