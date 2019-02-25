package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.SaleSecent;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@Repository
public interface SaleSecentDao extends BaseRepository<SaleSecent,Integer> {
    List<SaleSecent> findBySaleFirstId(Integer saleFirstId);
    SaleSecent findByGoodsIdAndSaleFirstId(Integer goods,Integer saleFirstId);
    void deleteBySaleFirstId(Integer saleFirstId);

}
