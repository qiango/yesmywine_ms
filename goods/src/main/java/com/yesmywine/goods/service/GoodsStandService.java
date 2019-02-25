package com.yesmywine.goods.service;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entity.GoodsStandDown;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by hz on 4/14/17.
 */
public interface GoodsStandService extends BaseService<GoodsStandDown,Integer>{

    PageModel findByGoods(String goodsName,String goodsCode,Integer checkState,Integer pageNo, Integer pageSize)throws YesmywineException;
}
