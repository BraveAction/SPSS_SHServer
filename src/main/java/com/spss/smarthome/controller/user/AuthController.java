package com.spss.smarthome.controller.user;

import com.spss.smarthome.SmarthomeApplication;
import com.spss.smarthome.common.controller.BaseController;
import com.spss.smarthome.common.controller.Result;
import com.spss.smarthome.common.exception.RequestParameterException;
import com.spss.smarthome.common.exception.ServiceException;
import com.spss.smarthome.common.exception.UserException;
import com.spss.smarthome.controller.form.UserSignInForm;
import com.spss.smarthome.controller.form.UserSignUpForm;
import com.spss.smarthome.model.User;
import com.spss.smarthome.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "用户登录注册")
public class AuthController extends BaseController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public Result signIn(@Valid @RequestBody User authenticationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestParameterException();
        }
        UserSignInForm userSignInForm = authService.signIn(authenticationRequest.getUserName(), authenticationRequest.getPassword());

        return Result.success("", userSignInForm);
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    @ApiOperation(value = "用户刷新令牌", notes = "用户刷新令牌")
    public Result refreshAndGetAuthenticationToken(HttpServletRequest request) throws AuthenticationException {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        if (refreshedToken == null) {
            return Result.success(null);
        } else {
            return Result.success(refreshedToken);
        }
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public Result signUp(@Valid @RequestBody UserSignUpForm addedUser, BindingResult bindingResult) {
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
    @ApiOperation(value = "用户找回密码", notes = "用户找回密码")
    public Result initPassword(@Valid @RequestBody UserSignUpForm updatePwdUser, BindingResult bindingResult) {
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
