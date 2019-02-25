package com.yesmywine.goods.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entity.PreSkuStatistics;

import java.util.List;

/**
 * Created by by on 2017/7/12.
 */
public interface PreSkuStatisticsService extends BaseService<PreSkuStatistics,Integer> {
    List<PreSkuStatistics> findByStockCountNotEqualPreCount();

    String updateSkuStockCount(String skuCodes, Integer counts);

    List<PreSkuStatistics> findSkuIsPresell(String skuCode);
}
