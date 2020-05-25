package com.example.demo.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.data.model.Friend;
import com.example.demo.data.repository.FriendRepository;
import com.example.demo.ui.friend.model.friend.FriendCommunicationActivity;
import com.example.demo.ui.friend.model.search.FriendAddActivity;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.ui.friend.model.search.FriendAddActivity.FRIEND_ADD;
import static com.example.demo.ui.friend.model.search.FriendAddActivity.FRIEND_DETAIL;


public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.MyViewHolder> {
    private MyViewHolder viewHolder;
    List<Friend> allFriends=new ArrayList<>();
    Activity context;
    FriendRepository friendRepository;
    public static Integer TYPE_COMMUNICATION=0;
    public static Integer TYPE_ADD=1;
    private  int type=0;


    public void setType(int type) {
        this.type = type;
    }

    public FriendRecyclerAdapter(Activity context, FriendRepository friendRepository) {
        this.context=context;
        this.friendRepository=friendRepository;

    }



    public void setAllFriends(List<Friend> allFriends) {
        this.allFriends = allFriends;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View itemView=layoutInflater.inflate(R.layout.recycler_friend_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Friend friend = allFriends.get(position);
        if(friend.getName()==null){
            holder.add_friend_button.setVisibility(View.VISIBLE);
            holder.friend_name.setText(""+friend.getFriendId());
            holder.friend_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toFriendAddActivity(friend,FRIEND_ADD);
                }
            });
            holder.friend_number.setVisibility(View.GONE);
        }
        else {
            holder.friend_number.setText(""+friend.getFriendId());
            holder.friend_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startIntent(friend,FRIEND_DETAIL);
                }
            });
            if (friend.getRemark() == null)
                holder.friend_name.setText(friend.getName());
            else
                holder.friend_name.setText(friend.getRemark());
        }
        //holder.friend_status.setText(String.valueOf(friend.getIsLogin()));
        if(type==TYPE_COMMUNICATION)
        friendRepository.getMessageNotReadBySender(friend).observe((LifecycleOwner) context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(integer==0)
                    holder.text_count_message_no_read.setVisibility(View.GONE);
                else
                    holder.text_count_message_no_read.setVisibility(View.VISIBLE);
                holder.text_count_message_no_read.setText(""+integer);
            }
        });
        if(type==TYPE_COMMUNICATION)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, FriendCommunicationActivity.class);
                    intent.putExtra("friend",friend);
                    context.startActivity(intent);
                }
            });
        else
            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFriendAddActivity(friend,FRIEND_ADD);
            }
        });
        holder.add_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               toFriendAddActivity(friend,FRIEND_ADD);

            }
        });
    }

    private void toFriendAddActivity(Friend friend,int type){
        Tool.alertDialogShow(context,context.getString(R.string.add_friend_sure), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               startIntent(friend,type);
                dialog.dismiss();
            }
        });

    }
    private  void startIntent(Friend friend,int type){
        Intent intent=new Intent(context, FriendAddActivity.class);
        intent.putExtra("friend",friend);
        intent.putExtra("type",type);

        context.startActivityForResult(intent, StringUtil.Return);
    }
    @Override
    public int getItemCount() {
        return allFriends.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView friend_name,friend_number,text_count_message_no_read;
        ImageView friend_image;
        Button add_friend_button;
        public MyViewHolder(@NonNull View itemView) {
           super(itemView);
           friend_image=itemView.findViewById(R.id.friend_image);
           friend_name=itemView.findViewById(R.id.friend_name);
           friend_number=itemView.findViewById(R.id.friend_number);
           text_count_message_no_read=itemView.findViewById(R.id.text_count_message_no_read);
            add_friend_button=itemView.findViewById(R.id.add_friend_button);
        }
    }
}
