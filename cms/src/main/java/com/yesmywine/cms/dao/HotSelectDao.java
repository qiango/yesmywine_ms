package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.HotSelect;

/**
 * Created by hz on 2017/5/16.
 */
public interface HotSelectDao extends BaseRepository<HotSelect,Integer> {

    HotSelect findByName(String name);
}
