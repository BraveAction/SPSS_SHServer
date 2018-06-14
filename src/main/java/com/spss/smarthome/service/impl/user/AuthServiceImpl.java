package com.spss.smarthome.service.impl.user;


import com.spss.smarthome.common.exception.UserException;
import com.spss.smarthome.dao.UserDao;
import com.spss.smarthome.model.User;
import com.spss.smarthome.secruity.JwtTokenUtil;
import com.spss.smarthome.secruity.JwtUser;
import com.spss.smarthome.service.AuthService;
import com.spss.smarthome.service.common.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public User register(User userToAdd) {
        User dbUser = userRepository.findUser(userToAdd);
        if (dbUser != null) {
            if (dbUser.getUserName().equals(userToAdd.getUserName())) {
                throw new UserException("用户名已存在请更改后重新注册！");
            } else if (dbUser.getPhone().equals(userToAdd.getPhone())) {
                throw new UserException("该手机号用户已存在请更改后重新注册！");
            }
        }
        final String rawPassword = userToAdd.getPassword();
        userToAdd.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        if (userRepository.insert(userToAdd) != -1) {
            return userToAdd;
        }
        return null;
    }

    @Override
    public boolean updatePassword(User updatePwdUser) {
        final String rawPassword = updatePwdUser.getPassword();
        updatePwdUser.setPassword(bCryptPasswordEncoder.encode(rawPassword));
        Long index = userRepository.updatePassword(updatePwdUser);
        return index != -1;
    }

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String token = jwtTokenUtil.generateToken(userDetails);
        return token;
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

