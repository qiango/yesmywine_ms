package com.yesmywine.cms.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.cms.entity.PositionEntity;

/**
 * Created by yly on 2017/2/10.
 */
//广告位接口
public interface PositionService extends BaseService<PositionEntity, Integer> {
    //新增广告位
    String savePosition(PositionEntity position, String adverIds);

    //根据广告位ID删除广告位
    public String delete(Integer positionId) throws Exception;

    //修改广告位
    public String update(PositionEntity position, String adverIds);

    //根据广告位ID查询广告位
    public PositionEntity findOne(Integer positionId);

//    //查询所有广告位
//    public PageModel findAll(PageModel pageModel);

    public Boolean used(Integer positionId);
}
