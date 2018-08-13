package com.spss.smarthome.controller.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserSignUpForm {
    @NonNull
    @NotNull
    private String userName;
    @NonNull
    @NotNull
    private String password;
    @NotNull
    @NonNull
    private String phone;
    @NotNull
    @NonNull
    private String vCode;
}
