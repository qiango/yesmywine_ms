package com.yesmywine.cms.service;

import com.yesmywine.util.error.YesmywineException;

/**
 * Created by hz on 2017/5/16.
 */
public interface OldService {

    Object findOne(Integer id);

    Object findAll();

    String insert(String name, Integer positionId, String jsonString);

    String update(Integer id, String name, Integer positionId, String jsonString);

    String deleteFirst(Integer id);

    String deleteSecent(Integer id);

    com.alibaba.fastjson.JSON getShuffling()throws YesmywineException;
}
