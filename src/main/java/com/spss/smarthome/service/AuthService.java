package com.spss.smarthome.service;

import com.spss.smarthome.common.exception.ServiceException;
import com.spss.smarthome.controller.form.InitPasswordForm;
import com.spss.smarthome.controller.form.UserSignInForm;
import com.spss.smarthome.controller.form.UserSignUpForm;
import com.spss.smarthome.dao.vo.UserSignUpInVo;
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
     * @param userSignUpForm
     * @return
     */
    UserSignUpInVo register(UserSignUpForm userSignUpForm);

    /**
     * 用户重置密码
     *
     * @param initPasswordForm
     * @return
     */
    boolean updatePassword(InitPasswordForm initPasswordForm);


    /**
     * 用户登录
     *
     *
     * @param userSignInForm@return
     */
    UserSignUpInVo signIn(UserSignInForm userSignInForm);

    /**
     * 用户刷新Token
     *
     * @param oldToken
     * @return
     */
    String refresh(String oldToken);
}
