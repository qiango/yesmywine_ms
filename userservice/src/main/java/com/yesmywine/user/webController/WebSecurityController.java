package com.yesmywine.user.webController;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.db.base.biz.RedisCache;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.bean.ConstantData;
import com.yesmywine.user.dao.UserEmailDao;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.entity.UserEmail;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.service.UserInformationService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.encode.Encode;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by hz on 6/26/17.
 */
@RestController
@RequestMapping("/member/userservice/security")
public class WebSecurityController {//账号安全

    @Autowired
    private UserInformationService userInformationService;
    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private UserEmailDao userEmailDao;

    @RequestMapping( method = RequestMethod.PUT)
    public String updatePassword(@RequestParam Map<String,String> param,HttpServletRequest request){//修改密码
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,userInformationService.update(param,request));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String message(HttpServletRequest request,String phone){//发送验证码
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,userInformationService.message(request,phone));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/bindingPhone",method = RequestMethod.PUT)
    public String bindingPhone(HttpServletRequest request,String message,String phone){//绑定手机确认(新增)
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,userInformationService.bindingPhone(request,message, phone));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/updatePhone",method = RequestMethod.PUT)
    public String updatePhone(HttpServletRequest request,String message,String phone){//绑定手机确认(修改)第一步(验证验证码)
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,userInformationService.updatePhone(request,message,phone));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/resetPayPassword",method = RequestMethod.PUT)
    public String resetPayPassword(HttpServletRequest request,String phone){//重置支付密码
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,userInformationService.resetPayPassword(request,phone));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/updatePayPassword",method = RequestMethod.PUT)
    public String updatePayPassword(HttpServletRequest request,String oldPassword,String payPasswordFirst){//修改支付密码
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,userInformationService.updatePayPassword(request, oldPassword, payPasswordFirst));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/payPassword",method = RequestMethod.PUT)
    public String payPassword(HttpServletRequest request,String payPasswordFirst){//设置支付密码
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,userInformationService.payPassword(request, payPasswordFirst));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/sendMail",method = RequestMethod.POST)
    public String sendMail(HttpServletRequest request,String email,Integer type)throws YesmywineException {//发送邮件
        if(null!=userInformationDao.findByEmailAndBindEmailFlag(email,true)){
            return ValueUtil.toError("500","该邮件已被使用!");
        }
            Integer userId = UserUtils.getUserId(request);
            UserInformation userInformation = userInformationDao.findOne(userId);
            String code=Encode.getSalt(6);
            System.out.print(code);
            String typeString=null;
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MINUTE, 30);
            if(type==1){//pc邮箱绑定
                    userInformation.setActivationTime(c.getTime());
                    userInformation.setCodeEmail(code);
                    userInformation.setEmail(email);
                    userInformation.setBindEmailFlag(false);
                    userInformationDao.save(userInformation);
                typeString="T8XYzvOQdX";
                }else if(type==2) {//pc邮箱修改
                   UserEmail userEmail=userEmailDao.findByUserId(userId);
                    if(null==userEmail){
                         userEmail=new UserEmail();
                    }
                userEmail.setEmail(email);
                userEmail.setUserId(userId);
                userEmail.setCodeEmail(code);
                userEmail.setActivationTime(c.getTime());
                userEmailDao.save(userEmail);
                typeString="KVjWOYXbJc";
                }
            else if(type==3){//app邮箱验证
                typeString="tfCSAZBjMx";
                RedisCache.set(email,code,600);
                UserEmail userEmail=userEmailDao.findByUserId(userId);
                if(null==userEmail){
                    userEmail=new UserEmail();
                }
                userEmail.setEmail(email);
                userEmail.setUserId(userId);
                userEmailDao.save(userEmail);
            }
        HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/web/email/emailSend", com.yesmywine.httpclient.bean.RequestMethod.post);
        httpRequest.addParameter("receiveMailAccount",email);
        httpRequest.addParameter("code",typeString);
        httpRequest.addParameter("title","");
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("email",email);
        jsonObject.put("emailCode",code);
        httpRequest.addParameter("theme",jsonObject);
        httpRequest.run();
        return ValueUtil.toJson(HttpStatus.SC_OK);
    }

    @RequestMapping(value = "/updateRegister",method = RequestMethod.POST)
    public String updateRegister(HttpServletRequest request, String message,String email) {//改绑验证邮件App
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,userInformationService.updateResgister(request,message,email));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }



}
