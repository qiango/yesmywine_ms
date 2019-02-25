package com.yesmywine.cms.service;

/**
 * Created by hz on 2017/5/16.
 */
public interface HomePagePositionService {

    Object findAll();

    String insert(Integer bannerPositionId, Integer positionIdOne, Integer positionIdTwo, Integer positionIdThree);

    String update(Integer id, Integer bannerPositionId, Integer positionIdOne, Integer positionIdTwo, Integer positionIdThree);

    String delete(Integer id);
}
