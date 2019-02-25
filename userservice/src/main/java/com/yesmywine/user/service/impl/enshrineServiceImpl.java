package com.yesmywine.user.service.impl;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.user.bean.ConstantData;
import com.yesmywine.user.bean.Enshrined;
import com.yesmywine.user.dao.EnshrineDao;
import com.yesmywine.user.entity.Enshrine;
import com.yesmywine.user.service.EnshrineService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${shuang} on 2017/6/13.
 */
@Service
public class enshrineServiceImpl  extends BaseServiceImpl<Enshrine, Integer> implements EnshrineService{
    @Autowired
    private EnshrineDao enshrineDao;

    @Override
    public Object enshrine(Integer userId, String goodsIds,Integer status) throws YesmywineException {
//        收藏商品，
        String [] array  = goodsIds.split(",");
        List<Enshrine> list = new ArrayList<>();
        if(status==1){//1表示收藏
            for (int i = 0; i <array.length ; i++) {
                Integer goodsId = Integer.valueOf(array[i]);
                Enshrine enshrine = enshrineDao .findByUserIdAndGoodsId( userId,goodsId);
                if(ValueUtil.isEmpity(enshrine)){
                    String data = this.httpGetOneGoods(goodsId);//请求到数据，下一步提取商品名
                    String goodsName = ValueUtil.getFromJson(data,"goodsName");
                    Enshrine enshrine1 = new Enshrine();
                    enshrine1.setUserId(userId);
                    enshrine1.setGoodsId(goodsId);
                    enshrine1.setGoodsName(goodsName);
                    list.add(enshrine1);
                }
            }
            enshrineDao.save(list);
        }else if(status==0){//0表示取消收藏
            for (int i = 0; i <array.length ; i++) {
                Integer goodsId = Integer.valueOf(array[i]);
                Enshrine enshrine = enshrineDao.findByUserIdAndGoodsId( userId,goodsId);
                if(ValueUtil.notEmpity(enshrine)){
                    enshrineDao.delete(enshrine.getId());
                }
            }
        }
        return true;


    }

    @Override
    public String page(PageModel pageModel) throws YesmywineException {
//        自定义的收藏分页
       List<Enshrine> content = pageModel.getContent();
        List<Enshrined> newContent = new ArrayList<>();
        if(content.size()==0){
            pageModel.setContent(newContent);
            return ValueUtil.toJson(pageModel);
        }
        for (int i = 0; i <content.size() ; i++) {
            Enshrined enshrined = new Enshrined();
            Enshrine enshrine = content.get(i);
            Integer goodsId = enshrine.getGoodsId();
            String data = this.httpGetOneGoods(goodsId);
            String goodsImageUrl = ValueUtil.getFromJson(data,"goodsImageUrl");
            String salePrice = ValueUtil.getFromJson(data,"salePrice");
            enshrined.setGoodsId(goodsId);
            enshrined.setGoodsImageUrl(goodsImageUrl);
            enshrined.setGoodsName(enshrine.getGoodsName());
            enshrined.setSalePrice(salePrice);
            newContent.add(enshrined);
        }
        pageModel.setContent(newContent);
        return ValueUtil.toJson(pageModel);
    }


    String httpGetOneGoods(Integer goodsId){
        //请求一条商品数据
        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST+ "/goods/goods/showone/itf", RequestMethod.get);
        httpRequest.addParameter("goodsId", goodsId);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        return ValueUtil.getFromJson(temp,"data");
    }


}
