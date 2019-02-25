package com.yesmywine.orders.task;

import com.yesmywine.orders.service.OrderService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.date.DateUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by by on 2017/8/3.
 */
@RestController
@RequestMapping("/order")
public class OrderTask {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/cancelTimeoutOrder/task",method = RequestMethod.GET)
    public String cancelOrders() {//订单超过30分钟自动取消
        System.out.println("=============================每日凌晨判断是否有超时的未支付订单 开始================================");
//        long begin = System.currentTimeMillis();
//        System.out.println("系统当前时间");
        try {
            orderService.cancels();
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),"每日凌晨判断是否有超时的未支付订单    "+ DateUtil.getNowTime()+"  " +e.getMessage());
        }
//        long end = System.currentTimeMillis();
//        System.out.println("定时任务结束，共耗时：[" + (end - begin) / 1000 + "]秒");
        System.out.println("=============================每日凌晨判断是否有超时的未支付订单 开始================================");
        return ValueUtil.toJson("每日凌晨判断是否有超时的未支付订单    "+ DateUtil.getNowTime());
    }

    @RequestMapping(value = "/confirmOrders/task",method = RequestMethod.GET)
    public String confirmOrders() {//每天凌晨订单超过n天后自动确认
        System.out.println("=============================每日零点判断超时未确认收货的订单 开始================================");
//        System.out.println("定时任务开始......");
//        long begin = System.currentTimeMillis();
//        System.out.println("系统当前时间");
        try {
            orderService.confirms();
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),"每日零点判断超时未确认收货的订单    "+ DateUtil.getNowTime()+"  " +e.getMessage());

        }
        System.out.println("=============================每日零点判断超时未确认收货的订单 开始================================");
//        long end = System.currentTimeMillis();
//        System.out.println("定时任务结束，共耗时：[" + (end - begin) / 1000 + "]秒");
        return ValueUtil.toJson("每日凌晨判断是否有超时的未支付订单    "+ DateUtil.getNowTime());

    }

}
