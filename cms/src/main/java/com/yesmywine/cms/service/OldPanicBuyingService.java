package com.yesmywine.cms.service;

/**
 * Created by hz on 2017/5/16.
 */
public interface OldPanicBuyingService {

    Object findOne(Integer id);

    Object findAll();

    Object getShuffling(Integer pageNo, Integer pageSize);

    String insert(Integer activityId, Integer status, String jsonString);

    String update(Integer id, Integer activityId, Integer status, String jsonString);

    String delete(Integer id);

    String deleteGoods(Integer id);
}
