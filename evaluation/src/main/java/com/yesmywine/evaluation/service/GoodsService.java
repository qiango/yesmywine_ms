package com.yesmywine.evaluation.service;


import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.evaluation.bean.Goods;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by hz on 2/10/17.
 */
public interface GoodsService extends BaseService<Goods, Integer> {

    String synchronous(String jsonData)throws YesmywineException;

}
