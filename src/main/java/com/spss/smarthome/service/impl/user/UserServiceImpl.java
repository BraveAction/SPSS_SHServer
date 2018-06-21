package com.spss.smarthome.service.impl.user;

import com.spss.smarthome.common.exception.ServiceException;
import com.spss.smarthome.dao.UserDao;
import com.spss.smarthome.model.User;
import com.spss.smarthome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User findByUsername(String userName) throws ServiceException {
        return userDao.findByName(userName);
    }

    @Override
    public List<User> finall() {
        return userDao.findAll();
    }
}
