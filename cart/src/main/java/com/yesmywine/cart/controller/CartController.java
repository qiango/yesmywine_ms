package com.yesmywine.cart.controller;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cart.service.CartService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by whao on 2016/12/19.
 */
@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    //登录后新增商品到购物车
    @RequestMapping(value = "/member/cart/cart/addGoodsToCart",method = RequestMethod.GET)
    public String addGoodsToCart(@RequestParam Map<String, String> param, HttpServletRequest request) {
        try {
            String userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                ValueUtil.isError("未登录");
            }
            return cartService.addCart(param,userInfo);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/member/cart/cart",method = RequestMethod.POST)
    public String memberAdd(@RequestParam Map<String, String> param, HttpServletRequest request) {//已登录购物车的添加商品
        try {
            String userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                ValueUtil.isError("未登录");
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, cartService.memberAdd(param,userInfo));

        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }


    // 删除购物车中一个商品
    @RequestMapping( value = "/member/cart/cart/delete",method = RequestMethod.GET)
    public String deleteGoodsFromCart(Integer goodsId,String appType,HttpServletRequest request,Integer childGoodsId) {
        try {
            String userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                ValueUtil.isError("未登录");
            }//cancel
            return cartService.deleteGoodsFromCart(userInfo, goodsId,appType,childGoodsId);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }
    //批量删除购物车选中的商品
    @RequestMapping( value = "/member/cart/cart/batchdDlete",method = RequestMethod.GET)
    public String batchdDlete(HttpServletRequest request) {
        try {
            String userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                ValueUtil.isError("未登录");
            }//cancel
            return cartService.batchdDleteGoodsFromCart(userInfo);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }

    // 已登录查询用户购物车中的商品
    @RequestMapping(value = "/member/cart/cart",method = RequestMethod.GET)
    public String memberCartGoodsList(HttpServletRequest request) {
        try {
            String  userInfo=null;
            JSONObject userInfoObj = UserUtils.getUserInfo(request);
            if(userInfoObj!=null){
                userInfo = userInfoObj.toJSONString();
            }
            return cartService.queryCartGoodsList(userInfo);

        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    // 未登录查询用户购物车中的商品
    @RequestMapping(value = "/web/cart/cart",method = RequestMethod.GET)
    public String webCartGoodsList(String cartCache) {
        try {
            return cartService.webCartGoodsList(cartCache);

        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    @RequestMapping(value = "/web/cart/order",method = RequestMethod.GET)
    public String webCartOrders(Integer userId,String username) {//结算界面刷新使用
        try {
            return cartService.webCartOrders(userId,username);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    @RequestMapping(value = "/cart/order/itf",method = RequestMethod.GET)
    public String cartOrders(Integer userId,String username) {//点击提交订单内部使用
        try {
            return cartService.cartOrders( userId,username);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    //清空购物车
    @RequestMapping(value = "/member/cart/cart/clear", method = RequestMethod.DELETE)
    public String clearCart(HttpServletRequest request) {
        try {
        Integer userId = UserUtils.getUserId(request);
        if(ValueUtil.isEmpity(userId)) {
            ValueUtil.isError("登录失效");
        }
        return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, cartService.clearCart(userId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    //查看购物车中商品数量
    @RequestMapping(value = "/member/cart/cart/queryGoodsAmount", method = RequestMethod.GET)
    public String queryGoodsAmount(HttpServletRequest request) {
        try {
            Integer userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)) {
                ValueUtil.isError("登录失效");
            }
            return ValueUtil.toJson(HttpStatus.SC_OK, cartService.queryGoodsAmount(userId));

        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    //下单后删除购物车中选购的商品
    @RequestMapping(value = "/cart/cart/deleteOrderGoods/itf", method = RequestMethod.POST)
    public String deleteOrderGoods(Integer userId) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, cartService.deleteOrderGoods(userId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    //用户在购物车对某件商品其数量加减以及勾选
    @RequestMapping(value = "/member/cart/cart/addAndSubtract", method = RequestMethod.GET)
    public String addAndSubtract(Integer status,Integer goodsId,Integer count,HttpServletRequest request,Integer childGoodsId,Integer regulationId,Integer selectAll) {
            try {
                String userInfo = UserUtils.getUserInfo(request).toJSONString();
                if(ValueUtil.isEmpity(userInfo)) {
                    ValueUtil.isError("登录失效");
                }
                return  cartService.addAndSubtract(status,goodsId,count,userInfo,childGoodsId,regulationId,selectAll);
            } catch (YesmywineException e) {
                return ValueUtil.toError(e.getCode(), e.getMessage());
            }
    }

    /*
    *@Author:diandian
    *@Email:
    *@Date:  2017/7/5
    *@Param
    *@Description:购物车结算
    */
    @RequestMapping(value = "/member/cart/cart/settlement", method = RequestMethod.GET)
    public String settlement(HttpServletRequest request) {
        try {
            String userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)) {
                ValueUtil.isError("登录失效");
            }
            return  cartService.settlement(userInfo);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/member/cart/enable/library",method = RequestMethod.GET)
    public String library(HttpServletRequest request) {//是否支持存酒库
        try {
            Integer userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)) {
                ValueUtil.isError("登录失效");
            }
            return ValueUtil.toJson(HttpStatus.SC_OK, cartService.library(userId));
        } catch (YesmywineException e) {

            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    @RequestMapping(value = "/member/cart/enable/cashOnDelivery",method = RequestMethod.GET)
    public String cashOnDelivery(HttpServletRequest request) {//是否支持货到付款
        try {
            Integer userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)) {
                ValueUtil.isError("登录失效");
            }
            return ValueUtil.toJson(HttpStatus.SC_OK,cartService.cashOnDelivery(userId));
        } catch (YesmywineException e) {

            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
}
