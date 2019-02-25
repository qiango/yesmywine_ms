package com.yesmywine.sso.controller;

import com.yesmywine.sso.bean.UserInformation;
import com.yesmywine.sso.service.UserInfoService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by SJQ on 2017/6/20.
 */
@RestController
public class ThirdAccountLoginController {
    @Autowired
    private LoginController loginController;

    @Autowired
    private UserInfoService userInfoService;


    @RequestMapping(value = "/web/sso/publicLogin",method = RequestMethod.POST)
    public String thirdAccountLogin(String userName,String channel, HttpServletRequest request, HttpServletResponse response){
        try {
        UserInformation user = userInfoService.findByUsername(userName);
        if(user==null){
            user = userInfoService.register(userName,userName,channel,null);
            return loginController.webLogin(user,true,"",request);
        }
        return ValueUtil.toJson("SUCCESS");
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
}
