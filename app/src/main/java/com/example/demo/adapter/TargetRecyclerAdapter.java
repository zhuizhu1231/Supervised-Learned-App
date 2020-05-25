package com.example.demo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.model.Aim;
import com.example.demo.data.model.Task;
import com.example.demo.data.repository.TargetRepository;
import com.example.demo.dialog.AimPieDialog;
import com.example.demo.dialog.TargetEditDialog;
import com.example.demo.dialog.TaskEditDialog;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.ArrayList;
import java.util.List;

public class TargetRecyclerAdapter extends RecyclerView.Adapter<TargetRecyclerAdapter.MyViewHolder> {
    private MyViewHolder viewHolder;
    List<Aim>  allAims=new ArrayList<Aim>();
   // TaskDao taskDao;
    Activity activity;
    Context context;
    private Handler handler;
    TargetRepository targetRepository;

    public TargetRecyclerAdapter(Context context,Activity activity, Handler handler) {
        this.context=context;
        this.activity=activity;

        this.handler=handler;
        //taskDao= MyDataBase.getInstance(context).getTaskDao();
    }

    public void setTaskRepository(TargetRepository targetRepository) {
        this.targetRepository = targetRepository;
    }

    public void setAll(List<Aim> allAims) {
        this.allAims = allAims;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.recycler_target_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
         final Aim aim = allAims.get(position);
        holder.aim_title.setText(aim.getTitle());



        holder.aim_item.setOnClickListener(new View.OnClickListener() {
            TaskRecyclerAdapter adapter;
            @Override
            public void onClick(View v) {

                if(holder.recyclerView.getVisibility()==RecyclerView.GONE) {
                    adapter = new TaskRecyclerAdapter(activity,TaskRecyclerAdapter.NORMAL_LAYOUT,handler);
                    holder.aim_click_image.setImageResource(R.drawable.ic_expand_more_black_24dp);
                    holder.recyclerView.setAdapter(adapter);
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
                    holder.recyclerView.setVisibility(RecyclerView.VISIBLE);
                  //  adapter.setAll(taskDao.getTasksByAimIdStatic(aim.getId()));
                    targetRepository.getTasksByAimId(aim.getId()).observe((LifecycleOwner) context, new Observer<List<Task>>() {
                        @Override
                        public void onChanged(List<Task> tasks) {
                            adapter.setAll(tasks);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                else {
                    holder.aim_click_image.setImageResource(R.drawable.ic_right_black_24dp);
                    holder.recyclerView.setVisibility(RecyclerView.GONE);
                }

            }

        });

        holder.aim_chart_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AimPieDialog dialog=new AimPieDialog(context,activity.getApplication(),aim);
                dialog.show();
            }
        });
        holder.aim_setting_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TargetEditDialog targetEditDialog=new TargetEditDialog(context,aim);
                targetEditDialog.show();
                targetEditDialog.getImage_action_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Aim dialogAim= targetEditDialog.getAim();
                        if(Tool.testStringNotNULL(dialogAim.getTitle())){
                            Tool.sendMessage(handler,StringUtil.TARGET_UPDATE,dialogAim);
                        }
                        targetEditDialog.dismiss();
                    }
                });
                targetEditDialog.getImage_delete().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tool.alertDialogShow(context,context.getString(R.string.delete_sure_notice),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Tool.sendMessage(handler,StringUtil.TARGET_DELETE,targetEditDialog.getAim());
                                        dialog.dismiss();
                                    }
                                }
                                );
                       // Tool.sendMessage(handler,StringUtil.TARGET_DELETE,targetEditDialog.getAim());
                        targetEditDialog.dismiss();
                    }
                });

            }
        });

        holder.add_task_in_aim_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TaskEditDialog dialog=new TaskEditDialog(context,null);
                dialog.show();
                dialog.getImage_action_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Task task = dialog.getTask();
                      //  if(Tool.testStringNotNULL(task.getTitle())&&context instanceof MainActivity){

                        if(Tool.testStringNotNULL(task.getTitle())){
                            Message msg=Message.obtain();
                            msg.what= StringUtil.TASK_INSERT;
                            task.setAimId(aim.getId());
                            msg.obj=task;
                           // MainActivity activity= (MainActivity) context;
                            handler.sendMessage(msg);
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return allAims.size();
    }



    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView aim_title;
        RecyclerView recyclerView;
        ConstraintLayout aim_item;
        ImageView aim_click_image,add_task_in_aim_image,aim_setting_image,aim_chart_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            aim_title=itemView.findViewById(R.id.aim_title);
            recyclerView=itemView.findViewById(R.id.aim_task_recycler_view);
            aim_item=itemView.findViewById(R.id.aim_item);
            aim_click_image=itemView.findViewById(R.id.aim_click_image);
            add_task_in_aim_image=itemView.findViewById(R.id.task_in_aim_add_image);
            aim_setting_image=itemView.findViewById(R.id.aim_setting_image);
            aim_chart_image=itemView.findViewById(R.id.aim_chart_image);
        }
    }
}
