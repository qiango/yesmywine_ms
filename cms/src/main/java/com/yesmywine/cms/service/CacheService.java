package com.yesmywine.cms.service;

/**
 * Created by hz on 2017/5/16.
 */
public interface CacheService {
    Object getGoods();

    Object getGoodsFront(Integer pageNo, Integer pageSize);
}
