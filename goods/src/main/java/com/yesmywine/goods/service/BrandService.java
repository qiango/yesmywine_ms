package com.yesmywine.goods.service;

/**
 * Created by hz on 2/10/17.
 */
public interface BrandService {
    Integer find(String brandCnName);

    Integer insert(String brandCnName);
}
