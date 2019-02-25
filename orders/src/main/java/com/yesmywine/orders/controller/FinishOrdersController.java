package com.yesmywine.orders.controller;

import com.yesmywine.jwt.UserUtils;
import com.yesmywine.orders.entity.Orders;
import com.yesmywine.orders.service.FinishOrdersService;
import com.yesmywine.orders.service.OrderService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdiandian on 2016/12/22.
 */
@RestController
public class FinishOrdersController {
    @Autowired
    private FinishOrdersService finishOrdersService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "orders/finish/paymentSuccess/itf", method = RequestMethod.POST)
    public String PaymentSuccess(String paymentType, Long orderNo) {
        try {

            return ValueUtil.toJson(HttpStatus.SC_CREATED, finishOrdersService.paymentSuccess(paymentType, orderNo));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    @RequestMapping(value = "/pushOMS/itf", method = RequestMethod.POST)
    public String pushOMS( Long orderNo,String userInfo) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, finishOrdersService.pushOMS(orderNo,userInfo));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }


    @RequestMapping(value = "web/finish/resPushOMS", method = RequestMethod.POST)
    public String resPushOMS(Long orderId, HttpServletRequest request) {
        try {
            ValueUtil.verify(orderId,"orderId");
//            Integer operator = UserUtils.ifLoginGetId(request);
            Orders orders = orderService.findOne(orderId);
            Integer placeOrderUserId = orders.getUserId();
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("userId",placeOrderUserId);
            String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST,"/userservice/userInfomation", com.yesmywine.httpclient.bean.RequestMethod.get,paramsMap,request);
            String userInfo = ValueUtil.getFromJson(result,"data");
            return ValueUtil.toJson(HttpStatus.SC_CREATED, finishOrdersService.pushOMS(orders.getOrderNo(),userInfo));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    @RequestMapping(value = "/orders/orders/evaluate/itf",method = RequestMethod.POST)//订单评论完修改评论状态内部接口
    public String ordersEvaluate(Long orderNo) {
        try {
            return  ValueUtil.toJson(HttpStatus.SC_CREATED,finishOrdersService.ordersEvaluate(orderNo));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }
    @RequestMapping(value = "/orders/goods/evaluate/itf",method = RequestMethod.POST)//订单商品评价完修改评论状态内部接口
    public String goodsEvaluate(Long orderNo) {
        try {
            return  ValueUtil.toJson(HttpStatus.SC_CREATED,finishOrdersService.goodsEvaluate(orderNo));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }
}
