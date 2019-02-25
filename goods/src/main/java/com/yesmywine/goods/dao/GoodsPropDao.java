package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.GoodsProp;
import org.springframework.stereotype.Repository;

/**
 * Created by hz on 6/19/17.
 */
@Repository
public interface GoodsPropDao extends BaseRepository<GoodsProp, Integer> {
}
