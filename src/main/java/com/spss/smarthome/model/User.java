package com.spss.smarthome.model;


import javax.validation.constraints.NotNull;

/**
 * 用户实体类
 */
public class User {

    private String id;
    @NotNull
    private String userName;
    @NotNull
    private String password;
    private String phone;
    private String signDate;

    public User() {

    }

    public User(String id, @NotNull String userName, @NotNull String password, String phone, String signDate) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.signDate = signDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }
}
