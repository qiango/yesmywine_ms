package com.yesmywine.activity.ifttt.service;

import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.activity.ifttt.entity.IftttConfig;
import com.yesmywine.base.record.biz.BaseService;

import java.util.List;

/**
 * Created by SJQ on 2017/6/16.
 */
public interface IftttConfigService extends BaseService<IftttConfig,Integer> {
    List<IftttConfig> findByDiscountId(Integer discountId);

    IftttConfig findByDiscountIdAndIsDelete(Integer discountId, DeleteEnum notDelete);
}
