package com.yesmywine.user.service.impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.user.dao.MessageDao;
import com.yesmywine.user.entity.Notices;
import com.yesmywine.user.service.NoticesService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ${shuang} on 2017/6/30.
 */
@Service
public class NoticesServiceImpl extends BaseServiceImpl<Notices, Integer> implements NoticesService {
    @Autowired
    private MessageDao messageDao;
    @Override
    public String create(Map<String, String> params,Integer userId) throws YesmywineException {

//        创建消息,物流消息
        String orderNumber = params.get("orderNumber");
        String goodsName = params.get("goodsName");
        String goodsImageUrl = params.get("goodsImageUrl");
        String LogisticsNumber = params.get("LogisticsNumber");
        String LogisticsCode = params.get("LogisticsCode");
        String LogisticsName = params.get("LogisticsName");
        String orderId = params.get("orderId");
        String goodsId = params.get("goodsId");

//        新建消息
        Notices notices = new Notices();
        notices.setGoodsImageUrl(goodsImageUrl);
        notices.setGoodsName(goodsName);
        notices.setLogisticsNumber(LogisticsNumber);
        notices.setOrderNumber(orderNumber);
        notices.setLogisticsName(LogisticsName);
        notices.setUserId(userId);
        notices.setOrderId(orderId);
        notices.setGoodsId(goodsId);
        messageDao.save(notices);
//        给用户发短消息,告诉用户订单发货了
        String title = "订单已发货！您购买的["+goodsName+"]已发货";
        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/push/message/pushToSingle", RequestMethod.post);
//        String userId, String title, Integer relevancyType
        httpRequest.addParameter("userId", userId);
        httpRequest.addParameter("title", title);
        httpRequest.addParameter("relevancyType", 10);
        httpRequest.run();
        return "success";
    }
}
