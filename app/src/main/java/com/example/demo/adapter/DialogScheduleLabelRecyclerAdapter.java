package com.example.demo.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.model.Schedule_label;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class DialogScheduleLabelRecyclerAdapter extends RecyclerView.Adapter<DialogScheduleLabelRecyclerAdapter.MyViewHolder> {
    private MyViewHolder viewHolder;
    List<Schedule_label>  allSchedule_labels=new ArrayList<Schedule_label>();
    Handler handler;
    Activity mActivity;
    public DialogScheduleLabelRecyclerAdapter(Activity activity, Handler handler) {
        mActivity=activity;
        this.handler=handler;

    }

    public void setAll(List<Schedule_label> allSchedule_labels) {
        this.allSchedule_labels = allSchedule_labels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.dialog_recycler_label_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
         final Schedule_label label = allSchedule_labels.get(position);
          holder.text_label_title.setText(label.getTitle());

          holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Message message = Message.obtain();
                    message.what = StringUtil.SCHEDULE_ADD_LABEL_DIALOG_MESSAGE;
                    message.obj = label;
                    handler.sendMessage(message);

            }
        });

    }

    @Override
    public int getItemCount() {
        return allSchedule_labels.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text_label_title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_label_title=itemView.findViewById(R.id.text_label_title);
        }
    }

    public List<Schedule_label> getAllSchedule_labels() {
        return allSchedule_labels;
    }
}
