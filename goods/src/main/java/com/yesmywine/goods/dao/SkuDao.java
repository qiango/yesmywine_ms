package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.Sku;
import com.yesmywine.goods.entityProperties.Category;
import com.yesmywine.goods.entityProperties.Supplier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by by on 2016/12/22.
 */
@Repository
public interface SkuDao extends BaseRepository<Sku, Integer> {
//    List<Sku> findByGoodsId(Long goodsId);
//
//    void deleteByGoodsId(Long goodsId);
//
//    Sku findBySkuId(Integer skuId);

    List<Sku> findByCategory(Category categoryId);
    List<Sku> findBySupplier(Supplier supplierId);
    Sku findBySkuName(String skuName);

    Sku findByCode(String code);
}
