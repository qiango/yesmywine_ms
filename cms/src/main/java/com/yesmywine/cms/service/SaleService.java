package com.yesmywine.cms.service;

import com.yesmywine.util.error.YesmywineException;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface SaleService {
    Object findOne(Integer id);

    Object findAll();

    String insert(String name, Integer positionId, String title, String jsonString);

    String update(Integer id, String name, Integer positionId, String title, String jsonString);

    String deleteFirst(Integer id);

    String deleteSecent(Integer id);

    com.alibaba.fastjson.JSONObject getShuffling() throws YesmywineException;
}
