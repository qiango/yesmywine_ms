package com.yesmywine.user.webController;

import com.yesmywine.user.dao.UserEmailDao;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.entity.UserEmail;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.service.UserInformationService;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by hz on 6/29/17.
 */

@RestController
@RequestMapping("/web/userservice/security")
public class WebForgotController {//忘记密码

    @Autowired
    private UserInformationService userInformationService;
    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private UserEmailDao userEmailDao;

    @RequestMapping(method = RequestMethod.GET)
    public String message(HttpServletRequest request,String phone){//发送验证码
        try {
            System.out.println("发送＝＝》"+request.getSession().getId());
            return ValueUtil.toJson(HttpStatus.SC_OK,userInformationService.messageForget(request,phone));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
        public String forgotPass(HttpServletRequest request,String message,String phone){//忘记密码第一步
        try {
            System.out.println("发送＝＝》"+request.getSession().getId());

            return ValueUtil.toJson(HttpStatus.SC_CREATED,userInformationService.updateFirst(request, message,phone));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String updatePassword(String passwordFirst,String phone){//忘记密码第二步
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,userInformationService.updatePassword(passwordFirst,phone));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/initalize", method = RequestMethod.POST)
    public String initalize(Integer userId){
        try {
            return userInformationService.initalize(userId);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/register",method = RequestMethod.GET)
    public String register(HttpServletRequest request){//pc绑定验证邮件
        String email = request.getParameter("email");//获取email
        String emaliCode = request.getParameter("emailCode");
        try {
            processActivate(email,emaliCode);//调用激活方法
        } catch (ParseException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return ValueUtil.toJson(HttpStatus.SC_OK);
    }

    @RequestMapping(value = "/registerSecond",method = RequestMethod.GET)
    public String registerSecond(HttpServletRequest request){//pc修改验证邮件
        String email = request.getParameter("email");//获取email
        String emaliCode = request.getParameter("emailCode");
        try {
            processActivateUpdate(email,emaliCode);//调用激活方法
        } catch (ParseException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
        return ValueUtil.toJson(HttpStatus.SC_OK);
    }

    public void processActivate(String email,String emaliCode)throws ServiceException, ParseException {
        //数据访问层，通过email获取用户信息
        UserInformation user=userInformationDao.findByEmailAndCodeEmail(email,emaliCode);
        //验证用户是否存在
        if(user!=null) {
            //验证用户激活状态
            if(!user.getBindEmailFlag()){
                ///没激活
                Date currentTime = new Date();//获取当前时间
                //验证链接是否过期
                if(currentTime.before(user.getActivationTime())){
                    if(emaliCode.equals(user.getCodeEmail())){
                        //激活成功， //并更新用户的激活状态，为已激活
                        System.out.println("==sq==="+user.getBindEmailFlag());
                        user.setBindEmailFlag(true);//把状态改为激活
//                        user.setEmail(email);
                        System.out.println("==sh==="+user.getBindEmailFlag());
                        userInformationDao.save(user);
                    } else {
                        throw new ServiceException("激活码不正确");
                    }
                } else {
                    throw new ServiceException("激活码已过期！");
                }
            } else {
                throw new ServiceException("邮箱已激活，请登录！");
            }
        } else {
            throw new ServiceException("该邮箱已注册！");
        }

    }

    public void processActivateUpdate(String email,String emaliCode)throws ServiceException, ParseException {
        //数据访问层，通过email获取用户信息
        UserEmail user=userEmailDao.findByEmail(email);
        //验证用户是否存在
        if(user!=null) {
            Date currentTime = new Date();//获取当前时间
            //验证链接是否过期
            if(currentTime.before(user.getActivationTime())){
                if(emaliCode.equals(user.getCodeEmail())){
                    UserInformation userInformation = userInformationDao.findOne(user.getUserId());
                    userInformation.setEmail(user.getEmail());
                    userInformationDao.save(userInformation);
                    userEmailDao.delete(user);
                } else {
                    throw new ServiceException("激活码不正确");
                }
            } else {
                throw new ServiceException("激活码已过期！");
            }
        } else {
            throw new ServiceException("邮箱已激活，请登录！");
        }
    }
    @RequestMapping(value = "/verifyPhone",method = RequestMethod.GET)
    public String getNumber(String phoneNumber) {
        UserInformation byPhoneNumber = userInformationDao.findByPhoneNumber(phoneNumber);
        if (null==byPhoneNumber){
            return ValueUtil.toJson(HttpStatus.SC_OK,"该号未注册");
        }else{
             return ValueUtil.toError("500","该号已注册");
        }
    }

}
