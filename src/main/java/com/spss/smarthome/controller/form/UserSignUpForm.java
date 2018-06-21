package com.spss.smarthome.controller.form;

import com.spss.smarthome.model.User;

import javax.validation.constraints.NotNull;

public class UserSignUpForm extends User {
    @NotNull
    private String vCode;

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }
}
