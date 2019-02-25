package com.yesmywine.orders.service;

import com.yesmywine.util.error.YesmywineException;

/**
 * Created by wangdiandian on 2017/2/13.
 */
public interface OrdersTaskService {
    void cancelOrders() throws YesmywineException;
    void confirmOrders() throws YesmywineException;

}
