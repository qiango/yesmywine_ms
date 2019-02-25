package com.yesmywine.user.webController;

import com.yesmywine.user.service.UserCouponService;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ${shuang} on 2017/4/18.
 */
@RestController
@RequestMapping("/member/userservice/userCoupon")
public class WebUserCouponController {
    @Autowired
    private UserCouponService userCouponService ;

    @RequestMapping(value = "/draw", method = RequestMethod.POST)//优惠券领取
    public String draw(Integer couponId ,HttpServletRequest request){
        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        try {
            return  ValueUtil.toJson(HttpStatus.SC_CREATED,userCouponService.draw(userId,couponId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping( method = RequestMethod.GET)//个人优惠券详情
    public String index(HttpServletRequest request){
        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
              ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

        try {
            return   userCouponService.selfList(userId);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/blance", method = RequestMethod.GET)//结算界面展示可用优惠券
    public String showOneBlance(HttpServletRequest request ,String json){
        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        try {
            return   userCouponService.ListOnBalance(userId,json);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/return", method = RequestMethod.POST)//优惠券退回
    public String couponReturn(Integer userId ,Integer userCouponId){
        try {
            return  ValueUtil.toJson(HttpStatus.SC_CREATED,userCouponService.returns(userId,userCouponId)) ;
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }



}
