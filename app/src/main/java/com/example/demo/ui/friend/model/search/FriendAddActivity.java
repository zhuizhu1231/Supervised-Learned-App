package com.example.demo.ui.friend.model.search;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.R;
import com.example.demo.data.model.Friend;
import com.example.demo.data.repository.FriendRepository;
import com.example.demo.net.json.Bean;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;

import java.util.ArrayList;
import java.util.List;

public class FriendAddActivity extends AppCompatActivity {

    TextView label,user_name,user_number,user_sex,user_sign,user_register_time,friend_add;
    EditText edit_remark;
    public static int FRIEND_ADD=1;
    public static int FRIEND_DETAIL=2;
    int type;
    Friend friend;
    FriendRepository friendRepository;
    Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtil.QUERY_FRIEND_ID_SUCCESS:
                    ArrayList<Friend> list= (ArrayList<Friend>) com.example.demo.data.entities.Friend.unpack((List<Bean< com.example.demo.data.entities.Friend >>)msg.obj);
                    if(list!=null&&list.size()>0) {
                        Integer id = null;
                        if(friend.getId()!=null) id=friend.getId();
                        friend=list.get(0);
                        if(id!=null)
                        friend.setId(id);
                        setData();
                        friendRepository.netUpdateFriendBack(friend);
                    }
                    break;
                case StringUtil.QUERY_FRIEND_ID_FAIL:
                    Toast.makeText(FriendAddActivity.this,R.string.fail_request,Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_add);
        friendRepository=FriendRepository.getInstance(this.getApplication());
        getData();
        initView();
        friendRepository.netQueryServiceFriendByFriendDbId(handler,friend.getFriendId());
        addFriendListener();
    }

    private void addFriendListener() {
        if(type==FRIEND_ADD)
        friend_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo:添加好友  联网同步
                if(Tool.testStringNotNULL(edit_remark.getText().toString()))
                     friend.setRemark(edit_remark.getText().toString());
                friendRepository.addFriend(friend);
                finish();
            }
        });
        else
            friend_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo:删除好友  联网同步（bug）
                    Tool.alertDialogShow(FriendAddActivity.this, "是否删除好友"+friend.getFriendId(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            friendRepository.statusDeleteFriend(friend);
                            dialog.dismiss();
                            Intent intent=new Intent();
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });

               }
            });
    }

    private void getData() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", FRIEND_ADD);
        friend = (Friend) intent.getSerializableExtra("friend");
    }

    private void initView() {
        Friend friendByFriendId = friendRepository.getFriendByFriendId(friend.getFriendId());
        if(friendByFriendId !=null&&type==FRIEND_ADD) {
            type=FRIEND_DETAIL;
            Tool.alertDialogShow(FriendAddActivity.this, "已是好友，不必重复添加", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        friend_add=findViewById(R.id.friend_add);
        label=findViewById(R.id.label);
        if(type==FRIEND_ADD||friendByFriendId==null) label.setText(getString(R.string.text_add_friend));
        else {
            label.setText(getString(R.string.text_user_detail));
            if(friendByFriendId !=null)
                friend_add.setText("删除好友");
            else
                friend_add.setVisibility(View.GONE);
         //
        }
        user_name=findViewById(R.id.user_name);
        user_number=findViewById(R.id.user_number);

        user_sex=findViewById(R.id.user_sex);

        user_sign=findViewById(R.id.user_sign);

        user_register_time=findViewById(R.id.user_register_time);

        edit_remark=findViewById(R.id.edit_remark);
        setData();
    }

    private void setData(){
        user_name.setText(friend.getName());
        user_number.setText(""+friend.getFriendId());
        if(friend.getSex()!=null)
        user_sex.setText(friend.getSex()==StringUtil.SEX_MAN?R.string.man:R.string.woman);
        user_sign.setText(friend.getSign());
        user_register_time.setText(TimeUtils.DatePickerDateConverter.timeFormat(friend.getRegisterTime()));
        edit_remark.setText(friend.getRemark());
    }
}
