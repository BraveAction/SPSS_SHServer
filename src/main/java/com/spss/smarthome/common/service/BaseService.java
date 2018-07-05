package com.spss.smarthome.common.service;

import com.spss.smarthome.secruity.JwtTokenUtil;
import com.spss.smarthome.secruity.JwtUser;
import com.spss.smarthome.secruity.JwtUserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class BaseService {

    protected JwtUser user;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 从token中获取用户名后,从数据库中加载用户信息
     *
     * @param request
     */
    public void setToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (StringUtils.isEmpty(token)) {
            return;
        }
//        String userName = jwtTokenUtil.getUsernameFromToken(token.substring(tokenHead.length()));
        user = ((JwtUserDetailsServiceImpl) userDetailsService).getJwtUser();

    }
}
