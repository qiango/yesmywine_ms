package com.yesmywine.activity.ifttt.service;

import com.yesmywine.activity.ifttt.entity.IftttEntity;
import com.yesmywine.base.record.biz.BaseService;

import java.util.List;

/**
 * Created by SJQ on 2017/6/16.
 */
public interface IftttService extends BaseService<IftttEntity,Integer> {
    IftttEntity findById(Integer actionId);

    List<IftttEntity> findAll(List<Integer> taIds);

    List<IftttEntity> findByType(String type);

    List<IftttEntity> findBytaIds(List<Integer> taIds);
}
