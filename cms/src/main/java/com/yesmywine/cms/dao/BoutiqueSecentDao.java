package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.BoutiqueSecent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by wangdiandian on 2017/6/1.
 */
public interface BoutiqueSecentDao extends BaseRepository<BoutiqueSecent,Integer> {
    BoutiqueSecent findByGoodsIdAndBoutiqueFirstId(Integer goodsId, Integer boutiqueFirstId);

    List<BoutiqueSecent> findByBoutiqueFirstId(Integer boutiqueFirstId);

    void deleteByBoutiqueFirstId(Integer boutiqueFirstId);

    Page<BoutiqueSecent> findByBoutiqueFirstId(Integer boutiqueFirstId, Pageable pageable);
}
