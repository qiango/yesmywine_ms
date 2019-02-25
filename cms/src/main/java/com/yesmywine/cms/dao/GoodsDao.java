package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
@Repository
public interface GoodsDao extends BaseRepository<Goods,Integer> {

//    Page<Goods> findAllOrderBySales(Pageable pageable);

    Page<Goods> findAll(Pageable pageable);

    List<Goods>findByCategoryIdOrderBySalesDesc(Integer categoryId);



}
