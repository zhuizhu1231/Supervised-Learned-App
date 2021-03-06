package com.example.demo.ui.friend;

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
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.demo.R;
import com.example.demo.adapter.FriendRecyclerAdapter;
import com.example.demo.data.model.Friend;
import com.example.demo.data.model.Message;
import com.example.demo.net.json.Bean;
import com.example.demo.ui.friend.model.friend.NoFriendActivity;
import com.example.demo.ui.friend.model.search.FriendSearchActivity;
import com.example.demo.util.StringUtil;
import com.example.demo.websocket.JWebSocketClient;
import com.example.demo.websocket.JWebSocketClientFriendService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class FriendActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FriendRecyclerAdapter adapter;
    private JWebSocketClient client;
    private JWebSocketClientFriendService.JWebSocketClientBinder binder;
    private JWebSocketClientFriendService jWebSClientService;
    private MessageReceiver chatMessageReceiver;
    ConstraintLayout constraintLayout;
    FriendViewModel friendViewModel;
    Intent intent;

    ImageButton friend_search_button;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtil.MESSAGE_QUERY_LIST_SUCCESS:
                    friendViewModel.cacheMessageListData(((List<Bean<com.example.demo.data.entities.Message>>)msg.obj));
                    break;

                case StringUtil.MESSAGE_QUERY_LIST_FAIL:
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        androidx.appcompat.widget.Toolbar toolbar=(androidx.appcompat.widget.Toolbar) findViewById(R.id.friend_toolbar);
        setSupportActionBar(toolbar);
        friendViewModel= ViewModelProviders.of(this).get(FriendViewModel.class);
        //TODO:联网读取friend信息 同步 直接显示在recyclerView 读取数据库  失败则读取数据库
        friendViewModel.netQueryMessageList(handler);

        startJWebSClientService();
        //绑定服务
        bindService();
        //注册广播
        doRegisterReceiver();
        //检测通知是否开启
        checkNotification(FriendActivity.this);

        initView();

    }
    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
         intent = new Intent(FriendActivity.this, JWebSocketClientFriendService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0以上通过startForegroundService启动service
            startForegroundService(intent);
        } else {
            startService(intent);
        }

      //  startService(intent);
    }
    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(FriendActivity.this, JWebSocketClientFriendService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }
    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter(getString(R.string.friend_receiver));
        registerReceiver(chatMessageReceiver, filter);
    }
    private void initView() {
        recyclerView=findViewById(R.id.recycler_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new FriendRecyclerAdapter(this,friendViewModel.friendRepository);
        recyclerView.setAdapter(adapter);
        friendViewModel.getFriendList().observe(this, new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friends) {
                adapter.setAllFriends(friends);
                adapter.notifyDataSetChanged();
            }
        });
        constraintLayout=findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FriendActivity.this, NoFriendActivity.class);
                startActivity(intent);
            }
        });
        friend_search_button=findViewById(R.id.friend_search_button);
        friend_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FriendActivity.this, FriendSearchActivity.class);
                startActivity(intent);
            }
        });

    }
    private class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String content=intent.getStringExtra("message");
            Message message = com.example.demo.data.entities.Message.toModel(JSON.parseObject(content, com.example.demo.data.entities.Message.class));
            friendViewModel.saveMessage(message);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("FriendActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientFriendService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            client = jWebSClientService.client;
            StringUtil.setClient(client);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("FriendActivity", "服务与活动成功断开");
        }


    };




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
       // client.onClose();
        unregisterReceiver(chatMessageReceiver);
        unbindService(serviceConnection);
        stopService(intent);
    }
}



