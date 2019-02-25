
package com.yesmywine.cms.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.cms.dao.GoodsDao;
import com.yesmywine.cms.entity.Goods;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.db.base.ehcache.CacheStatement;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by hz on 1/6/17.
 */
@Service
@Transactional
public class GoodsServiceImpl extends BaseServiceImpl<Goods, Integer> implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public Boolean synchronous(String jsonDate) {
        Boolean resutl = false;
        Integer synchronous=Integer.parseInt(ValueUtil.getFromJson(jsonDate,"synchronous"));
        if( 0 == synchronous){
            resutl = this.save(jsonDate);
        }else if(2==synchronous){
            resutl = this.delete(jsonDate);
        }else {
            resutl=this.updates(jsonDate);
        }
        return resutl;
    }

    @Override
    @Cacheable(value= CacheStatement.GOODS_VALUE,key = "'Goods_'+#id")
    public Goods findById(Integer id) {
        return goodsDao.findOne(id);
    }

    public Boolean save(String jsonDate){
        Integer goodsId=Integer.parseInt(ValueUtil.getFromJson(jsonDate,"id"));
        Goods one = goodsDao.findOne(goodsId);
        if(ValueUtil.notEmpity(one)){
            this.goodsDao.delete(goodsId);
        }
        try {
            Goods goods = new Goods();
            goods.setId(goodsId);
            goods.setGoStatus(1);
            goods.setCategoryId(Integer.parseInt(ValueUtil.getFromJson(jsonDate,"categoryId")));
            if(ValueUtil.notEmpity(ValueUtil.getFromJson(jsonDate,"goodsName"))){
                goods.setGoodsName(ValueUtil.getFromJson(jsonDate,"goodsName"));
            }else {
                goods.setGoodsName(ValueUtil.getFromJson(jsonDate,"goodsOriginalName"));
            }
            goods.setSalePrice(ValueUtil.getFromJson(jsonDate,"salePrice"));
            goods.setPrice(ValueUtil.getFromJson(jsonDate,"price"));
            String goodsEnName = ValueUtil.getFromJson(jsonDate, "goodsEnName");
            if(ValueUtil.notEmpity(goodsEnName)) {
                goods.setGoodsEnName(goodsEnName);
            }
            goods.setPicture(ValueUtil.getFromJson(jsonDate,"goodsImageUrl"));
            this.goodsDao.save(goods);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @CacheEvict(value= CacheStatement.GOODS_VALUE)
    public Boolean delete(String jsonDate){
        try {
            Goods id = this.goodsDao.findOne(Integer.parseInt(ValueUtil.getFromJson(jsonDate, "id")));
            id.setGoStatus(2);
            goodsDao.save(id);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @CacheEvict(value= CacheStatement.GOODS_VALUE)
    public Boolean updates(String jsonDate){
        try {
            Goods goods=this.goodsDao.findOne((Integer.parseInt(ValueUtil.getFromJson(jsonDate,"id"))));
            String categoryId=ValueUtil.getFromJson(jsonDate,"categoryId");
            if(null!=categoryId){
                goods.setCategoryId(Integer.parseInt(categoryId));
            }
            String comments = ValueUtil.getFromJson(jsonDate, "comments");
            if(ValueUtil.notEmpity(comments)){
                goods.setComments(Integer.parseInt(comments));
            }
            String praise = ValueUtil.getFromJson(jsonDate, "praise");
            if(ValueUtil.notEmpity(praise)){
                goods.setPraise(Double.parseDouble(praise));
            }
            goods.setPrice(ValueUtil.getFromJson(jsonDate,"price"));
            String goodsName = ValueUtil.getFromJson(jsonDate, "goodsName");
            if(ValueUtil.notEmpity(goodsName)){
                goods.setGoodsName(goodsName);
            }else {
                goods.setGoodsName(ValueUtil.getFromJson(jsonDate,"goodsOriginalName"));
            }
            String goodsEnName = ValueUtil.getFromJson(jsonDate, "goodsEnName");
            if(ValueUtil.notEmpity(goodsEnName)) {
                goods.setGoodsEnName(goodsEnName);
            }
            String image=ValueUtil.getFromJson(jsonDate,"goodsImageUrl");
            if(ValueUtil.notEmpity(image)){
                goods.setPicture(image);
            }
            goods.setSalePrice((ValueUtil.getFromJson(jsonDate,"salePrice")));
            goodsDao.save(goods);
        }catch (Exception e){
                return false;
        }
        return true;
    }

}
