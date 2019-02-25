package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entityProperties.Channel;
import org.springframework.stereotype.Repository;

/**
 * Created by wangdiandian on 2016/12/9.
 */
@Repository
public interface ChannelDao extends BaseRepository<Channel, Integer> {
}
