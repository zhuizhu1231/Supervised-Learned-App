package com.example.demo.ui.group.study;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.example.demo.R;
import com.example.demo.data.model.Study_room;
import com.example.demo.data.model.Study_room_message;
import com.example.demo.ui.group.other.RoomCreateActivity;
import com.example.demo.ui.group.study.ui.main.SectionsPagerAdapter;
import com.example.demo.util.StringUtil;
import com.example.demo.websocket.JWebSocketClient;
import com.example.demo.websocket.JWebSocketClientStudyRoomService;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.example.demo.ui.group.other.RoomCreateActivity.TYPE_DETAIL;

public class StudyroomActivity extends AppCompatActivity {

    private JWebSocketClient client;
    private JWebSocketClientStudyRoomService.JWebSocketClientBinder binder;
    private JWebSocketClientStudyRoomService jWebSClientService;
    private MessageReceiver chatMessageReceiver;
    private StudyRoomViewModel studyRoomViewModel;
    Study_room room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom);
        studyRoomViewModel= ViewModelProviders.of(this).get(StudyRoomViewModel.class);
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.study_room_toolbar);
        setSupportActionBar(toolbar);
        getData();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        startJWebSClientService();
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
        //检测通知是否开启
        checkNotification(StudyroomActivity.this);


    }

    private void getData() {
        Intent intent = getIntent();
        room= (Study_room) intent.getSerializableExtra("room");
    }

    private void doRegisterReceiver() {
        chatMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter(getString(R.string.friend_receiver));
        filter.addAction(getString(R.string.study_room_receiver));
        registerReceiver(chatMessageReceiver, filter);
    }
    private void bindService() {
        Intent bindIntent = new Intent(StudyroomActivity.this, JWebSocketClientStudyRoomService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }
    
    private void startJWebSClientService() {
        Intent intent = new Intent(StudyroomActivity.this, JWebSocketClientStudyRoomService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0以上通过startForegroundService启动service
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        //  startService(intent);
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("StudyroomActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientStudyRoomService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
            StringUtil.setRoomClient(client);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("StudyroomActivity", "服务与活动成功断开");
        }


    };
    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String content=intent.getStringExtra("message");
            Study_room_message message = com.example.demo.data.entities.Study_room_message.toModel(JSON.parseObject(content, com.example.demo.data.entities.Study_room_message.class));
            studyRoomViewModel.saveMessage(message);
        }
    }
    /**
     * 检测是否开启通知
     *
     * @param context
     */
    private void checkNotification(final Context context) {
        if (!isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("温馨提示")
                    .setMessage("你还未开启系统通知，将影响消息的接收，要去开启吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(context);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
    }
    /**
     * 如果没有开启通知，跳转至设置界面
     *
     * @param context
     */
    private void setNotification(Context context) {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(localIntent);
    }

    /**
     * 获取通知权限,监测是否开启了系统通知
     *
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.onClose(1,"",true);
        unregisterReceiver(chatMessageReceiver);
        unbindService(serviceConnection);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==StringUtil.Return&&resultCode==RESULT_OK){
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_detail:
                Intent intent=new Intent(this, RoomCreateActivity.class);
                intent.putExtra("room",room);
                intent.putExtra("type",TYPE_DETAIL);
                startActivityForResult(intent,StringUtil.Return);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}