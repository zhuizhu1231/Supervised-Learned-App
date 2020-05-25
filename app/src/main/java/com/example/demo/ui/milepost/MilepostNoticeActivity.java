package com.example.demo.ui.milepost;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.demo.R;
import com.example.demo.data.model.Milepost;
import com.example.demo.data.repository.MilepostRepository;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;

import java.sql.Timestamp;
import java.util.List;

public class MilepostNoticeActivity extends AppCompatActivity {
    TextView milepost_title,milepost_rest_day,milepost_die_time,text_milepost_remark;
    MilepostRepository milepostRepository;
    TimeUtils.DatePickerDateConverter converter=new TimeUtils.DatePickerDateConverter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milepost_notice);
        milepost_title=findViewById(R.id.milepost_title);
        milepost_rest_day=findViewById(R.id.milepost_rest_day);
        milepost_die_time=findViewById(R.id.milepost_die_time);
        text_milepost_remark=findViewById(R.id.text_milepost_remark);
        milepostRepository=MilepostRepository.getInstance(this.getApplication());
        Milepost liveMilepostLately = milepostRepository.getLiveMilepostLately();
        Milepost m=liveMilepostLately;
        milepost_title.setText(m.getTitle());
        milepost_die_time.setText(converter.timestampToDateFormat(m.getDieTime()));
        milepost_rest_day.setText(""+TimeUtils.timeStampIntervalParseToDay(m.getDieTime(),Tool.createNewTimeStamp()));
        text_milepost_remark.setText(m.getRemark());
        milepostRepository.getLiveMilepostList().observe(this, new Observer<List<Milepost>>() {
            @Override
            public void onChanged(List<Milepost> mileposts) {
                Milepost milepost = mileposts.get(0);
                Timestamp newTimeStamp = Tool.createNewTimeStamp();
                int day=TimeUtils.timeStampIntervalParseToDay(milepost.getDieTime(),newTimeStamp);
                milepost_title.setText(milepost.getTitle());
                milepost_die_time.setText(converter.timestampToDateFormat(milepost.getDieTime()));
                milepost_rest_day.setText(""+day);
                text_milepost_remark.setText(m.getRemark());
            }
        });
    }
}
