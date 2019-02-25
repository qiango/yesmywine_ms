package com.yesmywine.push.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.push.dao.MessageDao;
import com.yesmywine.push.dao.UserChannelDao;
import com.yesmywine.push.entity.Message;
import com.yesmywine.push.entity.UserChannel;
import com.yesmywine.push.service.MessageService;
import com.yesmywine.push.service.UserChannelService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by wangdiandian on 2017/3/15.
 */
@Service
public class UserChannelServiceImpl extends BaseServiceImpl<UserChannel, Integer> implements UserChannelService {

    @Autowired
    private UserChannelDao userChannelDao;


    @Override
    public String create(String userId, String channelId, Integer deviceType) {
        try {
            UserChannel byUserIdAndChannelId = this.userChannelDao.findByUserIdAndChannelId(userId, channelId);
            if(ValueUtil.notEmpity(byUserIdAndChannelId)){
                return "success";
            }
            UserChannel userChannel = new UserChannel();
            userChannel.setUserId(userId);
            userChannel.setChannelId(channelId);
            userChannel.setDeviceType(deviceType);
            this.userChannelDao.save(userChannel);
        }catch (Exception e){
            return "erro";
        }
        return "success";
    }

    @Override
    public String delete(String userId, String channelId) {
        try {
            UserChannel byUserIdAndChannelId = this.userChannelDao.findByUserIdAndChannelId(userId, channelId);
            if(ValueUtil.isEmpity(byUserIdAndChannelId)){
                return "success";
            }else {
                this.userChannelDao.delete(byUserIdAndChannelId);
            }
        }catch (Exception e){
            return "erro";
        }
        return "success";
    }
}
