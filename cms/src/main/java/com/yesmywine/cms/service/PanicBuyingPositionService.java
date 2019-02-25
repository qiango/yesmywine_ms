package com.yesmywine.cms.service;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface PanicBuyingPositionService {
    Object findAll();

    String insert(Integer positionId);

    String update(Integer id, Integer positionId);

    String delete(Integer id);

}
