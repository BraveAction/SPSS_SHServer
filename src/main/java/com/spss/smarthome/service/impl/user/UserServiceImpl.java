package com.spss.smarthome.service.impl.user;

import com.spss.smarthome.common.exception.RequestParameterException;
import com.spss.smarthome.common.exception.ServiceException;
import com.spss.smarthome.common.service.BaseService;
import com.spss.smarthome.controller.form.ChangePwdForm;
import com.spss.smarthome.dao.UserDao;
import com.spss.smarthome.dao.vo.ChangePwdVo;
import com.spss.smarthome.model.User;
import com.spss.smarthome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends BaseService implements UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findByUsername(String userName) throws ServiceException {
        return userDao.findByName(userName);
    }

    @Override
    public boolean changePassword(ChangePwdForm changePwdForm) throws ServiceException {

        if (!bCryptPasswordEncoder.matches(changePwdForm.getOldPwd(), user.getPassword())) {       //旧密码不匹配
            throw new RequestParameterException("原始密码错误!");
        }
        ChangePwdVo changePwdVo = new ChangePwdVo();
        changePwdVo.setNewPwd(bCryptPasswordEncoder.encode(changePwdForm.getNewPwd()));
        changePwdVo.setPhone(changePwdForm.getPhone());
        changePwdVo.setUserId(user.getId());
        return userDao.changePwd(changePwdVo) > 0;
    }

    @Override
    public List<User> finall() {
        return userDao.findAll();
    }
}
