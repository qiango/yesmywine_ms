package com.yesmywine.sso.controller;

import com.yesmywine.sso.service.AdminUserService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by SJQ on 2017/6/8.
 */
@RestController
@RequestMapping("/sso/logout")
public class LogoutController {

    @Autowired
    private AdminUserService userService;

    //用户登出
    @RequestMapping(method = RequestMethod.GET)
    public String loginOut( String token, HttpServletRequest request) {
        try {
            return ValueUtil.toJson(this.userService.updateByToken(token,request));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }
}
