package com.spss.smarthome.service;

import com.spss.smarthome.common.exception.ServiceException;
import com.spss.smarthome.controller.form.ChangePhoneForm;
import com.spss.smarthome.controller.form.ChangePwdForm;
import com.spss.smarthome.controller.form.UserAtHouse;
import com.spss.smarthome.controller.form.UserSignInForm;
import com.spss.smarthome.dao.vo.UserSignUpInVo;
import com.spss.smarthome.model.User;

import java.util.List;


/**
 * 用户业务接口
 */
public interface UserService {

    /**
     * 测试
     */
    List<User> finall() throws ServiceException;


    /**
     * 获取用户
     *
     * @param userName
     * @return
     * @throws ServiceException
     */
    User findByUsername(String userName) throws ServiceException;


    /**
     * 用户更新密码
     *
     * @param changePwdForm
     * @return
     */
    boolean changePassword(ChangePwdForm changePwdForm) throws ServiceException;

    /**
     * 通过家庭绑定的网关信息，查找家庭下的所有用户(不包含当前用户)
     *
     * @param mac
     * @return
     */
    List<UserAtHouse> findUserByGatewayMac(String mac) throws ServiceException;


    /**
     * 用户修改名称
     *
     * @param newName
     * @return 用户登录信息
     * @throws ServiceException
     */
    UserSignUpInVo changeUserName(String newName) throws ServiceException;

    /**
     * 用户修改手机号
     *
     * @param changePhoneForm
     * @return 用户登录信息
     */
    UserSignUpInVo changePhone(ChangePhoneForm changePhoneForm) throws ServiceException;

    /**
     * 管理员用户在工程模式下登录
     *
     * @param userSignInForm
     * @return
     */
    boolean adminLogin(UserSignInForm userSignInForm) throws ServiceException;
}

