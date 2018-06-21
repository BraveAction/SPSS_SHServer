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
    private String token;
    @NotNull
    @NonNull
    private String id;
    @NotNull
    @NonNull
    private String userName;
    @NotNull
    @NonNull
    private String phone;
}