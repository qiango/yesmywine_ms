package com.yesmywine.push.service;

import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;

/**
 * Created by light on 2017/4/12.
 */
public interface PushMsgToAllService {

    Object pushMsgToAllAnd(Integer id) throws PushClientException,PushServerException;

    Object pushMsgToAllIOS(Integer id) throws PushClientException,PushServerException;
}
