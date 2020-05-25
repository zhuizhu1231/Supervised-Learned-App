package com.example.demo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.demo.R;

public class RemarkEditDialog extends AlertDialog {

    public RemarkEditDialog(@NonNull Context context,String remark) {
        super(context);
        this.remark=remark;
    }
    ImageView image_action_sure;
    EditText edit_remark;
    String remark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remark_edit);
        image_action_sure=findViewById(R.id.image_action_sure);
        edit_remark=findViewById(R.id.edit_remark);
        setRemark(remark);
        image_action_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remark=edit_remark.getText().toString();
                dismiss();
            }
        });
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark=remark;
        edit_remark.setText(remark);
    }
}
