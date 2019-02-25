package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.GiftCardHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 12/12/16.
 */
@Repository
public interface GiftCardHistoryDao extends BaseRepository<GiftCardHistory, Long> {
    List<GiftCardHistory> findByCardNumber(Long cardNumber);
}
