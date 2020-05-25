package com.example.demo.ui.group.study.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.FriendRecyclerAdapter;
import com.example.demo.data.model.Friend;
import com.example.demo.data.model.Study_room;
import com.example.demo.data.model.User_in_study_room;
import com.example.demo.data.repository.StudyRoomRepository;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupMemberFragment extends Fragment {
    
    RecyclerView recycler_room_member;
    StudyRoomRepository studyRoomRepository;
    FriendRecyclerAdapter adapter;
    Study_room room;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtil.ROOM_QUERY_MEMBER_LIST_SUCCESS:
                    studyRoomRepository.clearCacheByRoomId(room.getDbId());
                    studyRoomRepository.cacheRoomIdMemberListData((List<Bean<com.example.demo.data.entities.User_in_study_room>>) msg.obj);
                    break;
                case StringUtil.ROOM_QUERY_MEMBER_LIST_FAIL:
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_group_member, container, false);
        studyRoomRepository=StudyRoomRepository.getInstance(this.getActivity().getApplication());
        recycler_room_member=view.findViewById(R.id.recycler_room_member);
        recycler_room_member.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter=new FriendRecyclerAdapter(this.getActivity(),null);
        adapter.setType(FriendRecyclerAdapter.TYPE_ADD);
        recycler_room_member.setAdapter(adapter);
        Intent intent = getActivity().getIntent();
         room= (Study_room) intent.getSerializableExtra("room");
        studyRoomRepository.netQueryMemberListByRoomId(handler,room.getDbId());
        studyRoomRepository.getMemberByRoomId(room.getDbId()).observe(getViewLifecycleOwner(), new Observer<List<User_in_study_room>>() {
            @Override
            public void onChanged(List<User_in_study_room> user_in_study_rooms) {
                ArrayList<Friend> list=new ArrayList<>();
                for(User_in_study_room member:user_in_study_rooms){
                    Friend friend=new Friend();
                    friend.setFriendId(member.getUserId());
                    friend.setName(member.getNameInRoom());
                    list.add(friend);
                }
                adapter.setAllFriends(list);
                adapter.notifyDataSetChanged();
            }
        });
        return view;
    }

}
