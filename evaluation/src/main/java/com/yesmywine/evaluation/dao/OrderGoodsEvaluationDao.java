package com.yesmywine.evaluation.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.evaluation.bean.Goods;
import com.yesmywine.evaluation.bean.OrderGoodsEvaluation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by light on 2017/1/9.
 */
@Repository
public interface OrderGoodsEvaluationDao extends BaseRepository<OrderGoodsEvaluation, Integer> {

//  @Query("select  id from OrderGoodsEvaluation　 where goodScores =:goodScores  and  goods =:goods")
//    List<OrderGoodsEvaluation> findByGoodScoresAndGoods(Integer goodScore,Goods goods);
    List<OrderGoodsEvaluation>findByGoodScoresBetweenAndGoodsAndStatus(Integer goodScore,Integer goodScores,Goods goods,Integer status);
    List<OrderGoodsEvaluation> findByGoodsAndStatus(Goods goods,Integer status);
    List<OrderGoodsEvaluation>findByAppTypeAndGoodsIdAndStatus(Integer appType, Integer goodsId,Integer status);

//    List<OrderGoodsEvaluation> findByGoodsAndUserInformation(Goods goods,UserInformation userInformation);
//
    @Query("select goods from OrderGoodsEvaluation  ORDER BY createTime DESC")
    List<Goods> findOrder();

    List<OrderGoodsEvaluation> findByGoods(Goods goods);
    @Query("select  goods from OrderGoodsEvaluation  where goodScores>4")
    List<Goods> findLength();

    @Query("select id from OrderGoodsEvaluation  where goodScores>4 and LENGTH(evaluation)>30 ORDER BY createTime DESC")
    List<Integer> findGoodComment();

}
