package com.example.demo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.demo.R;

import java.util.Calendar;


public class ScheduleDateChooseDialog extends AlertDialog {

    DatePicker datePicker;
    RadioGroup radioGroup;
    public int radioButton_all=R.id.radioButton_all;
    public int radioButton_not_done=R.id.radioButton_not_done;
    public int radioButton_done=R.id.radioButton_done;
    Calendar calendar=Calendar.getInstance();
    TextView text_action_sure,text_action_cancel;
    public ScheduleDateChooseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_schedule_date);
        getWindow().setGravity(Gravity.TOP);
        datePicker=findViewById(R.id.datePicker);
        radioGroup=findViewById(R.id.radio_done_choose);
        text_action_sure=findViewById(R.id.text_action_sure);
        text_action_cancel=findViewById(R.id.text_action_cancel);
        text_action_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public java.util.Date getDate() {
        calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
        return calendar.getTime();
    }
    public void setSureClickListener(View.OnClickListener listener){
        text_action_sure.setOnClickListener(listener);
    }

    public int getRadioButtonChoose(){
        return radioGroup.getCheckedRadioButtonId();
    }
}
