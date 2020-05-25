package com.example.demo.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.demo.R;
import com.example.demo.data.model.Aim;
import com.example.demo.data.model.User;
import com.example.demo.util.StringUtil;
import com.example.demo.util.TimeUtils;

public class TargetEditDialog extends AlertDialog {
    Aim aim;
    EditText edit_target_title,edit_target_remark,edit_target_relax_time;
    ImageView image_action_sure,image_delete;
    public TargetEditDialog(Context context,Aim aim) {
        super(context);
        this.aim=aim;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_target_edit);
        edit_target_relax_time=findViewById(R.id.edit_target_relax_time);
        edit_target_remark=findViewById(R.id.edit_target_remark);
        edit_target_title=findViewById(R.id.edit_target_title);
        image_action_sure=findViewById(R.id.image_action_sure);
        image_delete=findViewById(R.id.image_delete);
        User user = StringUtil.getUser();
        if(null!=aim){
            image_delete.setVisibility(View.VISIBLE);
            edit_target_title.setText(aim.getTitle());
            edit_target_remark.setText(aim.getRemark());
            if(aim.getRestTime()==null){
                if(user !=null)
                aim.setRestTime((user.getRestTime()));
            }
            edit_target_relax_time.setText(""+TimeUtils.timeStampParseToMinus(aim.getRestTime()));
        }else {
            aim=new Aim();
            if(user !=null) {
                edit_target_relax_time.setHint("" + TimeUtils.timeStampParseToMinus(user.getRestTime()));
                aim.setRestTime(user.getRestTime());
            }
            image_delete.setVisibility(View.GONE);

            aim.setStatus(StringUtil.LOCAL_INSERT);
        }
        setEditTextTextChangedListener();

    }

    private void setEditTextTextChangedListener() {
        edit_target_relax_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aim.setRestTime(TimeUtils.minusParseToTimeStamp(Integer.parseInt(edit_target_relax_time.getText().toString())));
            }
        });

        edit_target_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aim.setRemark(edit_target_remark.getText().toString());
            }
        });

        edit_target_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aim.setTitle(edit_target_title.getText().toString());
            }
        });
    }

    public ImageView getImage_action_sure() {
        return image_action_sure;
    }

    public Aim getAim() {
        return aim;
    }


    public ImageView getImage_delete() {
        return image_delete;
    }
}
