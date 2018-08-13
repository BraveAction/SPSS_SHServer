package com.spss.smarthome.controller.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class UserSignInForm {
    @NotNull
    @NonNull
    private String userName;
    @NonNull
    @NotNull
    private String password;

}