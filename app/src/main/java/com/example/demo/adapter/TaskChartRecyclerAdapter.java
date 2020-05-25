package com.example.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.entities.ChartItem;

import java.util.ArrayList;
import java.util.List;

public class TaskChartRecyclerAdapter extends RecyclerView.Adapter<TaskChartRecyclerAdapter.MyViewHolder> {
    private MyViewHolder viewHolder;
    List<ChartItem> allChartItem=new ArrayList<>();

    public void setAllChartItem(List<ChartItem> allChartItem) {
        this.allChartItem = allChartItem;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.task_recycler_chart_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChartItem task = allChartItem.get(position);
        holder.task_title.setText(task.getTitle());
        holder.chart_color.setBackgroundColor(task.getBackground());//task.getBackground()//0x10000000
    }

    @Override
    public int getItemCount() {
        return allChartItem.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView task_title;
         ConstraintLayout chart_color;
        public MyViewHolder(@NonNull View itemView) {
           super(itemView);
        
           task_title=itemView.findViewById(R.id.task_title);
            chart_color=itemView.findViewById(R.id.chart_color);
        }
    }
}
