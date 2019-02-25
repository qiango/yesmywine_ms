package com.yesmywine.orders.controller;

import com.yesmywine.orders.service.ReceiveOMSService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wangdiandian on 2017/6/7.
 */
@RestController
@RequestMapping("/oms/orders/status")
public class ReceiveOMSController {
    @Autowired
    private ReceiveOMSService receiveOMSService;

    @RequestMapping(value = "deliverdGoodsOrders",method = RequestMethod.POST)
    public String status( Long orderNo,String waybillNumber,String shipperCode,String deliverdTime) {// 接收OMS信息跟新发货订单状态
            try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, receiveOMSService.status(orderNo,waybillNumber,shipperCode,deliverdTime));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }

    @RequestMapping(value = "returnGoodsOrders",method = RequestMethod.POST)
    public String returnOrders(String returnNo,String dealTime, HttpServletRequest request) {//接收OMS信息跟新订单退换货状态
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, receiveOMSService.returnOrders(returnNo,dealTime,request));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    @RequestMapping(value = "cancelGoodsOrders",method = RequestMethod.POST)
    public String cancelOrders(Long orderNo,Integer status) {//接收OMS信息跟新订单取消状态
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, receiveOMSService.cancelOrders(orderNo,status));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    }
