package com.spss.smarthome.secruity;

import com.spss.smarthome.model.User;
import com.spss.smarthome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 令牌校验获取用户信息
 */
@Service
@Component("userDetailsService")
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService authService;

    private User user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user = authService.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }

    public User getUser() {
        return user;
    }
}
