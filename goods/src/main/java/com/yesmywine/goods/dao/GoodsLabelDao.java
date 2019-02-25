package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.GoodsLabel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 8/4/17.
 */
@Repository
public interface GoodsLabelDao extends BaseRepository<GoodsLabel,Integer> {

    List<GoodsLabel> findByLabelId(Integer LabelId);

    GoodsLabel findByLabelIdAndGoodsId(Integer labelId,Integer goodsId);
}
