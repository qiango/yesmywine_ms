package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.HomePagePanicBuyingSecent;
import com.yesmywine.cms.entity.OldPanicBuyingSecent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface OldPanicBuyingSecentDao extends BaseRepository<OldPanicBuyingSecent,Integer> {

    List<OldPanicBuyingSecent> findByOldPanicBuyingFirstId(Integer oldPanicBuyingFirstId);

    Page<OldPanicBuyingSecent> findByOldPanicBuyingFirstId(Integer oldPanicBuyingFirstId, Pageable pageable);

    List<OldPanicBuyingSecent> findByGoodsIdAndOldPanicBuyingFirstId(Integer goodsId, Integer oldPanicBuyingFirstId);

    void deleteByOldPanicBuyingFirstId(Integer oldPanicBuyingFirstId);
}
