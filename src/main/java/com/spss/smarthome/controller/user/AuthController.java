package com.spss.smarthome.controller.user;

import com.spss.smarthome.SmarthomeApplication;
import com.spss.smarthome.common.exception.UserException;
import com.spss.smarthome.controller.common.RequestParameterException;
import com.spss.smarthome.controller.common.Result;
import com.spss.smarthome.controller.user.form.UserForm;
import com.spss.smarthome.model.User;
import com.spss.smarthome.service.AuthService;
import com.spss.smarthome.service.common.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户登录授权
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public Result createAuthenticationToken(@Valid @RequestBody User authenticationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestParameterException();
        }
        String token = authService.login(authenticationRequest.getUserName(), authenticationRequest.getPassword());

        return Result.success("", token);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public Result refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return Result.success(null);
        } else {
            return Result.success(refreshedToken);
        }
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public Result register(@Valid @RequestBody UserForm addedUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestParameterException();
        }

        String cacheVcode = (String) SmarthomeApplication.VCODEMAP.get(addedUser.getPhone());
        if (cacheVcode == null || !cacheVcode.equals(addedUser.getvCode())) {
            throw new RequestParameterException("验证码不匹配");
        }

        User user = authService.register(addedUser);
        if (user == null) {
            throw new ServiceException();
        }

        return Result.success("用户注册成功");
    }

    @RequestMapping(value = "/initPassword", method = RequestMethod.POST)
    public Result initPassword(@Valid @RequestBody UserForm updatePwdUser, BindingResult bindingResult) {
        String cacheVcode = (String) SmarthomeApplication.VCODEMAP.get(updatePwdUser.getPhone());
        if (cacheVcode == null || !cacheVcode.equals(updatePwdUser.getvCode())) {
            throw new RequestParameterException("验证码不匹配");
        }
        if (!authService.updatePassword(updatePwdUser)) {
            throw new UserException("用户不存在!");
        }
        return Result.success("密码修改成功");
    }
}
