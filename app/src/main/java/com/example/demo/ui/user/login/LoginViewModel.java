package com.example.demo.ui.user.login;

import android.os.Handler;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.R;
import com.example.demo.data.model.User;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.util.StringUtil;
import com.example.demo.util.Tool;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private UserRepository userRepository;

    LoginViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }


    public void login(Integer userNumber, String password, Handler handler) {
        // can be launched in a separate asynchronous job
        userRepository.login(userNumber, password,handler);//Result<User>

//        if (result instanceof Result.Success) {
//            User data = ((Result.Success<User>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getName())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
    }

    public void loginDataChanged(String userNumber, String password) {
        if (!isUserNumberValid(userNumber)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_user_number, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder user number validation check
    private boolean isUserNumberValid(String userNumber) {
        if (userNumber == null) {
            return false;
        }
        if (userNumber.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(userNumber).matches();
        } else {
            return !userNumber.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length()>=5;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void saveUserInfo(User result ) {


        userRepository.saveUserInfo(result);
    }

    public Integer checkUserLogin() {
        User user = userRepository.findUser();
        if(user!=null)
            if(Tool.testStringNotNULL(StringUtil.getSessionId()))
                return StringUtil.ONLINE;
            else
                return StringUtil.OFFLINE;
        return null;
    }

    public User getUserInfo() {
        return userRepository.findUser();
    }
}
