package com.yesmywine.push.service;

import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.yesmywine.push.entity.UserChannel;

/**
 * Created by light on 2017/4/12.
 */
public interface PushMsgToSingleDeviceService {

    public String pushMsgToSingleDevice(UserChannel userChannel, String title, Integer relevancyType) throws PushClientException,PushServerException;

}
