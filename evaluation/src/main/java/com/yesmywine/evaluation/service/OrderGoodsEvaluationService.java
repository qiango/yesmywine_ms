package com.yesmywine.evaluation.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.evaluation.bean.OrderGoodsEvaluation;
import com.yesmywine.evaluation.bean.Reply;
import com.yesmywine.util.error.YesmywineException;


/**
 * Created by light on 2017/1/9.
 */
public interface OrderGoodsEvaluationService extends BaseService<OrderGoodsEvaluation, Integer> {

    String create(String jsonArray,JSONObject userInfo)throws YesmywineException;

    String updateEvaluation(String idList,Integer status);

    String delelteById(String[] ids);

    String saveReply(Reply reply, Integer evaluationId);

    JSONObject getShuff(Integer type, Integer goodsId)throws YesmywineException;

    JSONObject getShuff2(Integer goodsId)throws YesmywineException;

    JSONObject getShuffNum(Integer goodsId)throws YesmywineException;

    com.alibaba.fastjson.JSONArray getGoodByBuy() throws YesmywineException;

    com.alibaba.fastjson.JSONArray conditionAssessment() throws YesmywineException;

    com.alibaba.fastjson.JSONArray getGoodComment() throws YesmywineException;

}
