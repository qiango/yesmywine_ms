package com.yesmywine.activity.ifttt.dao;

import com.yesmywine.activity.entity.GoodsMirroring;
import com.yesmywine.base.record.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by SJQ on 2017/6/9.
 */
public interface GoodsDao extends BaseRepository<GoodsMirroring,Integer> {
    List<GoodsMirroring> findByCategoryIdAndGoStatusAndSaleModelAndGoodsTypeIn(String categoryId, String goStatus, String saleMode, List<String> goodsTypeList);

    List<GoodsMirroring> findByBrandIdAndGoStatusAndSaleModelAndGoodsTypeIn(String brandId, String goStatus, String saleMode, List<String> goodsTypeList);

    GoodsMirroring findByGoodsId(String goodsId);

    List<GoodsMirroring> findByGoodsIdIn(List<String> goodsIdList);

    @Query(value = "select count(*) from goodsMirroring g where g.goodsId in (select irg.targetId from regulationGoods irg where irg.activityId=:activityId and irg.ware=:ware)",nativeQuery = true)
    Integer CountByActivityId(@Param("activityId") Integer activityId, @Param("ware") Integer ware);
}
