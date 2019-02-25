
package com.yesmywine.goods.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.dao.ChannelDao;
import com.yesmywine.goods.entityProperties.Channel;
import com.yesmywine.goods.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by hz on 1/6/17.
 */
@Service
@Transactional
public class ChannelServiceImpl extends BaseServiceImpl<Channel, Integer> implements ChannelService {

    @Autowired
    private ChannelDao channelDao;

    @Override
    public Boolean synchronous(Integer id, String name, Integer synchronous) {
        Boolean resutl = false;
        if(0 == synchronous || 1 == synchronous){
            resutl = this.save(id, name);
        }else {
            resutl = this.delete(id);
        }
        return resutl;
    }

    public Boolean save(Integer id, String name){
        try {
            Channel channel = new Channel();
            channel.setId(id);
            channel.setChannelName(name);
            this.channelDao.save(channel);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public Boolean delete(Integer id){
        try {
            this.channelDao.delete(id);
        }catch (Exception e){
            return false;
        }
        return true;
    }

}
