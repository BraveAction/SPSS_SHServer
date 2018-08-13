package com.spss.smarthome.controller.user;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.spss.smarthome.SmarthomeApplication;
import com.spss.smarthome.common.controller.BaseController;
import com.spss.smarthome.common.controller.Result;
import com.spss.smarthome.common.exception.RequestParameterException;
import com.spss.smarthome.common.exception.ServiceException;
import com.spss.smarthome.common.service.BaseService;
import com.spss.smarthome.controller.form.*;
import com.spss.smarthome.dao.vo.UserSignUpInVo;
import com.spss.smarthome.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户获取验证码")
public class VerificationCodeController extends BaseController {

    //腾讯短信SDK APPID
    @Value("${tencent.sms.appid}")
    private int appid;
    //腾讯短信SDK Key
    @Value("${tencent.sms.appkey}")
    private String appkey;
    @Value("${tencent.sms.templateid}")
    private int templateid;
    @Value("${tencent.sms.smsSign}")
    private String smsSign;
    //发送短信的时间间隔
    @Value("${tencent.sms.interval}")
    private String interval;

    @Autowired
    private UserService userService;

    /**
     * 用户获取验证码
     *
     * @param phone
     * @return
     * @Todo 验证码发送时间间隔
     */
    @RequestMapping(value = "/getVerificationCode", method = RequestMethod.GET)
    @ApiOperation(value = "获取短信验证码", notes = "用于用户注册，找回密码接口")
    public Result getVerificationCode(@RequestParam(value = "phone") String phone) {
        Result result;
        if (phone == null || phone.isEmpty()) {        //没有手机号参数
            throw new RequestParameterException("手机号不能为空！");
        }

        try {
            String verificationcode = RSAUtils.findRandom();        //生成4位验证码
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            ArrayList<String> params = new ArrayList();
            params.add(verificationcode);
            params.add(interval);        //发送时间间隔2分钟
            SmsSingleSenderResult smsSingleSenderResult = ssender.sendWithParam("86", phone, templateid, params, smsSign, "", "");
            if (smsSingleSenderResult.result == 0) {
                result = Result.success("验证码发送成功！");
                SmarthomeApplication.VCODEMAP.put(phone, verificationcode);
            } else {
                logger.error("验证码发送失败！" + smsSingleSenderResult.errMsg);
                result = Result.success("验证码发送失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送验证码失败" + e.getMessage() + ":手机号为" + phone);
            throw new ServiceException("服务器异常！");
        }
        return result;
    }

    /**
     * 用于测试的接口api
     */
    @RequestMapping(value = "/findall", method = RequestMethod.GET)
    @ApiOperation(value = "获取所有用户", notes = "用于测试的接口")
    public Result findall() throws ServiceException {
        return Result.success(userService.finall());
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ApiOperation(value = "用户修改密码", notes = "用户修改密码")
    public Result changePassword(HttpServletRequest request, @Valid @RequestBody ChangePwdForm updatePwdUser, BindingResult bindingResult) {
        ((BaseService) userService).setToken(request);
        if (bindingResult.hasErrors()) {
            throw new RequestParameterException("参数有误");
        }
        boolean flag = userService.changePassword(updatePwdUser);
        return flag ? Result.success("密码修改成功") : Result.failed(ServiceException.ERROR_CODE, "密码修改失败");
    }

    @RequestMapping(value = "/changeUserName", method = RequestMethod.POST)
    @ApiOperation(value = "用户修改名称", notes = "用户修改名称")
    public Result changeUserName(HttpServletRequest request, @RequestBody ChangeUserNameForm changeUserNameForm) {
        ((BaseService) userService).setToken(request);
        if (StringUtils.isEmpty(changeUserNameForm.getNewName())) {
            throw new RequestParameterException("参数有误");
        }
        UserSignUpInVo userSignUpInVo = userService.changeUserName(changeUserNameForm.getNewName());
        if (userSignUpInVo != null) {
            return Result.success("用户名修改成功", userSignUpInVo);
        }
        return Result.failed(ServiceException.ERROR_CODE, "用户名修改失败");
    }

    @RequestMapping(value = "/changePhone", method = RequestMethod.POST)
    @ApiOperation(value = "用户修改手机号", notes = "用户修改手机号")
    public Result changePhone(HttpServletRequest request, @RequestBody @Valid ChangePhoneForm changePhoneForm, BindingResult bindingResult) {
        ((BaseService) userService).setToken(request);
        if (bindingResult.hasErrors()) {
            throw new RequestParameterException("参数有误");
        }
        String cacheVcode = (String) SmarthomeApplication.VCODEMAP.get(changePhoneForm.getOldPwd());
        if (cacheVcode == null || !cacheVcode.equals(changePhoneForm.getVCode())) {
            throw new RequestParameterException("验证码不匹配");
        }
        UserSignUpInVo userSignUpInVo = userService.changePhone(changePhoneForm);
        if (userSignUpInVo != null) {
            return Result.success("手机号修改成功", userSignUpInVo);
        }
        return Result.failed(ServiceException.ERROR_CODE, "用户名修改失败");
    }

    @RequestMapping(value = "/findAllByHouse", method = RequestMethod.GET)
    @ApiOperation(value = "查询家庭下的所有用户", notes = "根据mac地址查询家庭下的所有用户")
    public Result findAllByHouse(HttpServletRequest request, @NotNull String mac) throws AuthenticationException {
        ((BaseService) userService).setToken(request);
        if (StringUtils.isEmpty(mac)) {
            throw new RequestParameterException();
        }
        List<UserAtHouse> userAtHouseList = userService.findUserByGatewayMac(mac);
        return Result.success(userAtHouseList);
    }

    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    @ApiOperation(value = "管理员用户登录工程模式", notes = "管理员用户登录工程模式")
    public Result adminLogin(HttpServletRequest request, @RequestBody @Valid UserSignInForm userSignInForm, BindingResult bindingResult) {
        ((BaseService) userService).setToken(request);
        if (bindingResult.hasErrors()) {
            throw new RequestParameterException("参数有误");
        }

        boolean flag = userService.adminLogin(userSignInForm);
        if (flag) {
            return Result.success("登录成功");
        }
        return Result.failed(ServiceException.ERROR_CODE, "密码错误，登录失败!");
    }
}




