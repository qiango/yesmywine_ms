package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.HistoryPrice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 4/12/17.
 */
@Repository
public interface HistoryPriceDao extends BaseRepository<HistoryPrice,Integer> {

    @Query("select max(id) from HistoryPrice where goodsId = :goodsId")
    Integer findId(@Param("goodsId")Integer goodsId);

    List<HistoryPrice> findByGoodsId(Integer goodsId);
}
