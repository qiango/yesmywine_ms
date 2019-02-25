package com.yesmywine.orders.service;

import com.yesmywine.util.error.YesmywineException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wangdiandian on 2017/6/7.
 */
public interface ReceiveOMSService {

    String status(Long orderNo,String waybillNumber,String shipperCode,String deliverdTime)throws YesmywineException;

    String returnOrders (String returnNo,String dealTime, HttpServletRequest request)throws YesmywineException;

    String cancelOrders (Long orderNo,Integer status)throws YesmywineException;

}
