package com.example.demo.ui.diary.model.create;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.DialogScheduleLabelRecyclerAdapter;
import com.example.demo.adapter.ScheduleLabelRecyclerAdapter;
import com.example.demo.converter.MyConverter;
import com.example.demo.data.model.Schedule;
import com.example.demo.data.model.Schedule_in_label;
import com.example.demo.data.model.Schedule_label;
import com.example.demo.dialog.LabelAddDialog;
import com.example.demo.dialog.RemarkEditDialog;
import com.example.demo.receiver.ScheduleNoticeReceiver;
import com.example.demo.util.AlarmManagerUtil;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleEditActivity extends AppCompatActivity {
    ScheduleEditViewModel scheduleEditViewModel;
    int activity_status;
    private Schedule schedule;
    private Calendar calendar;
    EditText edit_text_schedule_title;
    TextView schedule_belong_time,schedule_clock,old_clock,schedule_date;
    RecyclerView recycler_schedule_label;
    ScheduleLabelRecyclerAdapter scheduleLabelRecyclerAdapter;
    List<Schedule_label>initScheduleLabelList;
    CheckBox check_schedule_done;
    RadioGroup import_urgent_degree;
    RadioButton import_urgent,no_import_urgent,import_no_urgent,no_import_no_urgent;
    TimePickerDialog timePickerDialog;
    Boolean isClock;
    LabelAddDialog tagDialog;
    RemarkEditDialog remarkEditDialog;
    Calendar initCalendar;
    ImageView image_clock,image_time,image_action_tag,image_action_remark,image_action_delete,image_action_sure;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case StringUtil.SCHEDULE_ADD_LABEL_DIALOG_MESSAGE:
                    scheduleLabelRecyclerAdapter.addOrRemoveScheduleLabel((Schedule_label) msg.obj);
                    scheduleLabelRecyclerAdapter.notifyDataSetChanged();
                    tagDialog.dismiss();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);
        initView();
        initData();
        getData();
        setClockListener();
        setTimeListener();
        setTagListener();
        setRemarkListener();
        sureAction();
        setDeleteListener();
        setRadioDroupCheckChangeListener();


    }

    private void setRadioDroupCheckChangeListener() {
        import_urgent_degree.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonTextSizeChange(checkedId);
            }
        });
    }

    private void radioButtonTextSizeChange(int checkedId){
        if( import_urgent.getId()==checkedId)  import_urgent.setTextSize(Dimension.DP,20);
        else import_urgent.setTextSize(Dimension.DP,0);
        if( import_no_urgent.getId()==checkedId)  import_no_urgent.setTextSize(Dimension.DP,20);
        else import_no_urgent.setTextSize(Dimension.DP,0);
        if( no_import_urgent.getId()==checkedId)  no_import_urgent.setTextSize(Dimension.DP,20);
        else no_import_urgent.setTextSize(Dimension.DP,0);
        if( no_import_no_urgent.getId()==checkedId)  no_import_no_urgent.setTextSize(Dimension.DP,20);
        else no_import_no_urgent.setTextSize(Dimension.DP,0);
    }

    private void setDeleteListener() {
        image_action_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Schedule_label label:initScheduleLabelList){
                    scheduleEditViewModel.statusDeleteRelationByScheduleIdLabelIdAsync(schedule.getId(),label.getId());
                }
                scheduleEditViewModel.statusDeleteScheduleAsync(schedule);
                finish();
            }
        });
    }


    private void sureAction() {
        image_action_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timestamp clock_time = MyConverter.dateToTimeStamp(calendar.getTime());
                String title = edit_text_schedule_title.getText().toString();
                List<Schedule_label> allSchedule_labels = scheduleLabelRecyclerAdapter.getAllSchedule_labels();
                if(activity_status==StringUtil.INSERT_STATUS){
                    if(Tool.testStringNotNULL(title)){
                        schedule=new Schedule();
                        schedule.setTitle(title);
                        schedule.setCategory(switchImportUrgentRadioButton()==null? null:switchImportUrgentRadioButton());
                        schedule.setRemark(remarkEditDialog.getRemark());
                        schedule.setBelongTime(clock_time);
                        schedule.setProperty(isClock? StringUtil.CLOCK_SET:StringUtil.CLOCK_NO_SET);
                        Long scheduleId=scheduleEditViewModel.saveSchedule(schedule);
                        schedule.setId(scheduleId.intValue());

                        for(Schedule_label label:allSchedule_labels){
                            Schedule_in_label relation = new Schedule_in_label();
                            relation.setScheduleLabelId(label.getId());
                            relation.setScheduleId(scheduleId.intValue());
                            scheduleEditViewModel.insertRelationAsync(relation);//repository中会更新label count数
                        }
                        if(clock_time.after(Tool.createNewTimeStamp())){
                            Intent scheduleNotice=new Intent(ScheduleEditActivity.this, ScheduleNoticeReceiver.class);
                            scheduleNotice.putExtra("schedule",schedule);
                            scheduleNotice.putExtra("id",schedule.getId());
                            AlarmManagerUtil.setAlarmTime(ScheduleEditActivity.this,calendar.getTimeInMillis(),scheduleNotice);
                            // PendingIntent sender = PendingIntent.getBroadcast(ScheduleEditActivity.this,schedule.getId(),scheduleNotice, 0);
                        }
                    }

                    finish();
                }else {
                    if(Tool.testStringNotNULL(title)){
                        schedule.setTitle(title);
                        schedule.setCategory(switchImportUrgentRadioButton()==null? null:switchImportUrgentRadioButton());
                        schedule.setRemark(remarkEditDialog.getRemark());
                        schedule.setBelongTime(clock_time);
                        schedule.setIsDone(check_schedule_done.isChecked()?StringUtil.DONE:StringUtil.NOT_DONE);
                        schedule.setProperty(isClock? StringUtil.CLOCK_SET:StringUtil.CLOCK_NO_SET);
                        scheduleEditViewModel.updateSchedule(schedule);
                      // List<Schedule_label> allSchedule_labels = scheduleLabelRecyclerAdapter.getAllSchedule_labels();
                        for(int i=0;i<initScheduleLabelList.size();i++){
                            Schedule_label label=initScheduleLabelList.get(i);
                            if(allSchedule_labels.contains(label)) {
                                allSchedule_labels.remove(label);
                                initScheduleLabelList.remove(label);
                                i--;
                            }
                        }
                        for(Schedule_label label:initScheduleLabelList){
                            scheduleEditViewModel.statusDeleteRelationByScheduleIdLabelIdAsync(schedule.getId(),label.getId());//repository中会更新label count数
                        }
                        for(Schedule_label label:allSchedule_labels){
                            Schedule_in_label relation = new Schedule_in_label();
                            relation.setScheduleLabelId(label.getId());
                            relation.setScheduleId(schedule.getId());
                            scheduleEditViewModel.insertRelationAsync(relation);//repository中会更新label count数
                        }
                        if(initCalendar.getTime().after(MyConverter.timeStampToDate(Tool.createNewTimeStamp()))){
                            Intent scheduleNotice=new Intent(ScheduleEditActivity.this, ScheduleNoticeReceiver.class);
                            scheduleNotice.putExtra("id",schedule.getId());
                            AlarmManagerUtil.cancelAlarm(ScheduleEditActivity.this,scheduleNotice);
                        }
                        if(clock_time.after(Tool.createNewTimeStamp())){
                            Intent scheduleNotice=new Intent(ScheduleEditActivity.this, ScheduleNoticeReceiver.class);
                            scheduleNotice.putExtra("schedule",schedule);
                            scheduleNotice.putExtra("id",schedule.getId());
                            AlarmManagerUtil.setAlarmTime(ScheduleEditActivity.this,calendar.getTimeInMillis(),scheduleNotice);
                            // PendingIntent sender = PendingIntent.getBroadcast(ScheduleEditActivity.this,schedule.getId(),scheduleNotice, 0);
                        }
                    }

                    finish();

                }
            }
        });
    }

    private void setRemarkListener() {
        image_action_remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarkEditDialog.show();
            }
        });
    }

    private void setTagListener() {
        image_action_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagDialog.show();
            }
        });
    }


    private void setClockListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScheduleClockChange();
            }
        };
        image_clock.setOnClickListener(clickListener);
        schedule_clock.setOnClickListener(clickListener);
    }

    private void setTimeListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        };
        image_time.setOnClickListener(clickListener);
        schedule_belong_time.setOnClickListener(clickListener);
    }


    private Integer switchImportUrgentRadioButton() {
        switch (import_urgent_degree.getCheckedRadioButtonId()){
            case R.id.import_urgent:
                return StringUtil.CATEGORY_IMPORT_EMERGE;
            case R.id.import_no_urgent:
                return StringUtil.CATEGORY_IMPORT_NO_EMERGE;
            case R.id.no_import_urgent:
                return StringUtil.CATEGORY_NO_IMPORT_EMERGE;
            case R.id.no_import_no_urgent:
                return StringUtil.CATEGORY_NO_IMPORT_NO_EMERGE;
        }
        return null;
    }

    private Integer switchImportUrgentCategory(int category) {
        switch (category){
            case StringUtil.CATEGORY_IMPORT_EMERGE:
                return R.id.import_urgent;
            case StringUtil.CATEGORY_IMPORT_NO_EMERGE:
                return  R.id.import_no_urgent;
            case StringUtil.CATEGORY_NO_IMPORT_EMERGE:
                return R.id.no_import_urgent;
            case StringUtil.CATEGORY_NO_IMPORT_NO_EMERGE:
                return R.id.no_import_no_urgent;
        }
        return null;
    }
    private void initData() {
        scheduleEditViewModel= ViewModelProviders.of(this).get(ScheduleEditViewModel.class);
        initTimeDialog();
        setScheduleClockChange();
        initTagDialog();
        initRemarkDialog();

    }

    private void initRemarkDialog() {
        remarkEditDialog=new RemarkEditDialog(this,null);
        remarkEditDialog.show();
        remarkEditDialog.dismiss();
    }

    private void initTagDialog() {
        tagDialog=new LabelAddDialog(ScheduleEditActivity.this,new DialogScheduleLabelRecyclerAdapter(ScheduleEditActivity.this,handler));
        DialogScheduleLabelRecyclerAdapter dialogAdapter = (DialogScheduleLabelRecyclerAdapter) tagDialog.getAdapter();
        List<Schedule_label> allNoteLabelStatic = scheduleEditViewModel.getAllScheduleLabelStatic();
        dialogAdapter.setAll(allNoteLabelStatic);
        scheduleEditViewModel.getAllScheduleLabel().observe(ScheduleEditActivity.this, new Observer<List<Schedule_label>>() {
            @Override
            public void onChanged(List<Schedule_label> notes_labels) {
                // dialog.updateView(notes_labels);
                dialogAdapter.setAll(notes_labels);
                dialogAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initTimeDialog() {
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);
        schedule_belong_time.setText(format(mHour)+":"+format(mMinute));
        timePickerDialog=new TimePickerDialog(ScheduleEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                schedule_belong_time.setText(format(hourOfDay)+":"+format(minute));
                if(mHour>hourOfDay||(mHour==hourOfDay&&mMinute>mMinute)) old_clock.setVisibility(View.VISIBLE);
                else old_clock.setVisibility(View.GONE);
            }
        }, mHour, mMinute, true);
    }

    private void setScheduleClockChange() {
        if(isClock!=null) isClock=!isClock;
        else isClock=false;
        if(isClock)
            schedule_clock.setText(getString(R.string.clock_set));
        else
            schedule_clock.setText(getString(R.string.clock_no_set));

    }

    private void getData() {
        Intent intent = getIntent();
        String date=intent.getStringExtra("date");
        if(Tool.testStringNotNULL(date))
        {
            if(TimeUtils.DatePickerDateConverter.dateParse(date)!=null)
                calendar.setTime(TimeUtils.DatePickerDateConverter.dateParse(date));
        }
        schedule= (Schedule) intent.getSerializableExtra("schedule");
        if(schedule!=null){
            activity_status=StringUtil.UPDATE_STATUS;
            image_action_delete.setVisibility(View.VISIBLE);
            check_schedule_done.setVisibility(View.VISIBLE);
            if(schedule.getIsDone()!=null&&schedule.getIsDone()==StringUtil.DONE)
            check_schedule_done.setChecked(true);
            edit_text_schedule_title.setText(schedule.getTitle());
            if(schedule.getProperty()==StringUtil.CLOCK_SET) setScheduleClockChange();
            import_urgent_degree.check(switchImportUrgentCategory(schedule.getCategory()));
            List<Schedule_label> scheduleLabelByScheduleId = scheduleEditViewModel.getScheduleLabelByScheduleId(schedule.getId());
            scheduleLabelRecyclerAdapter.setAll(scheduleLabelByScheduleId);
            scheduleLabelRecyclerAdapter.notifyDataSetChanged();
            initScheduleLabelList=new ArrayList<>();
            for(Schedule_label label:scheduleLabelByScheduleId){
                initScheduleLabelList.add(label);
            }
            remarkEditDialog.setRemark(schedule.getRemark());
            initCalendar=Calendar.getInstance();
            initCalendar.setTime(MyConverter.timeStampToDate(schedule.getBelongTime()));
            calendar.setTime(MyConverter.timeStampToDate(schedule.getBelongTime()));
            timePickerDialog.updateTime(initCalendar.get(Calendar.HOUR_OF_DAY), initCalendar.get(Calendar.MINUTE));
            schedule_belong_time.setText(format(initCalendar.get(Calendar.HOUR_OF_DAY))+":"+format(initCalendar.get(Calendar.MINUTE)));
        }else {
            activity_status=StringUtil.INSERT_STATUS;
        }
        schedule_date.setText(TimeUtils.DatePickerDateConverter.dateFormat(calendar.getTime()));
        radioButtonTextSizeChange(import_urgent_degree.getCheckedRadioButtonId());
    }

    private void initView() {
        edit_text_schedule_title=findViewById(R.id.edit_text_schedule_title);
        import_urgent_degree=findViewById(R.id.import_urgent_degree);
        check_schedule_done=findViewById(R.id.check_schedule_done);
        import_urgent=findViewById(R.id.import_urgent);
        import_no_urgent=findViewById(R.id.import_no_urgent);
        no_import_urgent=findViewById(R.id.no_import_urgent);
        no_import_no_urgent=findViewById(R.id.no_import_no_urgent);
        image_action_tag=findViewById(R.id.image_action_tag);
        image_action_remark=findViewById(R.id.image_action_remark);
        image_action_sure=findViewById(R.id.image_action_sure);
        schedule_belong_time=findViewById(R.id.schedule_belong_time);
        image_action_delete=findViewById(R.id.image_action_delete);
        image_time=findViewById(R.id.image_time);
        image_clock=findViewById(R.id.image_clock);
        schedule_clock=findViewById(R.id.schedule_clock);
        old_clock=findViewById(R.id.old_clock);
        recycler_schedule_label=findViewById(R.id.recycler_schedule_label);
        scheduleLabelRecyclerAdapter = new ScheduleLabelRecyclerAdapter(this, handler,ScheduleLabelRecyclerAdapter.SCHEDULE_LABEL_SMALL_LAYOUT);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_schedule_label.setLayoutManager(linearLayoutManager);
        recycler_schedule_label.setAdapter(scheduleLabelRecyclerAdapter);
        schedule_date=findViewById(R.id.schedule_date);
    }

    private String format(int x) {
        String s = Integer.toString(x);
        if (s.length()== 1)
             return "0" + s;
        return s;
    }
}
