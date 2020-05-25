package com.example.demo.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.demo.R;
import com.example.demo.data.model.Task;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;
import com.example.demo.util.Tool;

public class TaskDetailDialog extends AlertDialog {
     TextView task_count,task_time,task_name;
    Button button_task_edit_action,button_task_move_action,
            button_task_delete_action,button_task_time_axis_action,
            button_task_data_analyse_action;
    Task task;
    Handler handler;
    Context context;
    public TaskDetailDialog(@NonNull Context context, Task task) {
        super(context);
        this.task=task;
        this.context=context;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_task_detail);
        task_count=findViewById(R.id.task_count);
        task_time=findViewById(R.id.task_time);
        task_name=findViewById(R.id.task_name);
        button_task_edit_action=findViewById(R.id.button_task_edit_action);
        button_task_move_action=findViewById(R.id.button_task_move_action);
        button_task_delete_action=findViewById(R.id.button_task_delete_action);
        button_task_time_axis_action=findViewById(R.id.button_task_time_axis_action);
        button_task_data_analyse_action=findViewById(R.id.button_task_data_analyse_action);
        if(task!=null){
            task_name.setText(task.getTitle());
            task_count.setText(""+task.getWorkCount());
            task_time.setText(""+TimeUtils.timeStampParseToMinus(task.getWorkTime())*task.getWorkCount());
        }
        button_task_edit_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskEditDialog dialog=new TaskEditDialog(context,task);
                dialog.show();
                dialog.getImage_action_sure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tool.sendMessage(handler, StringUtil.TASK_UPDATE,dialog.getTask());
                        dialog.dismiss();
                    }
                });
                dismiss();

            }
        });
        button_task_delete_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(context);
//                builder.setMessage(context.getString(R.string.notice)+context.getString(R.string.delete_task_sure_text)+task.getTitle());
//                builder.setCancelable(true);
//                builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Tool.sendMessage(handler,StringUtil.TASK_DELETE,task);
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog dialog=builder.create();
//                dialog.show();
                Tool.alertDialogShow(context,
                        context.getString(R.string.notice)+context.getString(R.string.delete_task_sure_text)+task.getTitle(),
                        new DialogInterface.OnClickListener() {
                                                @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Tool.sendMessage(handler,StringUtil.TASK_DELETE,task);
                        dialog.dismiss();
                    }
                });
                dismiss();
            }
        });
    }

    public Task getTask() {
        return task;
    }
}
