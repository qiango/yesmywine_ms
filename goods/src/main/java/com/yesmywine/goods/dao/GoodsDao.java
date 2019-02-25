package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.bean.Item;
import com.yesmywine.goods.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hz on 12/8/16.
 */
@Repository
public interface GoodsDao extends BaseRepository<Goods, Integer> {

    //    Goods findByItemAndSkuIdString(Item item, String skuIdString);
    Goods findByPassGoodsId(Integer passId);

    Goods findByGoodsName(String name);

    List<Goods>findByGoodsNameContaining(String name);
    //    Goods findByGoodsNameContaining(String name);
    Goods findByGoodsCode(String code);

    Goods findByPassGoodsIdAndChannelId(Integer goodsId,Integer channelId);

    Page<Goods> findByCategoryIdOrderByCommentsDesc(Pageable pageable,Integer categoryId);

    Page<Goods> findByCategoryId(Pageable pageable,Integer categoryId);

    List<Goods> findByCategoryId(Integer categoryId);

    List<Goods> findByItem(Item item);

    List<Goods> findByOperateAndPreStatusAndEndTimeGreaterThan(Integer saleModel, Integer preStatus, Date now);

    List<Goods> findByOperateAndPreStatus(Integer saleModel, Integer preStatus);

    @Query(value = "select g.* from goods g left join goodsSku gs on g.id=gs.goodsSkuId  where g.goStatus=1 and gs.skuId in (:skuIds) GROUP BY g.id limit 0,:size",nativeQuery = true)
    List<Goods> findBySkuIdIn(@Param("skuIds") List<Integer> skuIdList, @Param("size") Integer size);

    @Query("select count(g) from Goods g where g.categoryId in (:categoryIds)")
    Integer findByCategoryIdIn(@Param("categoryIds") Integer[] categoryIds);

    @Query(value ="SELECT count(*) FROM goods  WHERE item not in (\"luckBage\",\"fictitious\") and operate NOT in(\"1\") AND goods.goStatus=1",nativeQuery = true)
    Integer findByGoods();

    @Query(value ="SELECT count(*) FROM goods  WHERE item not in (\"luckBage\",\"fictitious\") and operate NOT in(\"1\") AND goods.goStatus=1 AND goodsName like %:goodsName_l%",nativeQuery = true)
    Integer findByGoodsl(@Param("goodsName_l") String goodsName_l);
}

