package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.BoutiqueFirst;

/**
 * Created by wangdiandian on 2017/6/1.
 */
public interface BoutiqueFirstDao extends BaseRepository<BoutiqueFirst,Integer> {
    BoutiqueFirst findByName(String name);

}
