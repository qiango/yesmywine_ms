package com.yesmywine.evaluation.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.evaluation.bean.OrderGoodsAdvice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by light on 2017/1/9.
 */
@Repository
public interface OrderGoodsAdviceDao extends BaseRepository<OrderGoodsAdvice, Integer> {

    @Query(value = "SELECT count(*) FROM orderGoodsAdvice WHERE goodsId IN (SELECT goods.goodsId FROM goods WHERE goodsName LIKE %:goodsName%)",nativeQuery = true)
    Integer findByName(@Param("goodsName") String goodsName);
}
