
package com.yesmywine.cms.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.cms.entity.Goods;
import com.yesmywine.util.error.YesmywineException;


/**
 * Created by hz on 2/10/17.
 */

public interface GoodsService extends BaseService<Goods, Integer> {

    Boolean synchronous(String jsonDate)throws YesmywineException;

    Goods findById(Integer id);
}

