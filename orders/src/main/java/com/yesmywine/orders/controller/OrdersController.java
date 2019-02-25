package com.yesmywine.orders.controller;

import com.yesmywine.orders.bean.OrderType;
import com.yesmywine.orders.bean.WhetherEnum;
import com.yesmywine.orders.dao.OrderDetailDao;
import com.yesmywine.orders.entity.OrderDetail;
import com.yesmywine.orders.service.OrderRefreshService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.orders.bean.Payment;
import com.yesmywine.orders.service.OrderService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdiandian on 2016/12/22.
 */
@RestController
@RequestMapping("/member/orders/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRefreshService orderRefreshService;
    @Autowired
    private OrderDetailDao orderDetailDao;

    @RequestMapping(method = RequestMethod.POST)
    public String creatOrder(@RequestParam Map<String, String> param, HttpServletRequest request) {//创建订单
        try {
            String userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                ValueUtil.isError("未登录");
            }
            Object obj = orderService.creatOrder(param,userInfo,request);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, obj);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }


    @RequestMapping(method = RequestMethod.PUT)
    public String cancelOrders(Long id,HttpServletRequest request) {//取消订单
        try {
            String userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                ValueUtil.isError("未登录");
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, orderService.cancel(id,userInfo));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params,Integer pageNo,Integer pageSize,Long id,HttpServletRequest request) throws  Exception{//商城的分页查询
        MapUtil.cleanNull(params);
        if(id!=null){
            return ValueUtil.toJson(HttpStatus.SC_OK, orderService.updateLoad(id,request));
        }
        if(null!=params.get("all")&&params.get("all").toString().equals("true")){
            return ValueUtil.toJson(orderService.findAll());
        }else if(null!=params.get("all")){
            params.remove(params.remove("all").toString());
        }
        if (ValueUtil.notEmpity(params.get("paymentType"))) {
            String paymentType = params.get("paymentType").toString();
            params.remove(params.remove("paymentType").toString());
            switch (paymentType) {
                case "Alipay":
                    params.put("paymentType", Payment.Alipay);
                    break;
                case "WeChat":
                    params.put("paymentType", Payment.WeChat);
                    break;
                case "UnionPay":
                    params.put("paymentType", Payment.UnionPay);
                    break;
                case "CashOnDelivery":
                    params.put("paymentType", Payment.CashOnDelivery);
                    break;
            }
        }
        if (ValueUtil.notEmpity(params.get("comment"))) {
            String comment = params.get("comment").toString();
            params.remove(params.remove("comment").toString());
            switch (comment) {
                case "YES":
                    params.put("WhetherEnum", WhetherEnum.YES);
                    break;
                case "NO":
                    params.put("comment", WhetherEnum.NO);
                    break;
            }
        }
//        params.put("orderType_ne", OrderType.WineStore);
        Integer userId = UserUtils.getUserId(request);
        params.put("userId",userId);
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = orderService.findAll(pageModel);
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
    }

    @RequestMapping(value = "/confirm",method = RequestMethod.PUT)
    public String confirmOrders(Long id,HttpServletRequest request) {//确认订单
        try {
            Integer userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError("未登录");
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, orderService.confirm(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    @RequestMapping(value = "/submitPage",method = RequestMethod.PUT)
    public String submitPage(@RequestParam Map<String, String> param,HttpServletRequest request) {//提交页面各种使用后刷新
        try {
            String userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                ValueUtil.isError("未登录");
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, orderRefreshService.submitPage(param,userInfo));
        } catch (YesmywineException e) {

            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public String indexPage(@RequestParam Map<String, Object> params,Integer pageNo,Integer pageSize,Long id,HttpServletRequest request) throws  ExceptionThread{//分页查询供应商

}
