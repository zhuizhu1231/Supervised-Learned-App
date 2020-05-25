package com.example.demo.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.example.demo.R;
import com.example.demo.data.datasource.MilepostDataSource;
import com.example.demo.data.model.Milepost;
import com.example.demo.data.repository.MilepostRepository;
import com.example.demo.ui.milepost.MilepostNoticeActivity;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 *
 */
public class MilepostNoticeService extends IntentService {
    private boolean isNoticeShow = false;
    MilepostRepository milepostRepository;
    TimeUtils.DatePickerDateConverter converter=new TimeUtils.DatePickerDateConverter();
    public MilepostNoticeService() {
        super("MilepostNoticeService");
    }
    String text;

    @Override
    protected void onHandleIntent(Intent intent) {
        buildNotification();
    }
    private void buildNotification() {
        if (!isNoticeShow){ //避免多次显示


            milepostRepository=MilepostRepository.getInstance(new MilepostDataSource());
            milepostRepository.setCacheDataSource(this);
            Milepost liveMilepostLately = milepostRepository.getLiveMilepostLately();
            Milepost m=liveMilepostLately;
             text="距离"+m.getTitle()+"截至日期"+converter.timestampToDateFormat(m.getDieTime())
                    +"，还剩"+TimeUtils.timeStampIntervalParseToDay(m.getDieTime(), Tool.createNewTimeStamp())+"天";
//            milepostRepository.getLiveMilepostList().observe((LifecycleOwner) getApplicationContext(), new Observer<List<Milepost>>() {
//                @Override
//                public void onChanged(List<Milepost> mileposts) {
//                    Milepost milepost = mileposts.get(0);
//                    Timestamp newTimeStamp = Tool.createNewTimeStamp();
//                    int day=TimeUtils.timeStampIntervalParseToDay(milepost.getDieTime(),newTimeStamp);
//
//                    text="里程碑消息提示：距离"+m.getTitle()+"截至日期"+converter.timestampToDateFormat(m.getDieTime())
//                            +"，还剩"+TimeUtils.timeStampIntervalParseToDay(m.getDieTime(), Tool.createNewTimeStamp())+"天";
//                }
//            });
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent intent = new Intent(this, MilepostNoticeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(this)
                    .setTicker("APP正在运行")
                    .setAutoCancel(false)
                    .setContentTitle("里程碑消息提示")
                    .setContentText(text)
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
            manager.notify(StringUtil.Notification_ID_Milepost_notice, notification);

            startForeground(0x11, notification);


            isNoticeShow = true;
        }
    }
}
