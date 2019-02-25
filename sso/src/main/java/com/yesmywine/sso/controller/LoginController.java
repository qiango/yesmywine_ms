package com.yesmywine.sso.controller;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.db.base.biz.RedisCache;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.sso.bean.AdminUser;
import com.yesmywine.sso.bean.Perms;
import com.yesmywine.sso.bean.Roles;
import com.yesmywine.sso.bean.UserInformation;
import com.yesmywine.sso.service.AdminUserService;
import com.yesmywine.sso.service.PermService;
import com.yesmywine.sso.service.UserInfoService;
import com.yesmywine.sso.utils.PasswordUtils;
import com.yesmywine.util.basic.Constants;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.AccessToken;
import com.yesmywine.jwt.Audience;
import com.yesmywine.jwt.JwtHelper;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

/**
 * Created by SJQ on 2017/6/8.
 */
@RestController
public class LoginController {
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private UserInfoService userService;

    @Autowired
    private PermService permService;

    //获取用户权限
    @RequestMapping(value = "/sso/login",method = RequestMethod.POST)
    public String Login(String userName,String password ,String captcha,HttpServletRequest request ) {
        try {
            ValueUtil.verify(userName,"userName");
            ValueUtil.verify(password,"password");
//            ValueUtil.verify(captcha,"captcha");
//            checkCapcha(captcha,request);
            String loginName = userName;
            AdminUser dbUser = adminUserService.findByUsername(loginName);
            password = PasswordUtils.encodePassword(password);
            ValueUtil.verifyNotExist(dbUser,"无此用户");
            if(dbUser==null&&!dbUser.getPassword().equals(password)){
                ValueUtil.isError("用户名或密码错误！");
            }
            JSONObject permObj = new JSONObject();
            Set<Roles> rolesSet = dbUser.getRoles();
            for(Roles role:rolesSet){
                Set<Perms> permsSet = role.getPerms();
                Boolean haveAllPerms = role.getHaveAllPerms();
                if(haveAllPerms!=null&&haveAllPerms){
                    permObj.clear();
                    List<Perms> permsList = permService.findAll();
                    for(Perms perm:permsList){
                        permObj.put(perm.getPermKey(),perm.getPermName());
                    }
                    break;
                }else{
                    for(Perms perm:permsSet){
                        permObj.put(perm.getPermKey(),perm.getPermName());
                    }
                }

            }
            JSONObject dbUserJSON = ValueUtil.toJsonObject(dbUser);
            dbUserJSON.put("allPerms",permObj);
            RedisCache.set(Constants.USER_INFO+loginName,dbUserJSON);
            String accessToken = "";
            accessToken = JwtHelper.createJWT( dbUser.getId().toString(),dbUser.getUserName(),
                    Audience.expiresSecond * 1000);
            //返回accessToken
            AccessToken accessTokenEntity = new AccessToken();
            accessTokenEntity.setAccess_token(accessToken);
            accessTokenEntity.setExpires_in(Audience.expiresSecond);
            accessTokenEntity.setToken_type("bearer");
            Object object = adminUserService.getUserMenus(dbUser.getId());
            accessTokenEntity.setUserMenusPerms(object);
            accessTokenEntity.setUserId(dbUser.getId());
            accessTokenEntity.setNickName(dbUser.getNickName());
            return ValueUtil.toJson(HttpStatus.SC_CREATED,accessTokenEntity);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    //门户用户登录
    @RequestMapping(value = "/web/sso/login",method = RequestMethod.POST)
    public   String webLogin( UserInformation user , Boolean isNeedCaptcha, String captcha, HttpServletRequest request ) {
        try {
            String userName = user.getUserName();
            String password = user.getPassword();
            System.out.println("username==>"+userName);
            System.out.println("password==>"+password);
            ValueUtil.verify(userName,"userName");
            ValueUtil.verify(password,"password");
//            if(isNeedCaptcha==null){
//                ValueUtil.verify(captcha,"captcha");
//                checkCapcha(captcha,request);
//            }
            password = PasswordUtils.encodePassword(password);
            String loginName = userName;
            UserInformation dbUser = userService.findByUsername(loginName);
            ValueUtil.verifyNotExist(dbUser,"无此用户");
            if(!dbUser.getPassword().equals(password)){
                ValueUtil.isError("用户名或密码错误！");
            }
            RedisCache.set(Constants.USER_INFO+loginName,dbUser);
            String accessToken = "";
            accessToken = JwtHelper.createJWT( dbUser.getId().toString(),dbUser.getUserName(),
                    Audience.expiresSecond * 1000);
            //返回accessToken
            AccessToken accessTokenEntity = new AccessToken();
            accessTokenEntity.setAccess_token(accessToken);
            accessTokenEntity.setExpires_in(Audience.expiresSecond);
            accessTokenEntity.setToken_type("bearer");
            accessTokenEntity.setUserId(dbUser.getId());
            accessTokenEntity.setNickName(dbUser.getNickName());
            return ValueUtil.toJson(HttpStatus.SC_CREATED,accessTokenEntity);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    //验证token有效性
    @RequestMapping(value = "/web/sso/checkToken",method = RequestMethod.GET)
    public String checkToken( HttpServletRequest request ) {
        try {
            String username = UserUtils.getUserName(request);
            if(username==null){
                ValueUtil.isError("token已失效");
            }
            return ValueUtil.toJson("SUCCESS");
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    private void checkCapcha(String captcha, HttpServletRequest request) throws YesmywineException {
        HttpSession session = request.getSession();
        String code = (String)session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if (!captcha.toLowerCase().equals(code.toLowerCase())){
            ValueUtil.isError("验证码错误");
        }
    }
}
