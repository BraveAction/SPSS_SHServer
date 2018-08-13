package com.spss.smarthome.service.impl.user;

import com.spss.smarthome.common.exception.RequestParameterException;
import com.spss.smarthome.common.exception.ServiceException;
import com.spss.smarthome.common.exception.UserException;
import com.spss.smarthome.common.service.BaseService;
import com.spss.smarthome.controller.form.ChangePhoneForm;
import com.spss.smarthome.controller.form.ChangePwdForm;
import com.spss.smarthome.controller.form.UserAtHouse;
import com.spss.smarthome.controller.form.UserSignInForm;
import com.spss.smarthome.dao.UserDao;
import com.spss.smarthome.dao.vo.UserSignUpInVo;
import com.spss.smarthome.model.User;
import com.spss.smarthome.service.AuthService;
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
    AuthService authService;

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
        return userDao.changePwd(user.getId(), bCryptPasswordEncoder.encode(changePwdForm.getNewPwd())) > 0;
    }

    @Override
    public List<UserAtHouse> findUserByGatewayMac(String mac) {
        return userDao.findUserByGatewayMac(Long.valueOf(user.getId()), mac);
    }

    @Override
    public UserSignUpInVo changeUserName(String newName) throws ServiceException {
        User dbUser = authService.findByUsername(newName);
        if (dbUser != null) {
            throw new UserException("用户名已存在！");
        }

        if (userDao.changeUserName(user.getId(), newName) > 0) {
            return authService.signIn(new UserSignInForm(newName, user.getPassword()));
        }
        return null;
    }

    @Override
    public UserSignUpInVo changePhone(ChangePhoneForm changePhoneForm) throws ServiceException {
        User dbUser = userDao.findByPhone(changePhoneForm.getNewPhone());
        if (dbUser != null) {
            throw new UserException("该手机号用户已存在！");
        }
        if (userDao.changePhone(user.getId(), changePhoneForm.getNewPhone()) > 0) {
            return authService.signIn(new UserSignInForm(user.getUsername(), user.getPassword()));
        }
        return null;
    }

    @Override
    public boolean adminLogin(UserSignInForm userSignInForm) throws ServiceException {
        String rawPassword = userSignInForm.getPassword();
        return user.getUsername().equals(userSignInForm.getUserName()) && bCryptPasswordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public List<User> finall() {
        return userDao.findAll();
    }
}
