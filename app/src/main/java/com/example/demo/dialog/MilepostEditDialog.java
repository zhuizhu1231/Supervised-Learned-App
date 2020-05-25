package com.example.demo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.demo.R;
import com.example.demo.data.model.Milepost;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;

public class MilepostEditDialog extends AlertDialog {
    EditText edit_milepost_title,edit_milepost_remark;
    ImageView image_action_sure;
    Milepost milepost;
    DatePicker datePicker;
    TextView action_delete;
    TimeUtils.DatePickerDateConverter converter=new TimeUtils.DatePickerDateConverter();
    public MilepostEditDialog(@NonNull Context context, Milepost milepost) {
        super(context);
        this.milepost=milepost;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_milepost_edit);
        edit_milepost_remark=findViewById(R.id.edit_milepost_remark);
        edit_milepost_title=findViewById(R.id.edit_milepost_title);
        image_action_sure=findViewById(R.id.image_action_sure);
        datePicker=findViewById(R.id.datePicker);
        action_delete=findViewById(R.id.action_delete);
        DatePicker.OnDateChangedListener onDateChangedListener = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                milepost.setDieTime(converter.parseTimestamp(new Integer[]{year,monthOfYear,dayOfMonth}));
            }
        };
        if(null!=milepost){
            edit_milepost_title.setText(milepost.getTitle());
            edit_milepost_remark.setText(milepost.getRemark());
            Integer[] date = converter.parseDateArray(milepost.getDieTime());
            datePicker.init(date[0],date[1],date[2],onDateChangedListener);
            action_delete.setVisibility(View.VISIBLE);
        }else {
            milepost=new Milepost();
            milepost.setStatus(StringUtil.LOCAL_INSERT);
            Integer[] date = converter.parseDateArray(Tool.createNewTimeStamp());
            datePicker.init(date[0],date[1],date[2],onDateChangedListener);
        }

        setEditTextTextChangedListener();

    }

    private void setEditTextTextChangedListener() {
      

        edit_milepost_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                milepost.setRemark(edit_milepost_remark.getText().toString());
            }
        });

        edit_milepost_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                milepost.setTitle(edit_milepost_title.getText().toString());
            }
        });
    }
    public ImageView getImage_action_sure() {
        return image_action_sure;
    }

    public TextView getAction_delete() {
        return action_delete;
    }

    public Milepost getMilepost() {
        return milepost;
    }
}