package com.yesmywine.cms.service;

/**
 * Created by hz on 2017/5/16.
 */
public interface OldPositionService {

    Object findAll();

    String insert(Integer firstPositionId, Integer secentPositionId);

    String update(Integer id, Integer firstPositionId, Integer secentPositionId);

    String delete(Integer id);

}
