package com.yesmywine.orders.service;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.orders.entity.Orders;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/2/10.
 */
public interface OrderService extends BaseService<Orders, Long> {
    Map<String, Object> creatOrder(@RequestParam Map<String, String> param,String userInfo,HttpServletRequest request) throws YesmywineException;

    String cancel(Long id, String userInfo) throws YesmywineException;

    String confirm(Long id) throws YesmywineException;

    Map<String, Object> updateLoad(Long orderId, HttpServletRequest request) throws YesmywineException;

    String cancels() throws YesmywineException;//定时任务 30分钟后未支付的订单取消

    String confirms() throws YesmywineException;//定时任务 订单配送完成 客户 在几天后自动确认收货

    Map<String, Object> viewLogistics(Long orderNo,Integer userId )throws YesmywineException;

    PageModel findgOrdersPage(String goodsName, Integer pageNo, Integer pageSize,Integer userId);

    String giftCardDetaile(Long orderNo,Integer goodsId)throws YesmywineException;//支付完单后礼品卡显示详情

}


