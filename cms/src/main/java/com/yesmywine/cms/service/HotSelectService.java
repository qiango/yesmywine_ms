package com.yesmywine.cms.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.cms.entity.HotSelect;

/**
 * Created by hz on 2017/5/16.
 */
public interface HotSelectService extends BaseService<HotSelect, Integer> {

    String insert(String name);

    String delete(Integer id);
}
