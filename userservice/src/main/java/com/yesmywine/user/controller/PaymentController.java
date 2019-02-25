package com.yesmywine.user.controller;

import com.yesmywine.user.service.BeanFlowService;
import com.yesmywine.user.service.ChargeService;
import com.yesmywine.user.service.PaymentOfRefunds;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by ${shuang} on 2017/6/23.
 */
@RestController
@RequestMapping("/web/userservice/payment")
public class PaymentController {

    @Autowired
    private ChargeService chargeService;
    @Autowired
    private BeanFlowService beanFlowService;
    @Autowired
    private PaymentOfRefunds paymentOfRefunds;

    @RequestMapping(value = "/beanConsume", method = RequestMethod.POST)
    public String consume(Integer userId, Integer bean, String orderNumber, String channelCode) {//只消耗酒豆
        try {
            ValueUtil.verify(userId);
            ValueUtil.verify(bean);
            ValueUtil.verify(channelCode);
            ValueUtil.verify(orderNumber);
            return beanFlowService.consume(userId, bean, orderNumber, channelCode);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/remainConsume", method = RequestMethod.POST)
    public String consume(@RequestParam Map<String, String> params, Integer userId) {//只消耗余额

        try {
            ValueUtil.verify(params.get("payMoney"));
            ValueUtil.verify(params.get("orderNumber"));
            ValueUtil.verify(userId);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, chargeService.consume(params, userId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/beanRemain", method = RequestMethod.POST)
    public String beanRemain(@RequestParam Map<String, String> params, Integer userId) {//余额酒豆都用了

        try {
            ValueUtil.verify(params.get("payMoney"));
            ValueUtil.verify(params.get("orderNumber"));
            ValueUtil.verify(params.get("consumeBean"));
            ValueUtil.verify(userId);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, paymentOfRefunds.payment(params, userId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/returns", method = RequestMethod.POST)
    public String returns(@RequestParam Map<String, String> params, Integer userId) {//退货
        if (ValueUtil.isEmpity(params.get("returnBean"))) {
            params.put("returnBean", "0");
        }
        if (ValueUtil.isEmpity(params.get("returnMoney"))) {
            params.put("returnMoney", "0");
        }
        try {
//            Integer userId = UserUtils.getUserId(request);
            ValueUtil.verify(params.get("returnBean"));
            ValueUtil.verify(params.get("returnMoney"));
            ValueUtil.verify(params.get("orderNumber"));
            ValueUtil.verify(params.get("returnPoint"));
            ValueUtil.verify(userId);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, paymentOfRefunds.returns(params, userId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/returnsandpoint", method = RequestMethod.POST)
    public String returnpoint(@RequestParam Map<String, String> params, Integer userId) {//退货不退积分
        if (ValueUtil.isEmpity(params.get("returnBean"))) {
            params.put("returnBean", "0");
        }
        if (ValueUtil.isEmpity(params.get("returnMoney"))) {
            params.put("returnMoney", "0");
        }
        try {
//            Integer userId = UserUtils.getUserId(request);
            ValueUtil.verify(params.get("returnBean"));
            ValueUtil.verify(params.get("returnMoney"));
            ValueUtil.verify(params.get("orderNumber"));
            ValueUtil.verify(userId);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, paymentOfRefunds.returnsAndPoint(params, userId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}
