package com.example.demo.ui.user.register;

public class RegisterFormState {
    private Boolean nameValid;
    private Boolean passwordValid;
    private Boolean passwordAgainValid;
    private Boolean isValid;

    public RegisterFormState(Boolean nameValid, Boolean passwordValid, Boolean passwordAgainValid) {
        this.nameValid = nameValid;
        this.passwordValid = passwordValid;
        this.passwordAgainValid = passwordAgainValid;
    }

    public Boolean getNameValid() {
        return nameValid;
    }

    public Boolean getPasswordValid() {
        return passwordValid;
    }

    public Boolean getPasswordAgainValid() {
        return passwordAgainValid;
    }


    public Boolean valid(){
        if(nameValid&&passwordAgainValid&&passwordValid) return true;
        return false;
    }

}
