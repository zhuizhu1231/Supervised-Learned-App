package com.example.demo.ui.user.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.demo.R;
import com.example.demo.data.model.User;
import com.example.demo.ui.user.register.RegisterActivity;
import com.example.demo.util.StringUtil;

public class LoginActivity extends AppCompatActivity {
    // TODO: 需要在model中设置方法读shareP 存在user 密码 联网     否则（无密码或已被登录）跳转到login
    private LoginViewModel loginViewModel;
    private SharedPreferences sharedPreferences;
    Button action_toRegister;
    ProgressBar loadingProgressBar;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case StringUtil.FAIL_REQUEST:
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), getString(R.string.fail_request),Toast.LENGTH_SHORT).show();
                    break;
                case StringUtil.ONLINE:
                    User result = (User) msg.obj;
                    loginViewModel.saveUserInfo(result);
//                    Intent updateServiceIntent=new Intent(LoginActivity.this, TimerUpdateService.class);
//                    startService(updateServiceIntent);
                    Intent intent=new Intent();
                    intent.putExtra("user",result);
                    intent.putExtra("user_status",StringUtil.ONLINE);
                    setResult(RESULT_OK,intent);
                    String welcome = getString(R.string.welcome) + result.getName();
                //    SharedPreferencesUtils.writeInfo();
                    Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
                   finish();
                    break;
                case StringUtil.OFFLINE:
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),  msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        loginViewModel.getUserRepository().setCacheDataSource(getApplicationContext());
        initToRegisterActivityButton();
        final EditText usernameEditText = findViewById(R.id.login_user_number);
        final EditText passwordEditText = findViewById(R.id.login_user_password);
        final Button loginButton = findViewById(R.id.action_login);
        loadingProgressBar = findViewById(R.id.loading);
        Integer isLogin = loginViewModel.checkUserLogin();
//        if(isLogin!=null) {
//            if (isLogin == StringUtil.ONLINE) {
//                //Todo:用户详情页面
////                Intent intent = new Intent();
////                startActivity(intent);
//                finish();
//            }
//        }

        //TODO:待观察
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUserNumberError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUserNumberError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(Integer.valueOf(usernameEditText.getText().toString()),
                            passwordEditText.getText().toString(), handler);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(Integer.valueOf(usernameEditText.getText().toString()),
                        passwordEditText.getText().toString(),handler);
            }
        });

    }

    private void initToRegisterActivityButton() {
        action_toRegister=findViewById(R.id.action_toRegister);
        action_toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}
