package com.yesmywine.goods.service;

import com.yesmywine.goods.entity.Goods;

/**
 * Created by hz on 2/10/17.
 */
public interface SearchService {
    void insert() throws Exception;

    String delete(Integer goodsId) throws Exception;

    Object searchGoods(String keyWords, String screen, String sort, String order ,Integer page, Integer rows) throws Exception;

    Object searchProp(String keyWords, String screen) throws Exception;

    Object searchGoodsByCategoryId(String keyWords, String screen, String sort, String order ,Integer page, Integer rows) throws Exception;

    Object searchPropByCategoryId(String keyWords, String screen) throws Exception;

    void saveGoodsSearch(Goods goods) throws Exception;
}
