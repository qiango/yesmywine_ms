package com.yesmywine.orders.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.orders.entity.Orders;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by wangdiandian on 2017/7/10.
 */
public interface OrderRefreshService extends BaseService<Orders, Long> {
    Map<String, Object> submitPage(@RequestParam Map<String, String> param, String userInfo) throws YesmywineException;
//    String giftCard(Long cardNumber,String password,String userInfo)throws YesmywineException;
//    Map<String, Object> library(String goodsId)throws YesmywineException;
//     Map<String, Object> cashOnDelivery(String goodsId) throws YesmywineException;

}
