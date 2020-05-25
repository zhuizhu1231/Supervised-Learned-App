package com.example.demo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.demo.R;
import com.example.demo.data.model.Task;
import com.example.demo.receiver.LockScreenReceiver;
import com.example.demo.ui.close.DetailActivity;
import com.example.demo.util.StringUtil;


public class LockScreenService extends Service {
    private LockScreenReceiver mReceiver;
    private IntentFilter mIntentFilter = new IntentFilter();
    private boolean isNotiShow = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //动态注册
        mIntentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);
        mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        mIntentFilter.addAction(Intent.ACTION_TIME_TICK);

        mIntentFilter.setPriority(Integer.MAX_VALUE);
        if (null == mReceiver) {
            mReceiver = new LockScreenReceiver();
            mReceiver.setTask((Task) intent.getSerializableExtra("task"));//
            mIntentFilter.setPriority(Integer.MAX_VALUE);
            registerReceiver(mReceiver, mIntentFilter);
            buildNotification();
            Toast.makeText(getApplicationContext(), "开启成功", Toast.LENGTH_LONG).show();
        }

        return START_STICKY;
    }

    /**
     * 通知栏显示
     */
    private void buildNotification() {
        if (!isNotiShow){ //避免多次显示
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent intent = new Intent(this, DetailActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(this)
                    .setTicker("APP正在运行")
                    .setAutoCancel(false)
                    .setContentTitle("APP正在运行")
                    .setContentText("运行中")
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    ;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                //修改安卓8.1以上系统报错
                NotificationChannel notificationChannel = new NotificationChannel("CHANNEL_ONE_ID",
                        "CHANNEL_ONE_NAME",NotificationManager.IMPORTANCE_MIN);
                notificationChannel.enableLights(false);//如果使用中的设备支持通知灯，则说明此通知通道是否应显示灯
                notificationChannel.setShowBadge(false);//是否显示角标
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

                manager.createNotificationChannel(notificationChannel);
                builder.setChannelId("CHANNEL_ONE_ID");
            }


            Notification notification = builder.build(); // 获取构建好的Notification
            notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
            manager.notify(StringUtil.Notification_ID_Lock_notice, notification);

            startForeground(0x11, notification);


            isNotiShow = true;
        }
    }

    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        super.onDestroy();
    }
}