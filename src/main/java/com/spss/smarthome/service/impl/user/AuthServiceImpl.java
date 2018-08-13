package com.spss.smarthome.service.impl.user;


import com.spss.smarthome.common.exception.RequestParameterException;
import com.spss.smarthome.common.exception.ServiceException;
import com.spss.smarthome.common.exception.UserException;
import com.spss.smarthome.controller.form.InitPasswordForm;
import com.spss.smarthome.controller.form.UserSignInForm;
import com.spss.smarthome.controller.form.UserSignUpForm;
import com.spss.smarthome.dao.UserDao;
import com.spss.smarthome.dao.vo.UserSignUpInVo;
import com.spss.smarthome.model.User;
import com.spss.smarthome.secruity.JwtTokenUtil;
import com.spss.smarthome.secruity.JwtUser;
import com.spss.smarthome.secruity.JwtUserDetailsServiceImpl;
import com.spss.smarthome.service.AuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDao userRepository;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenUtil jwtTokenUtil,
            UserDao userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(String userName) throws ServiceException {
        return userRepository.findByName(userName);
    }

    @Override
    public UserSignUpInVo register(UserSignUpForm userSignUpForm) {
        User dbUser = userRepository.findUser(userSignUpForm);
        if (dbUser != null) {
            if (dbUser.getUserName().equals(userSignUpForm.getUserName())) {
                throw new UserException("用户名已存在请更改后重新注册！");
            } else if (dbUser.getPhone().equals(userSignUpForm.getPhone())) {
                throw new UserException("该手机号用户已存在请更改后重新注册！");
            }
        }
        final String rawPassword = userSignUpForm.getPassword();
        userSignUpForm.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        if (userRepository.insert(userSignUpForm) > 0) {
            UserSignInForm userSignInForm = new UserSignInForm(userSignUpForm.getUserName(), rawPassword);
            return signIn(userSignInForm);
        }
        return null;
    }

    @Override
    public boolean updatePassword(InitPasswordForm initPasswordForm) {
        final String rawPassword = initPasswordForm.getPassword();
        initPasswordForm.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        return userRepository.updatePassword(initPasswordForm) > 0;
    }

    @Override
    public UserSignUpInVo signIn(UserSignInForm userSignInForm) {
//        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security
//        final Authentication authentication = authenticationManager.authenticate(upToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userSignInForm.getUserName());
        String rawPassword = userSignInForm.getPassword();

        if (userDetails == null) {
            throw new RequestParameterException("用户不存在，请先注册!");
        }

        if (!bCryptPasswordEncoder.matches(rawPassword, userDetails.getPassword())) {
            throw new RequestParameterException("密码不正确，请重新输入!");
        }
        if (userDetailsService instanceof JwtUserDetailsServiceImpl) {
            User user = ((JwtUserDetailsServiceImpl) userDetailsService).getUser();
            final String token = jwtTokenUtil.generateToken(userDetails);
            UserSignUpInVo userSignUpInVo = new UserSignUpInVo();
            BeanUtils.copyProperties(user, userSignUpInVo);
            userSignUpInVo.setToken(token);
            return userSignUpInVo;
        } else {
            return null;
        }
    }

    @Override
    public String refresh(String oldToken) {
        final String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}

