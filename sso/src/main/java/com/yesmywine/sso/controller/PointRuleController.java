package com.yesmywine.sso.controller;

import com.yesmywine.sso.service.OrderPointRuleService;
import com.yesmywine.sso.service.chargePointRuleService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ${shuang} on 2017/7/10.
 */
@RestController
@RequestMapping("/sso/point")
public class PointRuleController {

    @Autowired
    private chargePointRuleService chargePointRuleService;
    @Autowired
    private OrderPointRuleService orderPointRuleService;

    @RequestMapping(value = "/itf",method = RequestMethod.GET)
    public String getponit(Double money,String status){

        String result =null;
        if(status.equals("充值")){
            try {
                ValueUtil.verify(money);
                result = chargePointRuleService.getPoint(money);
            } catch (YesmywineException e) {
                return ValueUtil.toError(e.getCode(),e.getMessage());
            }
        } else if(status.equals("下单")){
            try {
                ValueUtil.verify(money);
                result = orderPointRuleService.getPoint(money);
            } catch (YesmywineException e) {
                return ValueUtil.toError(e.getCode(),e.getMessage());
            }
        }
        return ValueUtil.toJson(HttpStatus.SC_OK,result);
    }



}
