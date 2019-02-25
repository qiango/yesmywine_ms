package com.yesmywine.goods.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entity.GoodsLabel;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by hz on 8/4/17.
 */
public interface GoodsLabelService extends BaseService<GoodsLabel,Integer> {

    String create(Integer labelId,String goodsIds)throws YesmywineException;
}
