package com.yesmywine.sso.controller;


import com.alibaba.fastjson.JSONObject;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.sso.bean.ChargePointRule;
import com.yesmywine.sso.bean.OrderPointRule;
import com.yesmywine.sso.dao.ChargePointRuleDao;
import com.yesmywine.sso.dao.OrderPointRuleDao;
import com.yesmywine.sso.dao.WebDescribeDao;
import com.yesmywine.sso.service.WebDescribeService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by ${shuang} on 2017/6/15.
 */
@RestController
@RequestMapping("/sso/webDescribe")
public class WebDescribeController {//网站设置
    @Autowired
    private WebDescribeService webDescribeService;
    @Autowired
    private WebDescribeDao webDescribeDao;
    @Autowired
    private ChargePointRuleDao chargePointRuleDao;
    @Autowired
    private OrderPointRuleDao orderPointRuleDao;


    @RequestMapping( method = RequestMethod.GET)
    public String show(HttpServletRequest request) {//获取
        String userInfo = null;
        try {
            userInfo = UserUtils.getUserInfo(request).toJSONString();
        } catch (YesmywineException e) {
            e.printStackTrace();
        }
        if(ValueUtil.isEmpity(userInfo)){
            return "未登录";
        }
        JSONObject jsonObject = ValueUtil.toJsonObject(webDescribeDao.findAll().get(0));
        ChargePointRule chargePointRule = chargePointRuleDao.findByStatus(1);
        jsonObject.put("chargePoint",chargePointRule.getMultiple());
        OrderPointRule orderPointRule = orderPointRuleDao.findByStatus(1);
        jsonObject.put("orderPoint",orderPointRule.getMultiple());
      return   ValueUtil.toJson(HttpStatus.SC_OK,jsonObject);
    }


    @RequestMapping( method = RequestMethod.POST)
    public String index(@RequestParam Map<String, String> params,HttpServletRequest request) {//插入
        String userInfo = null;
        try {
            userInfo = UserUtils.getUserInfo(request).toJSONString();
        } catch (YesmywineException e) {
            e.printStackTrace();
        }
        if(ValueUtil.isEmpity(userInfo)){
            return "未登录";
        }
        try {
            webDescribeDao.deleteAll();
            boolean result= params.get("days").matches("[0-9]+");
            boolean result1= params.get("points").matches("[0-9]+");
            boolean result2= params.get("orderPoint").matches("[0-9]+");
            boolean result3= params.get("chargePoint").matches("[0-9]+");
            if(Integer.valueOf(params.get("days"))<0||Integer.valueOf(params.get("points"))<0){
                return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"天数或者成长值不能未负数");
            }
            if(result==false){
                return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"天数格式不对");
            }
            if(result1==false||result2==false||result3==false){
                return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"积分格式不对");
            }
            ValueUtil.verify(params.get("webDescribe"));
            ValueUtil.verify(params.get("keyword"));
            ValueUtil.verify(params.get("webTitle"));
            ValueUtil.verify(params.get("days"));
            ValueUtil.verify(params.get("points"));
            ValueUtil.verify(params.get("orderPoint"));
            ValueUtil.verify(params.get("chargePoint"));
            return ValueUtil.toJson(HttpStatus.SC_CREATED,webDescribeService.insert(params));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

}
