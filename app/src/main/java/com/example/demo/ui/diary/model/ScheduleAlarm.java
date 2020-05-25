package com.example.demo.ui.diary.model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.R;

public class ScheduleAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.clockmusic2);
        mediaPlayer.start();
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_access_alarm_black_24dp)
                .setTitle("日程消息提醒：")
                .setCancelable(false)
                .setMessage("您设定的日程时间已到，请尽快完成！")
                .setPositiveButton("关掉"
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                mediaPlayer.stop();
                            }
                        }).show();
    }
}
