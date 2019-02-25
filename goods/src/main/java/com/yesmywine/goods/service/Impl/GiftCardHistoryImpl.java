package com.yesmywine.goods.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.dao.GiftCardHistoryDao;
import com.yesmywine.goods.entity.GiftCardHistory;
import com.yesmywine.goods.service.GiftCardHistoryService;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/25.
 */
@Service
public class GiftCardHistoryImpl  extends BaseServiceImpl<GiftCardHistory, Long> implements GiftCardHistoryService {
    @Autowired
    private GiftCardHistoryDao giftCardHistoryDao;

    public  List<GiftCardHistory> history(Long cardNumber)throws YesmywineException{
        List<GiftCardHistory> giftCardHistoryList=giftCardHistoryDao.findByCardNumber(cardNumber);
        return giftCardHistoryList;
    }
}
