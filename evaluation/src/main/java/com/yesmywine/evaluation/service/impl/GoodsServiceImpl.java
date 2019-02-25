package com.yesmywine.evaluation.service.impl;


import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.evaluation.bean.Goods;
import com.yesmywine.evaluation.dao.GoodsDao;
import com.yesmywine.evaluation.service.GoodsService;
import com.yesmywine.util.basic.ValueUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by hz on 12/8/16.
 */
@Service
public class GoodsServiceImpl extends BaseServiceImpl<Goods, Integer> implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    public String synchronous(String jsonDate){
        Integer synchronous=Integer.parseInt(ValueUtil.getFromJson(jsonDate,"synchronous"));
//        Integer synchronous=Integer.parseInt(map.get("synchronous"));
        if(0==synchronous){
            Integer goodsId=Integer.parseInt(ValueUtil.getFromJson(jsonDate,"id"));
            Goods one = goodsDao.findByGoodsId(goodsId);
            if(ValueUtil.notEmpity(one)){
                Integer id = goodsDao.findByGoodsId(goodsId).getId();
                goodsDao.delete(id);
            }
            Goods goods=new Goods();
            goods.setGoStatus(1);
            if(ValueUtil.notEmpity(ValueUtil.getFromJson(jsonDate,"goodsName"))){
                goods.setName(ValueUtil.getFromJson(jsonDate,"goodsName"));
            }else {
                goods.setName(ValueUtil.getFromJson(jsonDate,"goodsOriginalName"));
            }
            goods.setGoodsId(goodsId);
            goods.setPrice(ValueUtil.getFromJson(jsonDate,"price"));
            goods.setSalePrice((ValueUtil.getFromJson(jsonDate,"salePrice")));
//            goods.setContent(ValueUtil.getFromJson(jsonDate,"content"));//商品描述
            goods.setPicture(ValueUtil.getFromJson(jsonDate,"goodsImageUrl"));
            goods.setEnName(ValueUtil.getFromJson(jsonDate,"goodsEnName"));
            Goods byGoodsId = this.goodsDao.findByGoodsId(goods.getGoodsId());
            if(ValueUtil.notEmpity(byGoodsId)){
                goods.setId(byGoodsId.getId());
            }
            goodsDao.save(goods);
            return "add success";
        }else if(2==synchronous){
            Goods id = goodsDao.findByGoodsId(Integer.parseInt(ValueUtil.getFromJson(jsonDate,"id")));
            id.setGoStatus(2);
            goodsDao.save(id);
            return "delete success";
        }else{//修改
            Integer goodsId=Integer.parseInt(ValueUtil.getFromJson(jsonDate,"id"));
            Goods goods=goodsDao.findByGoodsId(goodsId);
            String comments=ValueUtil.getFromJson(jsonDate,"comments");
            String sales=ValueUtil.getFromJson(jsonDate,"sales");
            if(ValueUtil.notEmpity(comments)){
                goods.setComment(Integer.parseInt(comments));
            }
            if(ValueUtil.notEmpity(sales)){
                goods.setComment(Integer.parseInt(sales));
            }
            goods.setPrice(ValueUtil.getFromJson(jsonDate,"price"));
            goods.setSalePrice((ValueUtil.getFromJson(jsonDate,"salePrice")));
            String name=ValueUtil.getFromJson(jsonDate,"goodsName");
            String goodsEnName=ValueUtil.getFromJson(jsonDate,"goodsEnName");
            if(ValueUtil.notEmpity(goodsEnName)){
                goods.setEnName(goodsEnName);
            }
            if(ValueUtil.notEmpity(name)){
                goods.setName(name);
            }else {
                goods.setName(ValueUtil.getFromJson(jsonDate,"goodsOriginalName"));
            }
            goods.setPicture(ValueUtil.getFromJson(jsonDate,"goodsImageUrl"));
            goodsDao.save(goods);
            return "update success";
        }
    }
}



