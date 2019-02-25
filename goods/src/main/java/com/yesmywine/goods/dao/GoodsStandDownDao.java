package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.GoodsStandDown;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 4/12/17.
 */
@Repository
public interface GoodsStandDownDao  extends BaseRepository<GoodsStandDown, Integer> {


    @Query(value = "SELECT count(*) FROM goodsStandDown a WHERE a.goodsId in(SELECT b.id FROM goods b WHERE b.goodsName LIKE %:goodsName% And b.goodsCode LIKE %:goodsCode%) AND a.checkState=:checkState",nativeQuery = true)
    Integer findGoods(@Param("goodsName") String goodsName,@Param("goodsCode") String goodsCode,@Param("checkState") Integer checkState);

    @Query(value = "SELECT count(*) FROM goodsStandDown a WHERE a.goodsId in(SELECT b.id FROM goods b WHERE b.goodsName LIKE %:goodsName% And b.goodsCode LIKE %:goodsCode%)",nativeQuery = true)
    Integer findGoodsNameAndGoodsCode(@Param("goodsName") String goodsName,@Param("goodsCode") String goodsCode);

    @Query(value = "SELECT count(*) FROM goodsStandDown a WHERE a.goodsId in(SELECT b.id FROM goods b WHERE b.goodsName LIKE %:goodsName%) AND a.checkState=:checkState",nativeQuery = true)
    Integer findGoodsNameAndstatus(@Param("goodsName") String goodsName,@Param("checkState") Integer checkState);

    @Query(value = "SELECT count(*) FROM goodsStandDown a WHERE a.goodsId in(SELECT b.id FROM goods b WHERE b.goodsName LIKE %:goodsName%)",nativeQuery = true)
    Integer findGoodsName(@Param("goodsName") String goodsName);

    @Query(value = "SELECT count(*) FROM goodsStandDown a WHERE a.goodsId in(SELECT b.id FROM goods b WHERE b.goodsCode LIKE %:goodsCode%) AND a.checkState=:checkState",nativeQuery = true)
    Integer findGoodsCodeAndstatus(@Param("goodsCode") String goodsCode,@Param("checkState") Integer checkState);

    @Query(value = "SELECT count(*) FROM goodsStandDown a WHERE a.goodsId in(SELECT b.id FROM goods b WHERE b.goodsCode LIKE %:goodsCode%)",nativeQuery = true)
    Integer findGoodsCode(@Param("goodsCode") String goodsCode);

    List<GoodsStandDown> findByCheckState(Integer checkState);
    @Query("select count(*) from GoodsStandDown")
    Integer findBycount();
}
