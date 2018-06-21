package com.spss.smarthome.service;

import com.spss.smarthome.common.exception.ServiceException;
import com.spss.smarthome.controller.form.UserSignInForm;
import com.spss.smarthome.model.User;

public interface AuthService {
    /**
     * 获取用户
     *
     * @param userName
     * @return
     * @throws ServiceException
     */
    User findByUsername(String userName) throws ServiceException;

    /**
     * 用户注册
     *
     * @param userToAdd
     * @return
     */
    User register(User userToAdd);

    /**
     * 用户更新密码
     *
     * @param updatePwdUser
     * @return
     */
    boolean updatePassword(User updatePwdUser);

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    UserSignInForm signIn(String username, String password);

    /**
     * 用户刷新Token
     *
     * @param oldToken
     * @return
     */
    String refresh(String oldToken);
}
