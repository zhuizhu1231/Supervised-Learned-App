package com.example.demo.ui.group.study.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.demo.R;
import com.example.demo.adapter.StudyRoomMessageRecyclerAdapter;
import com.example.demo.data.model.Study_room;
import com.example.demo.data.model.Study_room_message;
import com.example.demo.data.repository.StudyRoomRepository;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;
import com.example.demo.websocket.JWebSocketClient;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatFragment extends Fragment {

    RecyclerView message_recycler_view;
    StudyRoomMessageRecyclerAdapter adapter;
    EditText send_edit_text;
    Button send_action_button;
    View view;
    Study_room room;
    StudyRoomRepository studyRoomRepository;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        studyRoomRepository=StudyRoomRepository.getInstance(this.getActivity().getApplication());
        view=inflater.inflate(R.layout.fragment_group_chat, container, false);
        getData();
        initView();
        sendMessageButtonListener();
        return view;
    }
    private void sendMessageButtonListener() {
        send_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = send_edit_text.getText().toString();
                if(Tool.testStringNotNULL(data)){
                    JWebSocketClient roomClient = StringUtil.getRoomClient();
                    if(roomClient !=null) {
                        Study_room_message message = new Study_room_message();
                        message.setSendUserId(StringUtil.getUser().getDbId());
                        message.setContent(data);
                        roomClient.send(JSON.toJSONString(Study_room_message.toEntity(message)));
                        send_edit_text.setText("");
                    }
                }
            }
        });
    }
    private void getData() {
        Intent intent = getActivity().getIntent();
        room = (Study_room) intent.getSerializableExtra("room");
    }

    private void initView() {
        message_recycler_view=view.findViewById(R.id.message_recycler_view);
        send_edit_text=view.findViewById(R.id.send_edit_text);
        send_action_button=view.findViewById(R.id.send_action_button);
        message_recycler_view.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        adapter=new StudyRoomMessageRecyclerAdapter(this.getActivity(),studyRoomRepository,room);
        message_recycler_view.setAdapter(adapter);
        studyRoomRepository.getMessageListByRoomDbId(room.getDbId()).observe(getViewLifecycleOwner(), new Observer<List<Study_room_message>>() {
            @Override
            public void onChanged(List<Study_room_message> study_room_messages) {
                adapter.setAll(study_room_messages);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
