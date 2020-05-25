package com.example.demo.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.demo.R;
import com.example.demo.util.StringUtil;

public class UserStatusDialog extends AlertDialog {

    Activity mActivity;
    ConstraintLayout layout_login,layout_logout,layout_close;

    public UserStatusDialog(@NonNull Activity context) {
        super(context);
        mActivity=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_status);
      //  getWindow().setGravity(Gravity.BOTTOM);
        layout_login=findViewById(R.id.layout_login);
        layout_logout=findViewById(R.id.layout_logout);
        layout_close=findViewById(R.id.layout_close);
        if(StringUtil.getSessionId()!=null)
            layout_login.setVisibility(View.GONE);
    }

   public void setLoginListener(View.OnClickListener listener){
        layout_login.setOnClickListener(listener);
    }
    public void setLogoutListener(View.OnClickListener listener){
        layout_logout.setOnClickListener(listener);
    }
    public void setCloseListener(View.OnClickListener listener){
        //Todo:自己触发关闭程序
        layout_close.setOnClickListener(listener);
    }
}
