package com.spss.smarthome.controller.user;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.spss.smarthome.SmarthomeApplication;
import com.spss.smarthome.controller.BaseController;
import com.spss.smarthome.controller.common.RequestParameterException;
import com.spss.smarthome.controller.common.Result;
import com.spss.smarthome.service.UserService;
import com.spss.smarthome.service.common.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


/**
 * 用户注册
 */
@RestController
@RequestMapping("/user")
public class SignUpController extends BaseController {

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
    @RequestMapping(value = "/getVerificationCode")
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
    public Result findall() throws ServiceException {
        return Result.success(userService.finall());
    }


}




