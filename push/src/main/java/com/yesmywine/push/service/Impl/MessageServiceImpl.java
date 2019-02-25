package com.yesmywine.push.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.push.dao.MessageDao;
import com.yesmywine.push.entity.Message;
import com.yesmywine.push.service.MessageService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * Created by wangdiandian on 2017/3/15.
 */
@Service
public class MessageServiceImpl extends BaseServiceImpl<Message, Integer> implements MessageService {

    @Autowired
    private MessageDao messageDao;


    @Override
    public Message findOne(Integer id) {
        return messageDao.findOne(id);
    }

    @Override
    public Object findAll(Integer status) {
        return messageDao.findByStatus(status);
    }

    @Override
    public String create(Message message) {
        try {
            message.setStatus(0);
            this.messageDao.save(message);
        }catch (Exception e){
            return "erro";
        }
        return "success";
    }

    @Override
    public String update(Message message) {
        try {
            Message one = this.messageDao.findOne(message.getId());
            if(1==one.getStatus()){
                return "此消息已被发送";
            }
            if(ValueUtil.isEmpity(one)){
                return "没有此消息";
            }
            message.setStatus(0);
            this.messageDao.save(message);
        }catch (Exception e){
            return "erro";
        }
        return "success";
    }

    @Override
    public String delete(Integer id) {
        try {
            this.messageDao.delete(id);
        }catch (Exception e){
            return "erro";
        }
        return "success";
    }
}
