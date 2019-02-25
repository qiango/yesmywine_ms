package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.Goods;
import com.yesmywine.cms.entity.PanicBuyingSecent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface PanicBuyingSecentDao extends BaseRepository<PanicBuyingSecent,Integer> {

    List<PanicBuyingSecent> findByPanicBuyingFirstId(Integer panicBuyingFirstId);

    List<PanicBuyingSecent> findByGoodsIdAndPanicBuyingFirstId(Integer goodsId,Integer panicBuyingFirstId);

    void deleteByPanicBuyingFirstId(Integer panicBuyingFirstId);

    Page<PanicBuyingSecent> findAll(Pageable pageable);
}
