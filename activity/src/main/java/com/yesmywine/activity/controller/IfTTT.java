package com.yesmywine.activity.controller;

import com.yesmywine.activity.ifttt.IfTTTfactory;
import com.yesmywine.activity.ifttt.ThisThenThat;
import com.yesmywine.activity.entity.IftttRegulation;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@RestController
@RequestMapping("/ifttt")
public class IfTTT {

    @Autowired
    private IfTTTfactory ifTTTfactory;
    @Autowired
    private ThisThenThat thisThenThat;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String iftttGoods(@RequestParam Map<String, Object> params, IftttRegulation regulation,Integer shareId) throws YesmywineException {
        return ValueUtil.toJson(thisThenThat.runGoods(regulation, params,shareId));
    }

    public Map<String,Object> runCart(@RequestParam Map<String, Object> params, IftttRegulation regulation,Integer shareId) throws YesmywineException {
        thisThenThat.runGoods(regulation, params,shareId);
        return params;
    }

    public String getRegulation(@RequestParam Map<String, Object> params, Integer discountId) {
//        thisThenThat.getRegulation(discountId, params)
        return ValueUtil.toJson("");
    }

    @RequestMapping(method = RequestMethod.GET)
    public String iftttOrder(Double totalPrice) {
        return ValueUtil.toJson(thisThenThat.runOrder(totalPrice));
    }


}
