package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.PreSkuStatistics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by by on 2017/7/12.
 */
public interface PreSkuStatisticsDao extends BaseRepository<PreSkuStatistics,Integer> {
    @Query(value = "select * from preSkuStatistics pss where pss.stockCount!=pss.preCount ",nativeQuery = true)
    List<PreSkuStatistics> findByStockCountNotEqualsPreCount();

    @Query(value = "select * from preSkuStatistics pss where pss.stockCount!=pss.preCount and pss.skuCode=:skuCode ",nativeQuery = true)
    List<PreSkuStatistics> findSkuIsPresell(@Param("skuCode") String skuCode);
}
