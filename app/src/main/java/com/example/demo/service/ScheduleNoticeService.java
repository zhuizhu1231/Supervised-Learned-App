package com.example.demo.service;

import android.app.IntentService;
import android.content.Intent;

import com.example.demo.converter.MyConverter;
import com.example.demo.data.datasource.DiaryDataSource;
import com.example.demo.data.model.Schedule;
import com.example.demo.data.repository.DiaryRepository;
import com.example.demo.receiver.ScheduleNoticeReceiver;
import com.example.demo.util.AlarmManagerUtil;
import com.example.demo.util.StringUtil;

import java.util.Date;
import java.util.List;



public class ScheduleNoticeService extends IntentService {
    DiaryRepository diaryRepository;

    public ScheduleNoticeService() {
        super("ScheduleNoticeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        diaryRepository=DiaryRepository.getInstance(new DiaryDataSource());
        diaryRepository.setCacheDataSource(this.getApplication());
        List<Schedule> alarmScheduleList = diaryRepository.getDayAlarmScheduleListByTimestampAfterStatic(new Date());
        for(Schedule schedule:alarmScheduleList){
           if(schedule.getProperty()== StringUtil.CLOCK_SET) {
                Intent scheduleNotice = new Intent(this, ScheduleNoticeReceiver.class);
                scheduleNotice.putExtra("schedule", schedule);
                scheduleNotice.putExtra("id", schedule.getId());
                AlarmManagerUtil.setAlarmTime(this, MyConverter.timeStampToDate(schedule.getBelongTime()).getTime(), scheduleNotice);
            }
        }
    }


}
