package com.example.demo.adapter;

import android.app.Activity;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.model.Task;
import com.example.demo.dialog.TaskDetailDialog;
import com.example.demo.util.LockUtil;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.MyViewHolder> {
    List<Task>  allTasks=new ArrayList<Task>();
    public final static Integer BIG_LAYOUT=1;
    public final static Integer NORMAL_LAYOUT=2;
    private Integer now_layout;
    Activity mActivity;
    Handler handler;
    public TaskRecyclerAdapter(Activity activity,int layout_style,Handler handler) {
        mActivity=activity;
        this.now_layout=layout_style;
        this.handler=handler;


    }

    public void setAll(List<Task> allTasks) {
        this.allTasks = allTasks;

    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView;
        if(now_layout==BIG_LAYOUT)
            itemView=layoutInflater.inflate(R.layout.recycler_task_item_layout,parent,false);
        else
            itemView=layoutInflater.inflate(R.layout.recycler_target_task_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
         final Task task = allTasks.get(position);
        holder.task_title.setText(task.getTitle());
        holder.task_time.setText(TimeUtils.timeStampParseToMinus(task.getWorkTime()).toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskDetailDialog dialog=new TaskDetailDialog(mActivity,task);
//                Window window = dialog.getWindow();
//                window.setGravity(Gravity.TOP);
                dialog.setHandler(handler);
                dialog.show();

            }
        });
        holder.task_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringUtil.setTask(task);
                LockUtil.lock(mActivity,task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allTasks.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView task_title,task_time;
        Button task_start;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            task_title=itemView.findViewById(R.id.aim_title);
            task_time=itemView.findViewById(R.id.task_time);
            task_start=itemView.findViewById(R.id.task_start);
        }
    }
}
