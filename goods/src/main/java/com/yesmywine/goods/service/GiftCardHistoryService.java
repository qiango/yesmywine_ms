package com.yesmywine.goods.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entity.GiftCardHistory;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/25.
 */
public interface GiftCardHistoryService extends BaseService<GiftCardHistory, Long> {
    List<GiftCardHistory> history(Long cardNumber)throws YesmywineException;
}
