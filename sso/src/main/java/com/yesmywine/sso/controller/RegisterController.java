package com.yesmywine.sso.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.db.base.biz.RedisCache;
import com.yesmywine.sso.bean.UserInformation;
import com.yesmywine.sso.service.UserInfoService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.Constants;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by SJQ on 2017/6/19.
 */
@RestController
@RequestMapping("/web/sso/register")
public class RegisterController  {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(method = RequestMethod.POST)
    public String register(String userName, String password,String registerChannel,String phoneVerifyCode, HttpServletRequest request, HttpServletResponse response){

        try {
            ValueUtil.verify(userName,"userName");
            ValueUtil.verify(password,"password");
            ValueUtil.verify(phoneVerifyCode,"phoneVerifyCode");
            String redisResult = RedisCache.get(Constants.USER_PHONE_VERIFY_CODE+userName);
            if(redisResult!=null){
                String code = ValueUtil.getFromJson(redisResult,"messages");
                if(!code.equals(phoneVerifyCode)){
                    ValueUtil.isError("验证码输入错误");
                }
            }else{
                ValueUtil.isError("验证码已过期");
            }

            UserInformation userInformation = userInfoService.register(userName,password,registerChannel==null?"GW":registerChannel, phoneVerifyCode);
            Map<String,Object> map = new HashMap<>();
            map.put("userId",userInformation.getId());
            String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST,"/web/userservice/security/initalize", com.yesmywine.httpclient.bean.RequestMethod.post,map,request);
            String userJson = null;
            if(result!=null){
                JSONObject jsonObject = JSON.parseObject(result);
                String resultCode = jsonObject.getString("code");
                if(!resultCode.equals("201")){
                    userInfoService.delete(userInformation);
                    ValueUtil.isError("系统更新，暂无法注册");
                }else{//商城注册成功，向paas同步
                    Map<String,Object> userMap = new HashMap<>();
                    userMap.put("jsonData",result);
                    String paasResult = SynchronizeUtils.getResult(Dictionary.PAAS_HOST,"/user/userInfo/syn", com.yesmywine.httpclient.bean.RequestMethod.post,userMap,request);
                    if(paasResult==null){
                        userInfoService.delete(userInformation);
                        ValueUtil.isError("系统更新，暂无法注册");
                    }else{
                        String code = ValueUtil.getFromJson(paasResult,"code");
                        if(!code.equals("201")){
                            userInfoService.delete(userInformation);
                            ValueUtil.isError("系统更新，暂无法注册");
                        }
                    }
                }
            }else{
                userInfoService.delete(userInformation);
                ValueUtil.isError("系统更新，暂无法注册");
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED,"SUCCESS");
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/verifyCode",method = RequestMethod.POST)
    public String verifyCode(String userName, HttpServletRequest request, HttpServletResponse response){
        try {
            ValueUtil.verify(userName,"userName");
            Map<String,Object> requestParam = new HashMap<>();
            requestParam.put("phones",userName);
            requestParam.put("code",Dictionary.PHONE_MODEL);
            String result = SynchronizeUtils.getResult(Dictionary.PHONE_MESSAGE , "/send/sendSms/itf", com.yesmywine.httpclient.bean.RequestMethod.post,requestParam,null);
            String messages=ValueUtil.getFromJson(result,"data");
            JSONObject object = new JSONObject();
            object.put("messages",messages);
            RedisCache.set(Constants.USER_PHONE_VERIFY_CODE+userName,object);
            RedisCache.expire(Constants.USER_PHONE_VERIFY_CODE+userName, 10*60);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,messages);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

    }
}
