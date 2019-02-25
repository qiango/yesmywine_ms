package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.GiftCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by hz on 12/12/16.
 */
@Repository
public interface GiftCardDao extends BaseRepository<GiftCard, Long> {

    GiftCard findByCardNumber(Long cardNumber);
    GiftCard findByCardNumberAndPassword(Long cardNumber,String password);
    List<GiftCard> findByUserIdAndRemainingSumGreaterThan(Integer userId, Double remainingSum);
    List<GiftCard> findByUserId(Integer userId);

    @Query(value = "SELECT * FROM giftCard WHERE ifBuy=0 and type=0 and skuId=:skuId LIMIT :num",nativeQuery =true)
    List<GiftCard> findNoByGiftCard(@Param("skuId")Integer skuId, @Param("num") Integer counts);

//    List<GiftCard> findByCardNumberIn(String cardNumber);
    GiftCard findByCardNumberAndIfBuy(Long cardNumber,Integer ifBuy);

}
