package com.example.demo.ui.group.other;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.demo.R;
import com.example.demo.data.model.Study_room;
import com.example.demo.data.repository.StudyRoomRepository;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;

import java.util.List;

public class RoomCreateActivity extends AppCompatActivity {

    StudyRoomRepository studyRoomRepository;
    public static int TYPE_ADD=1;
    public static int TYPE_DETAIL=2;
    TextView text_room_name,text_room_time,text_room_remark,text_create_time;
    EditText edit_text_room_name,edit_text_room_time,edit_text_room_remark;
    Button button_room_create,button_room_exit;
    Study_room room;
    ConstraintLayout room_create_time;
    int type;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtil.ROOM_UPDATE_SUCCESS:
                    cache(msg);
                    Tool.alertDialogShow(RoomCreateActivity.this,"自习室更新成功", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    break;
                case StringUtil.QUERY_ROOM_ID_SUCCESS:
                    cache(msg);
                    break;
                case StringUtil.ROOM_CREATE_SUCCESS:
                    cache(msg);
                    Tool.alertDialogShow(RoomCreateActivity.this,"自习室创建成功", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    break;
                case StringUtil.ROOM_DELETE_SUCCESS:
                    Tool.alertDialogShow(RoomCreateActivity.this,"自习室已删除", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            studyRoomRepository.removeRoom(room);
                            dialog.dismiss();
                            Intent intent=new Intent();
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
                    break;
                case StringUtil.ROOM_UPDATE_FAIL:
                case StringUtil.ROOM_CREATE_FAIL:
                case StringUtil.ROOM_DELETE_FAIL:
                case StringUtil.QUERY_ROOM_ID_FAIL:
                    break;

            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studyRoomRepository=StudyRoomRepository.getInstance(this.getApplication());
        setContentView(R.layout.activity_room_create);
        initView();
        getData();
        buttonListener();
    }
    private void cache(Message msg){
        studyRoomRepository.cacheRoomListData((List<Bean<com.example.demo.data.entities.Study_room>>) msg.obj);

    }

    private void buttonListener() {
        if(type==TYPE_ADD){
            button_room_create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = edit_text_room_name.getText().toString();
                    if(Tool.testStringNotNULL(name)) {
                        Study_room studyRoom = new Study_room();
                        studyRoom.setName(name);
                        studyRoom.setUserCreateId(StringUtil.getUser().getDbId());
                        studyRoom.setUserCount(1);
                        studyRoom.setDetail(edit_text_room_remark.getText().toString());
                        String time = edit_text_room_time.getText().toString();
                        if(Tool.testStringNotNULL(time))
                        studyRoom.setLeastWorkTime(TimeUtils.minusParseToTimeStamp(Integer.valueOf(time)));
                        else studyRoom.setLeastWorkTime(TimeUtils.minusParseToTimeStamp(0));
                        studyRoomRepository.netSaveRoom(handler,studyRoom);
                    }
                }
            });
        }else{
            button_room_create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = edit_text_room_name.getText().toString();
                    if(Tool.testStringNotNULL(name)) {
                        room.setName(name);
                        String time = edit_text_room_time.getText().toString();
                        if(Tool.testStringNotNULL(time))
                            room.setLeastWorkTime(TimeUtils.minusParseToTimeStamp(Integer.valueOf(time)));
                        else room.setLeastWorkTime(TimeUtils.minusParseToTimeStamp(0));
                        room.setDetail(edit_text_room_remark.getText().toString());
                        studyRoomRepository.netUpdateRoom(handler,room);
                    }
                }
            });
        }
    }


    private void getData() {
        Intent intent = getIntent();
        room = (Study_room) intent.getSerializableExtra("room");
        type=intent.getIntExtra("type",0);
        if(type==TYPE_ADD){
            room_create_time.setVisibility(View.GONE);
            button_room_exit.setVisibility(View.GONE);
            setTextGone();
        }else if(room!=null){
            studyRoomRepository.netRefleshRoomByDbId(handler,room.getDbId());
            text_create_time.setText(TimeUtils.DatePickerDateConverter.timeFormat(room.getCreateTime()));
            if(room.getUserCreateId()== StringUtil.getUser().getDbId().intValue())
            {
                setTextGone();
                button_room_create.setText("保存修改");
                button_room_exit.setText("删除自习室");
                setEditText();
                button_room_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tool.alertDialogShow(RoomCreateActivity.this, "是否确认删除该自习室", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                studyRoomRepository.netDeleteRoom(handler,room);
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
            else {
                setEditTextGone();
                setText();
                button_room_create.setVisibility(View.GONE);
            }
        }else {finish();}
    }

    private void setText() {
        text_room_name.setText(room.getName());
        text_room_remark.setText(room.getDetail());
        text_room_time.setText(""+TimeUtils.timeStampParseToMinus(room.getLeastWorkTime()));
    }

    private void setEditText() {
        edit_text_room_name.setText(room.getName());
        edit_text_room_remark.setText(room.getDetail());
        edit_text_room_time.setText(""+TimeUtils.timeStampParseToMinus(room.getLeastWorkTime()));
    }

    private  void setEditTextGone(){
        edit_text_room_name.setVisibility(View.GONE);
        edit_text_room_remark.setVisibility(View.GONE);
        edit_text_room_time.setVisibility(View.GONE);
    }

    private  void setTextGone(){
        text_room_name.setVisibility(View.GONE);
        text_room_remark.setVisibility(View.GONE);
        text_room_time.setVisibility(View.GONE);
    }

    private void initView() {
        text_room_name=findViewById(R.id.text_room_name);
        text_room_time=findViewById(R.id.text_room_time);
        text_room_remark=findViewById(R.id.text_room_remark);
        edit_text_room_name=findViewById(R.id.edit_text_room_name);
        edit_text_room_time=findViewById(R.id.edit_text_room_time);
        edit_text_room_remark=findViewById(R.id.edit_text_room_remark);
        button_room_create=findViewById(R.id.button_room_create);
        room_create_time=findViewById(R.id.room_create_time);
        text_create_time=findViewById(R.id.text_create_time);
        button_room_exit=findViewById(R.id.button_room_exit);
    }
}
