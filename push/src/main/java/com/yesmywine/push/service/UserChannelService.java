package com.yesmywine.push.service;


import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.push.entity.Message;
import com.yesmywine.push.entity.UserChannel;

/**
 * Created by wangdiandian on 2017/3/15.
 */
public interface UserChannelService extends BaseService<UserChannel, Integer> {

    String create(String userId, String channelId, Integer deviceType);

    String delete(String userId, String channelId);

}
