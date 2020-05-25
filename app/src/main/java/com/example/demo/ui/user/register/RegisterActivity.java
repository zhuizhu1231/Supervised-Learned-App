package com.example.demo.ui.user.register;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.demo.R;
import com.example.demo.data.entities.User;
import com.example.demo.util.StringUtil;

public class RegisterActivity extends AppCompatActivity {
    RegisterViewModel registerViewModel;
   // ActivityRegisterBinding binding;
   public Handler handler=new Handler(){@Override
   public void handleMessage(@NonNull Message msg) {
       switch (msg.what){
           case StringUtil.FAIL_REQUEST:
               Toast.makeText(RegisterActivity.this,getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
               break;
           case StringUtil.SUCCESS_REGISTER:
               Integer id= (Integer) msg.obj;

               Toast.makeText(RegisterActivity.this,getString(R.string.success_register),Toast.LENGTH_SHORT).show();
               AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
               builder.setMessage(getString(R.string.user_number_promot)+id);
               builder.setCancelable(true);
               builder.setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       finish();
                       dialog.dismiss();
                   }
               });
               AlertDialog dialog=builder.create();
               dialog.show();
               break;
           case StringUtil.FAIL_REGISTER:
                   Toast.makeText(RegisterActivity.this,getString(R.string.fail_register),Toast.LENGTH_SHORT).show();
                   break;
       }
   }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel= ViewModelProviders.of(this,new RegisterViewModelFactory()).get(RegisterViewModel.class);
       //binding= DataBindingUtil.setContentView(this,R.layout.activity_register);
        //user=new User();
       // binding.setUser(user);
       // binding.setLifecycleOwner(this);
        final EditText register_user_name=findViewById(R.id.register_user_name);
        final EditText register_user_password=findViewById(R.id.register_user_password);
        final EditText register_user_password_again=findViewById(R.id.register_user_password_again);
        final Button action_register=findViewById(R.id.action_register);
        final RadioButton radio_button_man=findViewById(R.id.radio_button_man);
        final RadioButton radio_button_woman=findViewById(R.id.radio_button_woman);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerViewModel.registerDataChange(register_user_name.getText().toString(),
                        register_user_password.getText().toString(),
                        register_user_password_again.getText().toString());
            }
        };
        register_user_name.addTextChangedListener(textWatcher);
        register_user_password.addTextChangedListener(textWatcher);
        register_user_password_again.addTextChangedListener(textWatcher);
        registerViewModel.getRegisterFormState().observe(this,new Observer<RegisterFormState>() {
            @Override
            public void onChanged(RegisterFormState registerFormState) {
                if(registerFormState.valid())
                    action_register.setEnabled(true);
                else {
                    action_register.setEnabled(false);
                    if(!registerFormState.getNameValid()) register_user_name.setError(getResources().getString(R.string.notice_name_null));
                    if(!registerFormState.getPasswordValid()) register_user_password.setError(getResources().getString(R.string.notice_password_less));
                    if(!registerFormState.getPasswordAgainValid()) register_user_password_again.setError(getResources().getString(R.string.notice_password_again_error));
                }

            }
        });
        action_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=new User();
                user.setName(register_user_name.getText().toString());
                user.setPassword(register_user_password.getText().toString());
                if(radio_button_man.isChecked())
                    user.setSex(StringUtil.SEX_MAN);
                else
                    user.setSex(StringUtil.SEX_WOMAN);

                registerViewModel.registerUser(user,handler);
            }
        });
    }
}
