package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.FlashPurchaseSecent;
import com.yesmywine.cms.entity.HomePagePanicBuyingSecent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface HomePagePanicBuyingSecentDao extends BaseRepository<HomePagePanicBuyingSecent,Integer> {

    List<HomePagePanicBuyingSecent> findByHomePagePanicBuyingFirstId(Integer homePagePanicBuyingFirstId);

    Page<HomePagePanicBuyingSecent> findByHomePagePanicBuyingFirstId(Integer homePagePanicBuyingFirstId, Pageable pageable);

    List<HomePagePanicBuyingSecent> findByGoodsIdAndHomePagePanicBuyingFirstId(Integer goodsId, Integer homePagePanicBuyingFirstId);

    void deleteByHomePagePanicBuyingFirstId(Integer homePagePanicBuyingFirstId);
}
