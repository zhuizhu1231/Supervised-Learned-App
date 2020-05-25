package com.example.demo.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.model.Schedule_label;
import com.example.demo.dialog.ScheduleLabelEditDialog;
import com.example.demo.ui.diary.model.tag.ScheduleTagDetailActivity;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.ArrayList;
import java.util.List;

public class ScheduleLabelRecyclerAdapter extends RecyclerView.Adapter<ScheduleLabelRecyclerAdapter.MyViewHolder> {

    List<Schedule_label>  allSchedule_labels=new ArrayList<Schedule_label>();
    public final static Integer SCHEDULE_LABEL_BIG_LAYOUT=1;
    public final static Integer SCHEDULE_LABEL_SMALL_LAYOUT=2;
    public final static Integer SCHEDULE_LABEL_SEARCH_LAYOUT=3;
    private Integer now_layout;
    Handler handler;
    Activity mActivity;
    public ScheduleLabelRecyclerAdapter(Activity activity, Handler handler,int layout_style) {
        mActivity=activity;
        this.now_layout=layout_style;
        this.handler=handler;
    }

    public void setAll(List<Schedule_label> allSchedule_labels) {
        this.allSchedule_labels = allSchedule_labels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView;
        if(now_layout==SCHEDULE_LABEL_BIG_LAYOUT)
            itemView=layoutInflater.inflate(R.layout.recycler_tag_item_layout,parent,false);
        else if(now_layout==SCHEDULE_LABEL_SMALL_LAYOUT)
            itemView=layoutInflater.inflate(R.layout.labels_recycler_view_item,parent,false);
        else
            itemView=layoutInflater.inflate(R.layout.recycler_search_tag_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

         final Schedule_label label = allSchedule_labels.get(position);
        holder.note_label_title.setText(label.getTitle());
        if(null!=label.getScheduleCount())
            holder.note_label_count.setText(label.getScheduleCount().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mActivity, ScheduleTagDetailActivity.class);
                intent.putExtra("label",label);
                mActivity.startActivity(intent);
            }
        });

        if(now_layout==SCHEDULE_LABEL_BIG_LAYOUT){
            holder.image_action_setting=holder.itemView.findViewById(R.id.image_action_setting);
            holder.image_action_delete=holder.itemView.findViewById(R.id.image_action_delete);
            holder.image_action_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tool.alertDialogShow(mActivity,null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Tool.sendMessage(handler, StringUtil.SCHEDULE_LABEL_DELETE, label);
                            dialog.dismiss();
                        }
                    });
                }
            });
            holder.image_action_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ScheduleLabelEditDialog scheduleLabelEditDialog=new ScheduleLabelEditDialog(mActivity,label);
                    scheduleLabelEditDialog.show();
                    scheduleLabelEditDialog.getAction_sure().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Schedule_label dialogLabel = scheduleLabelEditDialog.getLabel();
                            if(Tool.testStringNotNULL(dialogLabel.getTitle()))
                                 Tool.sendMessage(handler, StringUtil.SCHEDULE_LABEL_UPDATE, dialogLabel);
                            scheduleLabelEditDialog.dismiss();
                        }
                    });
                    scheduleLabelEditDialog.getImage_action_delete().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Tool.alertDialogShow(mActivity,null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Tool.sendMessage(handler, StringUtil.SCHEDULE_LABEL_DELETE, scheduleLabelEditDialog.getLabel());
                                    dialog.dismiss();
                                }
                            });
                            scheduleLabelEditDialog.dismiss();
                        }
                    });
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return allSchedule_labels.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView note_label_title,note_label_count;
        ImageView image_action_setting,image_action_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            note_label_title=itemView.findViewById(R.id.tag_title);
            note_label_count=itemView.findViewById(R.id.tag_count);

        }
    }

    public List<Schedule_label> getAllSchedule_labels() {
        return allSchedule_labels;
    }
    public void addOrRemoveScheduleLabel(Schedule_label label){
        boolean temp=true;
        for (Schedule_label i:allSchedule_labels){
            if(i.getId().equals(label.getId())){
                allSchedule_labels.remove(i);
                temp=false;
                break;
            }
        }
        if(temp) allSchedule_labels.add(label);
    }
}
