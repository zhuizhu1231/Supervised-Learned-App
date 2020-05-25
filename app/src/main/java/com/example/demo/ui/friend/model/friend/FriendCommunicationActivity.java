package com.example.demo.ui.friend.model.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.demo.R;
import com.example.demo.adapter.MessageRecyclerAdapter;
import com.example.demo.data.model.Friend;
import com.example.demo.data.model.Message;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;
import com.example.demo.websocket.JWebSocketClient;

import java.util.List;

public class FriendCommunicationActivity extends AppCompatActivity {

    CommunicationViewModel communicationViewModel;
    RecyclerView message_recycler_view;
    MessageRecyclerAdapter adapter;
    JWebSocketClient client;
    TextView name;
    EditText send_edit_text;
    Button send_action_button;
    Friend friend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_communication);
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.communication_toolbar);
        setSupportActionBar(toolbar);
        communicationViewModel= ViewModelProviders.of(this).get(CommunicationViewModel.class);
        getData();
        initView();
        sendMessageButtonListener();

    }

    private void sendMessageButtonListener() {
        send_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = send_edit_text.getText().toString();
                if(Tool.testStringNotNULL(data)){
                    if(client!=null) {
                        Message message = new Message();
                        message.setSendId(StringUtil.getUser().getDbId());
                        message.setReceiverId(friend.getFriendId());
                        message.setContent(data);
                        client.send(JSON.toJSONString(Message.toEntity(message)));
                        send_edit_text.setText("");
                    }
                }
            }
        });
    }

    private void getData() {
        Intent intent = getIntent();
        friend= (Friend) intent.getSerializableExtra("friend");
        client= StringUtil.getClient();
    }

    private void initView() {
        name=findViewById(R.id.name);
        if(friend.getName()==null)
            name.setText(""+friend.getFriendId());
        else if(friend.getRemark()==null)
            name.setText(friend.getName());
        else
            name.setText(friend.getRemark());
        message_recycler_view=findViewById(R.id.message_recycler_view);
        send_edit_text=findViewById(R.id.send_edit_text);
        send_action_button=findViewById(R.id.send_action_button);
        message_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new MessageRecyclerAdapter(this,friend);
        message_recycler_view.setAdapter(adapter);
        communicationViewModel.getMessageByFriend(friend).observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                adapter.setAll(messages);
                adapter.notifyDataSetChanged();
                message_recycler_view.getLayoutManager().scrollToPosition(adapter.getItemCount());
                communicationViewModel.readMessage(messages);
                //message_recycler_view.setFocus
            }
        });
    }
}
