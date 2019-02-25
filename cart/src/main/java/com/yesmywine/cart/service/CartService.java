package com.yesmywine.cart.service;


import com.yesmywine.util.error.YesmywineException;

import java.util.Map;

/**
 * Created by yly on 2017/2/10.
 */
public interface CartService  {

    //登录后新增商品到购物车
    String addCart(Map<String, String> param,String userInfo) throws YesmywineException;

    //登录后新增购物车
    String memberAdd(Map<String, String> param,String userInfo) throws YesmywineException;

    //查询用户购物车中的商品
    String queryCartGoodsList(String userInfo)throws YesmywineException;

    String webCartGoodsList(String cartCache)throws YesmywineException;

    //删除购物车中指定的商品
    String deleteGoodsFromCart(String userInfo, Integer goodsId,String appType,Integer childGoodsId)throws YesmywineException;

    //批量删除购物车选中的商品
    String batchdDleteGoodsFromCart(String userInfo)throws YesmywineException;

    //清空购物车
    String clearCart(Integer userId)throws YesmywineException;

    //查询购物车中的商品数量
    Integer queryGoodsAmount(Integer userId)throws YesmywineException;

    //下单后订单系统要调用我的删除购物车中的商品的接口
    String deleteOrderGoods(Integer userId) throws YesmywineException;

    //用户在购物车对某件商品其数量加减
    String addAndSubtract (Integer status,Integer goodsId,Integer count,String userInfo,Integer childGoodsId,Integer regulationId,Integer selectAll) throws YesmywineException;

    String settlement(String userInfo)throws YesmywineException;//点击去结算校验

    String webCartOrders(Integer userId,String username)throws YesmywineException;

    String cartOrders(Integer userId,String username)throws YesmywineException;

    Map<String, Object> library(Integer userId)throws YesmywineException;

    Map<String, Object> cashOnDelivery(Integer userId) throws YesmywineException;

}
