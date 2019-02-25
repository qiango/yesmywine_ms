package com.yesmywine.ware.service.impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.ware.dao.ChannelsDao;
import com.yesmywine.ware.entity.Channels;
import com.yesmywine.ware.service.ChannelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by SJQ on 2017/2/10.
 */
@Service
@Transactional
public class ChannelsServiceImpl extends BaseServiceImpl<Channels, Integer> implements ChannelsService {
    @Autowired
    private ChannelsDao channelsDao;

    public void delete(Integer channelId) {
        Channels channels = channelsDao.findOne(channelId);
        channelsDao.delete(channels);
    }
}
