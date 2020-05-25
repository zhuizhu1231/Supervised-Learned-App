package com.example.demo.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.model.Milepost;
import com.example.demo.dialog.MilepostEditDialog;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class MilepostRecyclerAdapter extends RecyclerView.Adapter<MilepostRecyclerAdapter.MyViewHolder> {
    List<Milepost>  allMileposts=new ArrayList<Milepost>();
    public final static Integer BIG_LAYOUT=1;
    public final static Integer NORMAL_LAYOUT=2;
    TimeUtils.DatePickerDateConverter converter=new TimeUtils.DatePickerDateConverter();
    Activity mActivity;
    Handler handler;
    public MilepostRecyclerAdapter(Activity activity,Handler handler) {
        mActivity=activity;
        this.handler=handler;
    }

    public void setAll(List<Milepost> allMileposts) {
        this.allMileposts = allMileposts;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.recycler_milepost_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
         final Milepost milepost = allMileposts.get(position);
        holder.text_milepost_title.setText(milepost.getTitle());
        holder.text_milepost_remark.setText(milepost.getRemark());
        holder.text_milepost_die_time.setText(converter.timestampToDateFormat(milepost.getDieTime()));
        Timestamp newTimeStamp = Tool.createNewTimeStamp();
        int day=TimeUtils.timeStampIntervalParseToDay(milepost.getDieTime(),newTimeStamp);
        if(milepost.getDieTime().before(newTimeStamp)){
            holder.text_day_rest.setText(""+0);
            holder.text_pass_day.setVisibility(View.VISIBLE);
            holder.text_pass_day.setText("已过期"+day+"天");
        }else {
            holder.text_day_rest.setText(""+day);
            holder.text_pass_day.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MilepostEditDialog dialog=new MilepostEditDialog(mActivity,milepost);
                dialog.show();
                dialog.getImage_action_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Milepost milepost = dialog.getMilepost();
                        if(Tool.testStringNotNULL(milepost.getTitle())){
                            Tool.sendMessage(handler, StringUtil.MILEPOST_UPDATE,milepost);
                        }
                        dialog.dismiss();
                    }
                });
                dialog.getAction_delete().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Milepost milepost = dialog.getMilepost();
                        AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
                        builder.setMessage(mActivity.getString(R.string.delete_sure_notice));
                        builder.setCancelable(true);
                        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Tool.sendMessage(handler, StringUtil.MILEPOST_DELETE,milepost);
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();

                        dialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return allMileposts.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text_milepost_title,text_day_rest,text_milepost_remark,text_milepost_die_time,text_pass_day;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_milepost_title=itemView.findViewById(R.id.text_milepost_title);
            text_day_rest=itemView.findViewById(R.id.text_day_rest);
            text_milepost_remark=itemView.findViewById(R.id.text_milepost_remark);
            text_milepost_die_time=itemView.findViewById(R.id.text_milepost_die_time);
            text_pass_day=itemView.findViewById(R.id.text_pass_day);

        }
    }
}
