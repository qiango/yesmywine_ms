package com.yesmywine.orders.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.orders.entity.Orders;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by wangdiandian on 2017/2/13.
 */
public interface FinishOrdersService extends BaseService<Orders, Long> {
    String paymentSuccess(String paymentType, Long orderNo) throws YesmywineException;
    String pushOMS(Long orderNo ,String userInfo)throws YesmywineException;
    String ordersEvaluate(Long orderNo)throws YesmywineException;
    String goodsEvaluate(Long orderNo)throws YesmywineException;

}
