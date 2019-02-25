package com.yesmywine.cms.service;


import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.cms.entity.AdverEntity;
import com.yesmywine.util.error.YesmywineException;

import java.util.Map;

/**
 * Created by yly on 2017/2/10.
 */
//广告素材接口
public interface AdverService extends BaseService<AdverEntity, Integer> {
    //新增广告素材
    public String saveAdver(Map<String, String> params);

    //根据广告素材ID删除广告素材
    public String delete(Integer adverId) throws Exception;

    //修改广告素材
    public String update(Map<String, String> params);

    Object page(String adverId)throws YesmywineException;//替换content
}
