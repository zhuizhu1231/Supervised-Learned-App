package com.example.demo.ui.user.register;

import android.os.Handler;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demo.R;
import com.example.demo.data.Result;
import com.example.demo.data.entities.User;
import com.example.demo.data.repository.UserRepository;
import com.example.demo.util.Tool;

public class RegisterViewModel extends ViewModel {



    private UserRepository userRepository;
    private MutableLiveData<RegisterFormState> registerFormState;

    RegisterViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public LiveData<RegisterFormState> getRegisterFormState() {
        if(null==registerFormState) registerFormState=new MutableLiveData<>();
        return registerFormState;
    }

    public void registerDataChange(String name, String password, String passwordAgain) {
        boolean n,p,p2;
        if(Tool.testStringNotNULL(name))
            n=true;
        else
            n=false;
        if(password.trim().length()>=5)
            p=true;
        else p=false;
        if(passwordAgain.equals(password))
            p2=true;
        else p2=false;
        registerFormState.setValue(new RegisterFormState(n,p,p2));
    }

    public void registerUser(User user, Handler handler) {
        userRepository.registerUser( user,handler);
    }


}
