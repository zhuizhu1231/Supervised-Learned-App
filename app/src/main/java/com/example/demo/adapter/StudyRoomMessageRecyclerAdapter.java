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
import com.example.demo.data.model.Friend;
import com.example.demo.data.model.Study_room;
import com.example.demo.data.model.Study_room_message;
import com.example.demo.data.model.User_in_study_room;
import com.example.demo.data.repository.StudyRoomRepository;
import com.example.demo.ui.friend.model.search.FriendAddActivity;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.ui.friend.model.search.FriendAddActivity.FRIEND_ADD;

public class StudyRoomMessageRecyclerAdapter extends RecyclerView.Adapter<StudyRoomMessageRecyclerAdapter.MyViewHolder> {
    private MyViewHolder viewHolder;
    List<Study_room_message> allStudy_room_message=new ArrayList<>();
    Study_room studyRoom;
    Activity context;
    StudyRoomRepository studyRoomRepository;

    public void setAll(List<Study_room_message> allStudy_room_message) {
        this.allStudy_room_message = allStudy_room_message;

      
    }


    public StudyRoomMessageRecyclerAdapter(Activity context, StudyRoomRepository studyRoomRepository, Study_room studyRoom) {
       this.context=context;
        this.studyRoomRepository = studyRoomRepository;
        this.studyRoom=studyRoom;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.recycler_message_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Study_room_message message = allStudy_room_message.get(position);
        if(message.getSendUserId()== StringUtil.getUser().getDbId().intValue()){
            holder.friend_name.setVisibility(View.GONE);
            holder.friend_content.setVisibility(View.GONE);
            holder.friend_head.setVisibility(View.GONE);
            holder.my_name.setText(StringUtil.getUser().getName());
            holder.my_content.setText(message.getContent());

//            holder.my_head.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }else{
            holder.my_name.setVisibility(View.GONE);
            holder.my_content.setVisibility(View.GONE);
            holder.my_head.setVisibility(View.GONE);
            holder.person=studyRoomRepository.getUserByRoomIdUserId(studyRoom.getDbId(),message.getSendUserId());
            holder.friend_name.setText(""+holder.person.getNameInRoom());

            holder.friend_content.setText(message.getContent());

//            holder.friend_head.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //TODO:跳到被点击头像的信息页
//
//
//                }
//            });
        }
        holder.my_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Friend friend=new Friend();
                friend.setDbId(message.getSendUserId());
                Intent intent=new Intent(context, FriendAddActivity.class);
                intent.putExtra("friend",friend);
                intent.putExtra("type",FRIEND_ADD);

                context.startActivityForResult(intent, StringUtil.Return);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allStudy_room_message.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView friend_name,friend_content,my_name,my_content;
        ImageView friend_head,my_head;
        User_in_study_room person;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            friend_head=itemView.findViewById(R.id.friend_head);
            friend_name=itemView.findViewById(R.id.friend_name);
            friend_content=itemView.findViewById(R.id.friend_content);
            my_head=itemView.findViewById(R.id.my_head);
            my_name=itemView.findViewById(R.id.my_name);
            my_content=itemView.findViewById(R.id.my_content);
        }
    }
}
