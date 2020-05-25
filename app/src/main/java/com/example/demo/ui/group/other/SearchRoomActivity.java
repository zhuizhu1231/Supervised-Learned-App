package com.example.demo.ui.group.other;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.StudyRoomRecyclerAdapter;
import com.example.demo.data.model.Study_room;

import com.example.demo.data.repository.StudyRoomRepository;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.ui.group.other.RoomCreateActivity.TYPE_ADD;

public class SearchRoomActivity extends AppCompatActivity {
    ConstraintLayout room_create;
    StudyRoomRepository studyRoomRepository;
    ConstraintLayout net_search_room;
    RecyclerView recycler_room;
    StudyRoomRecyclerAdapter adapter;
    EditText edit_text_key;
    TextView text_key;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtil.QUERY_ROOM_LIKE_SUCCESS:

                    ArrayList<Study_room> list = (ArrayList<Study_room>) com.example.demo.data.entities.Study_room.unpack((List<Bean<com.example.demo.data.entities.Study_room>>) msg.obj);
                    Intent intent = new Intent(SearchRoomActivity.this, RoomSearchListActivity.class);
                    intent.putExtra("list", list);
                    startActivity(intent);

                    break;
                case StringUtil.QUERY_ROOM_LIKE_FAIL:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studyRoomRepository=StudyRoomRepository.getInstance(this.getApplication());
        setContentView(R.layout.activity_search_room);
        initView();
        keyChangeListener();
        searchRoomListener();
        roomCreateListener();
    }

    private void searchRoomListener() {
        net_search_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studyRoomRepository.netSearchRoomListByNameLikeOrDbIdLike(handler,edit_text_key.getText().toString());
            }
        });
    }
    private void keyChangeListener() {
        edit_text_key.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String title = s.toString();
                if(Tool.testStringNotNULL(title)){
                    net_search_room.setVisibility(View.VISIBLE);
                    String titleLike=Tool.stringTurnToLike(title);
                    Integer id = null;
                    try {
                        id=Integer.valueOf(title);
                    }catch (Exception e){}
                    studyRoomRepository.getRoomListByRemarkNameLikeOrDbIdLike(titleLike,id).observe(SearchRoomActivity.this, new Observer<List<Study_room>>() {
                        @Override
                        public void onChanged(List<Study_room> rooms) {
                            adapter.setAllStudy_rooms(rooms);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    text_key.setText(title);
                }else {
                    net_search_room.setVisibility(View.GONE);
                }
            }
        });
    }


    private void roomCreateListener() {
        room_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo:跳转到room  create
                Intent intent=new Intent(SearchRoomActivity.this,RoomCreateActivity.class);
                intent.putExtra("type",TYPE_ADD);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        room_create=findViewById(R.id.constraintLayout_room_create);
        net_search_room=findViewById(R.id.net_search_room);
        recycler_room=findViewById(R.id.recycler_room);
        recycler_room.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new StudyRoomRecyclerAdapter(this,studyRoomRepository);
        recycler_room.setAdapter(adapter);
        edit_text_key=findViewById(R.id.edit_text_key);
        text_key=findViewById(R.id.text_key);

    }
}
