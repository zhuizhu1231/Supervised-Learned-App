package com.example.demo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.demo.R;
import com.example.demo.data.model.Task;
import com.example.demo.data.model.User;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;

public class TaskEditDialog extends AlertDialog {
    EditText edit_task_title,edit_task_remark,edit_task_work_time;
    ImageView image_action_sure;
    Task task;
    TextView tag_title,tag_set;
    public TaskEditDialog(@NonNull Context context,Task task) {
        super(context);
        this.task=task;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_task_edit);
        edit_task_remark=findViewById(R.id.edit_task_remark);
        edit_task_title=findViewById(R.id.edit_task_title);
        edit_task_work_time=findViewById(R.id.edit_task_work_time);
        image_action_sure=findViewById(R.id.image_action_sure);
        User user = StringUtil.getUser();
        if(null!=task){
            edit_task_title.setText(task.getTitle());
            edit_task_remark.setText(task.getRemark());
            edit_task_work_time.setText(""+TimeUtils.timeStampParseToMinus(task.getWorkTime()));
            if(task.getAimId()==null){
                tag_set.setVisibility(View.VISIBLE);
            }
        }else {
            task=new Task();
            if(user!=null) {
                task.setWorkTime(user.getWorkTime());
                edit_task_work_time.setHint(""+TimeUtils.timeStampParseToMinus(user.getWorkTime()));
            }
            task.setStatus(StringUtil.LOCAL_INSERT);
        }

        setEditTextTextChangedListener();

    }

    private void setEditTextTextChangedListener() {
        edit_task_work_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                task.setWorkTime(TimeUtils.minusParseToTimeStamp(Integer.parseInt(edit_task_work_time.getText().toString())));
            }
        });

        edit_task_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                task.setRemark(edit_task_remark.getText().toString());
            }
        });

        edit_task_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                task.setTitle(edit_task_title.getText().toString());
            }
        });
    }
    public ImageView getImage_action_sure() {
        return image_action_sure;
    }

    public Task getTask() {
        return task;
    }
}