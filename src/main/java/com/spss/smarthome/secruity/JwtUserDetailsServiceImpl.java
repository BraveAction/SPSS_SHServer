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

    private JwtUser mJwtUser;
    private User mUser;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        mUser = authService.findByUsername(username);

        if (mUser == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            mJwtUser = JwtUserFactory.create(mUser);
            return mJwtUser;
        }
    }

    public User getUser() {
        return mUser;
    }

    public JwtUser getJwtUser() {
        return mJwtUser;
    }
}
