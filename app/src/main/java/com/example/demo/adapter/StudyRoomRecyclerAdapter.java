package com.example.demo.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.model.Study_room;
import com.example.demo.data.repository.StudyRoomRepository;
import com.example.demo.net.json.Bean;
import com.example.demo.ui.group.other.RoomCreateActivity;
import com.example.demo.ui.group.study.StudyroomActivity;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.ArrayList;
import java.util.List;


public class StudyRoomRecyclerAdapter extends RecyclerView.Adapter<StudyRoomRecyclerAdapter.MyViewHolder> {
    private MyViewHolder viewHolder;
    List<Study_room> allStudy_rooms=new ArrayList<>();
    Context context;
    StudyRoomRepository studyRoomRepository;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtil.JOIN_ROOM_ID_SUCCESS:
                    studyRoomRepository.cacheRoomListData((List<Bean<com.example.demo.data.entities.Study_room>>) msg.obj);
                    break;
                case StringUtil.JOIN_ROOM_ID_FAIL:
                    break;
            }
        }
    };
    public StudyRoomRecyclerAdapter(Context context, StudyRoomRepository studyRoomRepository) {
        this.context=context;
        this.studyRoomRepository=studyRoomRepository;


    }



    public void setAllStudy_rooms(List<Study_room> allStudy_rooms) {
        this.allStudy_rooms = allStudy_rooms;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.recycler_study_room_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Study_room room = allStudy_rooms.get(position);
        holder.study_room_name.setText(room.getName());
        Study_room r=studyRoomRepository.getRoomByDbId(room.getDbId());
        if(r!=null)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, StudyroomActivity.class);
                intent.putExtra("room",room);
                StringUtil.setRoomId(room.getDbId());
                context.startActivity(intent);
            }
        });
        else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, RoomCreateActivity.class);
                    intent.putExtra("room",room);
                    context.startActivity(intent);
                }
            });
            holder.add_room_button.setVisibility(View.VISIBLE);
            holder.add_room_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tool.alertDialogShow(context,"是否加入该自习室？" ,new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            studyRoomRepository.addRoomByDbId(handler,room.getDbId());
                            dialog.dismiss();
                        }
                    });
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return allStudy_rooms.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView study_room_name;
        Button add_room_button;
        public MyViewHolder(@NonNull View itemView) {
           super(itemView);
            study_room_name=itemView.findViewById(R.id.study_room_name);
            add_room_button=itemView.findViewById(R.id.add_room_button);
        }
    }
}
