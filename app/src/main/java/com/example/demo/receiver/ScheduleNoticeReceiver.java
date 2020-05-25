package com.example.demo.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.example.demo.R;
import com.example.demo.ui.diary.model.ScheduleAlarm;
import com.example.demo.ui.diary.model.ScheduleNoticeActivity;
import com.example.demo.util.StringUtil;


public class ScheduleNoticeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //showMemo(context);

        Log.d("闹钟响了","响了");
        showNotice(context);
        showAlarm(context);

    }

    private void showAlarm(Context context) {
        Intent intent=new Intent(context, ScheduleAlarm.class);
        context.startActivity(intent);
    }


    private void showNotice(Context context) {


        Intent intent=new Intent(context, ScheduleNoticeActivity.class);

        PendingIntent pi= PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Notification.Builder builder=new Notification.Builder(context)
                .setTicker("APP正在运行")
                .setContentTitle("日程消息提示：")
                .setContentText(context.getString(R.string.schedule)+"已开始，请尽快完成!")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .setAutoCancel(false)
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(record.getMainText()))
                .setLights(Color.GREEN,1000,1000)
                .setContentIntent(pi);


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
        manager.notify(StringUtil.Notification_ID_Schedule_notice,notification);
    }
//
//    private void deleteTheAlarm(int num) {
//        ContentValues temp = new ContentValues();
//        temp.put("alarm_key", "");
//        String where = String.valueOf(num);
//        DataSupport.updateAll(Note_table.class, temp, "id = ?", where);
//    }
//
//    private void transportInformationToEdit(Intent it, Note_table note_table) {
//
//        it.putExtra("content",note_table.getContent());
//        it.putExtra("color_key",note_table.getColor_key());
//        it.putExtra("time",note_table.getTime());
//
//    }

}
