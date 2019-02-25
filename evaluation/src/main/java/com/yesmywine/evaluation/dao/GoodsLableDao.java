package com.yesmywine.evaluation.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.evaluation.bean.EvaluationLable;
import com.yesmywine.evaluation.bean.Lable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 6/21/17.
 */
@Repository
public interface GoodsLableDao extends BaseRepository<EvaluationLable,Integer> {

    EvaluationLable findByGoodsIdAndLable(Integer goodsId, Lable lable);

    List<EvaluationLable>findByGoodsIdOrderByCountDesc(Integer goodsId);
}