package com.yesmywine.activity.service;

import com.yesmywine.activity.entity.GoodsMirroring;

/**
 * Created by SJQ on 2017/6/9.
 */
public interface SynService {
     GoodsMirroring saveGoodsInfo(String jsonData, String goodsId);
}
