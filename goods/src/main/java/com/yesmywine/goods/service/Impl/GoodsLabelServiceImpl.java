package com.yesmywine.goods.service.Impl;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.dao.GoodsLabelDao;
import com.yesmywine.goods.entity.GoodsLabel;
import com.yesmywine.goods.entity.Label;
import com.yesmywine.goods.service.GoodsLabelService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hz on 8/4/17.
 */
@Service
public class GoodsLabelServiceImpl extends BaseServiceImpl<GoodsLabel,Integer> implements GoodsLabelService {


    @Autowired
    private GoodsLabelDao goodsLabelDao;
    @Override
    public String create(Integer labelId, String goodsIds) throws YesmywineException {
        List<GoodsLabel> labels=new ArrayList<>();
        String arr[]=goodsIds.split(",");
        for(int i=0;i<arr.length;i++){
            GoodsLabel g = goodsLabelDao.findByLabelIdAndGoodsId(labelId,Integer.parseInt(arr[i]));
            if(null!=g){
                ValueUtil.isError("该标签下id为"+arr[i]+"的商品已存在");
            }
            GoodsLabel goodsLabel=new GoodsLabel();
            goodsLabel.setLabelId(labelId);
            goodsLabel.setGoodsId(Integer.parseInt(arr[i]));
            labels.add(goodsLabel);
        }
        goodsLabelDao.save(labels);
        return "success";
    }
}
