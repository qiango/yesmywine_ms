package com.yesmywine.cms.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.cms.entity.AdverPositionEntity;

/**
 * Created by yly on 2017/2/10.
 */
public interface AdverPositionService extends BaseService<AdverPositionEntity, Integer> {
    //保存中间表
    public String saveAP(AdverPositionEntity adverPosition) throws Exception;

    Object findByAdverEntity(Integer adverId);

    Object findByPositionEntity(Integer positionId);

    Object findByPositionEntityShowAdver(Integer positionId);

    public String deleteAP(Integer adverId, Integer positionId) throws Exception;

}
