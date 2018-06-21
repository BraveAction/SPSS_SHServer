package com.spss.smarthome.service;

import com.spss.smarthome.common.exception.ServiceException;
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
     * @param userName
     * @return
     * @throws ServiceException
     */
    User findByUsername(String userName) throws ServiceException;
}
