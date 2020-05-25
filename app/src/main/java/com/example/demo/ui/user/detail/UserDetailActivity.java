package com.example.demo.ui.user.detail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.demo.R;
import com.example.demo.data.model.User;
import com.example.demo.data.repository.DiaryRepository;
import com.example.demo.data.repository.FriendRepository;
import com.example.demo.data.repository.MilepostRepository;
import com.example.demo.data.repository.NotesRepository;
import com.example.demo.data.repository.StudyRoomRepository;
import com.example.demo.data.repository.TargetRepository;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.dialog.UserStatusDialog;
import com.example.demo.service.OverrideDataIntentService;
import com.example.demo.ui.user.login.LoginActivity;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

public class UserDetailActivity extends AppCompatActivity {

    UserDetailViewModel userDetailViewModel;
    NotesRepository notesRepository;
    DiaryRepository diaryRepository;
    MilepostRepository milepostRepository;
    TargetRepository targetRepository;
    UserRepository userRepository;
    FriendRepository friendRepository;
    StudyRoomRepository studyRoomRepository;
    public Handler handler=new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case StringUtil.FAIL_REQUEST:
                    Toast.makeText(getApplicationContext(), getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;
                case StringUtil.ONLINE:
                    User result = (User) msg.obj;
                    startUpdateService();
                    StringUtil.setUser(result);
                    userRepository.netUpdateUserBack(result);
                    String welcome = getString(R.string.welcome) + result.getName();
                    Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                    break;
                case StringUtil.OFFLINE:
                    Toast.makeText(getApplicationContext(),  msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    clearAllCacheData();
                    Tool.alertDialogShow(UserDetailActivity.this,"重新登录", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(UserDetailActivity.this, LoginActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        }
                    });
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        androidx.appcompat.widget.Toolbar toolbar= findViewById(R.id.user_detail_toolbar);
        setSupportActionBar(toolbar);
        userDetailViewModel= ViewModelProviders.of(this).get(UserDetailViewModel.class);

        notesRepository=NotesRepository.getInstance(this.getApplication());
        diaryRepository=DiaryRepository.getInstance(this.getApplication());
        milepostRepository=MilepostRepository.getInstance(this.getApplication());
        targetRepository=TargetRepository.getInstance(this);
        friendRepository=FriendRepository.getInstance(this.getApplication());
        studyRoomRepository=StudyRoomRepository.getInstance(this.getApplication());
        userRepository=UserRepository.getInstance(this.getApplication());
        TextView user_last_login_time=findViewById(R.id.user_last_login_time);
        TextView user_register_time=findViewById(R.id.user_register_time);
        TextView user_rest_time=findViewById(R.id.user_rest_time);
        TextView user_work_time=findViewById(R.id.user_work_time);
        TextView user_name=findViewById(R.id.user_name);
        TextView user_sex=findViewById(R.id.user_sex);
        TextView user_sign=findViewById(R.id.user_sign);
        TextView user_number=findViewById(R.id.user_number);
        ConstraintLayout toEdit=findViewById(R.id.toEdit);
        toEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDetailActivity.this,EditSelfActivity.class));
            }
        });
        userDetailViewModel.getUser().observe(this, new Observer<User>() {
             @Override
             public void onChanged(User user) {
                 if(user!=null) {
                     user_name.setText(user.getName());
                     user_number.setText("" + user.getDbId());
                     user_sex.setText(user.getSex() == StringUtil.SEX_MAN ? getString(R.string.man) : getString(R.string.woman));
                     user_sign.setText(user.getSign());
                     user_last_login_time.setText(userDetailViewModel.DateConverter(user.getLastLoginTime()));
                     user_register_time.setText(userDetailViewModel.DateConverter(user.getRegisterTime()));
                     user_rest_time.setText(userDetailViewModel.MinusConverter(user.getRestTime()));
                     user_work_time.setText(userDetailViewModel.MinusConverter(user.getWorkTime()));
                 }
             }
         });
        initView();

    }

    private void initView() {
        TextView user_status=findViewById(R.id.user_status);
        if(StringUtil.getSessionId()==null)
            user_status.setText(getString(R.string.offline));
        else  user_status.setText(getString(R.string.online));
        user_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserStatusDialog dialog=new UserStatusDialog(UserDetailActivity.this);
                dialog.show();
                dialog.setCloseListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        setResult(RESULT_OK,intent);
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.setLogoutListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clearAllCacheData();
                        StringUtil.setSessionId(null);
                        StringUtil.setUser(null);
                        Intent intent=new Intent();
                        setResult(RESULT_OK,intent);
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.setLoginListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        User u = userRepository.findUser();
                        if (u != null) {
                            StringUtil.setUser(u);
                            userRepository.quickLogin(u.getDbId(), u.getPassword(), handler);
                        }
                    }
                });
            }
        });
    }


    private void clearAllCacheData() {
        diaryRepository.clearCache();
        milepostRepository.clearCache();
        notesRepository.clearCache();
        targetRepository.clearCache();
        friendRepository.clearCache();
        studyRoomRepository.clearCache();
        userRepository.clearCache();
    }

    private void startUpdateService(){
    Intent updateServiceIntent=new Intent(UserDetailActivity.this, OverrideDataIntentService.class);
    startService(updateServiceIntent);
    }

}
