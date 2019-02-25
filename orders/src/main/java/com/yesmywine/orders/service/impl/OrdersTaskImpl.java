package com.yesmywine.orders.service.impl;

import com.yesmywine.orders.service.OrderService;
import com.yesmywine.orders.service.OrdersTaskService;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by wangdiandian on 2017/2/13.
 */
@Service
public class OrdersTaskImpl implements OrdersTaskService {
    @Autowired
    private OrderService orderService;

    public void cancelOrders() {//订单超过30分钟自动取消
        System.out.println("定时任务开始......");
        long begin = System.currentTimeMillis();
        System.out.println("系统当前时间");
        try {
            orderService.cancels();
        } catch (YesmywineException e) {
            e.getMsg();
        }
        long end = System.currentTimeMillis();
        System.out.println("定时任务结束，共耗时：[" + (end - begin) / 1000 + "]秒");
    }

    public void confirmOrders() {//每天凌晨订单超过n天后自动确认
        System.out.println("定时任务开始......");
        long begin = System.currentTimeMillis();
        System.out.println("系统当前时间");
        try {
            orderService.confirms();
        } catch (YesmywineException e) {
            e.getMsg();
        }
        long end = System.currentTimeMillis();
        System.out.println("定时任务结束，共耗时：[" + (end - begin) / 1000 + "]秒");
    }
}
