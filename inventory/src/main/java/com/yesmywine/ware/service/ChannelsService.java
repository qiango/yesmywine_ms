package com.yesmywine.ware.service;


import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.ware.entity.Channels;

/**
 * Created by SJQ on 2017/1/9.
 *
 * @Description:
 */
public interface ChannelsService extends BaseService<Channels, Integer> {
    void delete(Integer channelId);
}
