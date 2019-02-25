package com.yesmywine.goods.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entity.GiftCard;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/12/22.
 */
public interface GiftCardService extends BaseService<GiftCard, Long> {
    String addGiftCard(Map<String,String> param)throws YesmywineException;//商城新增礼品卡
    GiftCard updateLoad(Long id) throws YesmywineException;//查看礼品卡详情
    GiftCard loadCardNumber(Long cardNumber) throws YesmywineException;//查看礼品卡详情内部调用
    String synchronizeGiftCard(String jsonData) throws YesmywineException;
    String boundGiftCard(String jsonDatas) throws YesmywineException;
    String spendGiftCard(String jsonDatas) throws YesmywineException;
    String giftCardHistory(String jsonDatas) throws YesmywineException;//pass礼品卡消费后记录同步给商城接口
    String spendPassCard(String jsonDatas)  throws YesmywineException;
    String bound(String jsonData)throws YesmywineException;//内部商城礼品卡绑定信息同步到pass
    String mallbound(Long cardNumber, String password,String userInfo)throws YesmywineException;//商城用户绑定
    String returnPassCard(String jsonDatas)  throws YesmywineException;
    GiftCard useOneGiftCard(Long cardNumber,String password) throws YesmywineException;
    Map<String, Object>  userGiftCart(Integer userId)throws YesmywineException;
//    String buyGiftCard(Long cardNumber)throws YesmywineException;//商城购买礼品卡
    List<GiftCard> randomGiftCard(Integer skuId,Integer counts)throws YesmywineException;//随机抽取未购买的礼品卡
    List<GiftCard> ordersGift(String cardNumbers)throws YesmywineException;//购买礼品卡后显示信息
}
