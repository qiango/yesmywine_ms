package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.FlashPurchaseSecent;
import com.yesmywine.cms.entity.PanicBuyingSecent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface FlashPurchaseSecentDao extends BaseRepository<FlashPurchaseSecent,Integer> {

    List<FlashPurchaseSecent> findByFlashPurchaseFirstId(Integer flashPurchaseFirstId);

    Page<FlashPurchaseSecent> findByFlashPurchaseFirstId(Integer flashPurchaseFirstId, Pageable pageable);

    List<FlashPurchaseSecent> findByGoodsIdAndFlashPurchaseFirstId(Integer goodsId, Integer flashPurchaseFirstId);

    void deleteByFlashPurchaseFirstId(Integer flashPurchaseFirstId);

    Page<FlashPurchaseSecent> findAll(Pageable pageable);
}
