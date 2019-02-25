package com.yesmywine.cms.service;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by wangdiandian on 2017/5/25.
 */
public interface PanicBuyingService {
    Object findOne(Integer id);

    Object findAll();

    Object findGoods(Integer pageNo, Integer pageSize);

    String insert(String name, Integer activityId,String jsonString);

    String update(Integer id, String name, Integer activityId,String jsonString);

    String deleteFirst(Integer id);

    String deleteSecent(Integer id);

    JSONObject getShuffling() throws YesmywineException;
}