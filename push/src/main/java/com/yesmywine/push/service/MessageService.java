package com.yesmywine.push.service;


import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.push.entity.Message;

/**
 * Created by wangdiandian on 2017/3/15.
 */
public interface MessageService extends BaseService<Message, Integer> {

    Message findOne(Integer id);

    Object findAll(Integer status);

    String create(Message message);

    String update(Message message);

    String delete(Integer id);

}
