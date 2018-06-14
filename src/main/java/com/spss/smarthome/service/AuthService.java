package com.spss.smarthome.service;

import com.spss.smarthome.model.User;
import com.spss.smarthome.service.common.ServiceException;

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
    String login(String username, String password);

    /**
     * 用户刷新Token
     *
     * @param oldToken
     * @return
     */
    String refresh(String oldToken);
}
