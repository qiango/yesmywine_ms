package com.yesmywine.goods.controller;

import com.yesmywine.goods.dao.ChannelDao;
import com.yesmywine.goods.dao.GoodsDao;
import com.yesmywine.goods.entity.Goods;
import com.yesmywine.goods.service.GoodsChannelService;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by ${shuang} on 2017/3/16.
 */


@RestController
@RequestMapping("/goods/goodschannel")
public class GoodsChannelController {

    @Autowired
    GoodsChannelService goodsChannelService;
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    ChannelDao channelDao;

    @RequestMapping(value = "/synchronous", method = RequestMethod.POST)
    public String synchronous(@RequestParam Map<String,String> map){
        try {
            ValueUtil.verify(map.get("channelId"),"channelId");
            ValueUtil.verify(map.get("goodsId"),"goodsId");
            return ValueUtil.toJson(HttpStatus.SC_OK,goodsChannelService.synchronous(map));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    //判断是否预售
    @RequestMapping(value = "/judge/itf", method = RequestMethod.GET)
    public String exchange(Integer goodsId){
        Goods goods = goodsDao.findOne(goodsId);
        Integer flag = goods.getOperate();
        if(flag==1){
            return ValueUtil.toJson(HttpStatus.SC_OK,"true") ;
        }else {
            return ValueUtil.toJson(HttpStatus.SC_OK,"false") ;
        }
    }

}
