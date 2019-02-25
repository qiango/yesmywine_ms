package com.yesmywine.evaluation.service;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.evaluation.bean.OrderGoodsAdvice;
import com.yesmywine.evaluation.bean.Reply;


/**
 * Created by light on 2017/1/9.
 */
public interface OrderGoodsAdviceService extends BaseService<OrderGoodsAdvice, Integer> {

    String saveAdvice(OrderGoodsAdvice orderGoodsAdvice, Integer goodsId, JSONObject userInfo);

    String updateAdvice(String idList,Integer status);

    String delelteById(String[] ids);

    String saveReply(Reply reply, Integer adviceId);
}
