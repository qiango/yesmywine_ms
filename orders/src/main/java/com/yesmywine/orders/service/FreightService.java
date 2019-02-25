package com.yesmywine.orders.service;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.orders.entity.Freight;
import com.yesmywine.util.error.YesmywineException;

import java.util.Map;

/**
 * Created by hz on 6/8/17.
 */
public interface FreightService extends BaseService<Freight, Integer> {

    String saveAdviceType(Map<String, String> params , String json)throws YesmywineException;

    String deleteById(Integer id)throws YesmywineException;

    JSONObject calculate(String areaId, double orderAmount)throws YesmywineException;
}

