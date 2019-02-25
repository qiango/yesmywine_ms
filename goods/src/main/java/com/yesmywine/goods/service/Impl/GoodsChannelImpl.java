package com.yesmywine.goods.service.Impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.bean.Item;
import com.yesmywine.goods.dao.GoodsChannelDao;
import com.yesmywine.goods.dao.GoodsDao;
import com.yesmywine.goods.dao.SkuDao;
import com.yesmywine.goods.entity.Goods;
import com.yesmywine.goods.entity.GoodsChannel;
import com.yesmywine.goods.service.GoodsChannelService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.httpclient.bean.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ${shuang} on 2017/3/16.
 */
@Service
public class GoodsChannelImpl extends BaseServiceImpl<GoodsChannel, Integer> implements GoodsChannelService {

    @Autowired
    GoodsDao goodsDao;
    @Autowired
    SkuDao skuDao;
    @Autowired
    GoodsChannelDao goodsChannelDao;



    //下发预售
//    @Override
//    public   List setGoodsChannel(Integer goodsId, String[] params) throws YesmywineException {
//
//            Goods goods= goodsDao.findOne(goodsId);
//        List< String> failedlist=new ArrayList<>();
//            for (int i = 0; i <=params.length-1 ; i++) {
//                String multiple= params[i];
//                String[] splitone = multiple.split(";");
//                Integer channelId=Integer.valueOf(splitone[0]);
//                String channelName=splitone[1];
//                String channelCode=splitone[2];
//                if(ValueUtil.isEmpity(goodsChannelDao.findByGoodsIdAndChannelId(goodsId,channelId))){
//                    GoodsChannel goodsChannel=new GoodsChannel();
//                    goodsChannel.setGoodsId(goodsId);
//                    goodsChannel.setGoodsName(goods.getGoodsName());
//                    goodsChannel.setSkuId(goods.getSkuIdString());
//                    goodsChannel.setPrice(goods.getPrice());
//                    goodsChannel.setItem(goods.getItem().toString());
//                    goodsChannel.setChannelId(channelId);
//                    goodsChannel.setGoodsCode(goods.getGoodsCode());
//                    goodsChannel.setChannelCode(channelCode);
//                    goodsChannel.setChannelName(channelName);
//                    goodsChannelDao.save(goodsChannel);
//                }else {
//                    HashMap<String,Object> map=new HashMap<>();
//                    map.put("goodsName",goods.getGoodsName());
//                    map.put("channelName",channelName);
//
//                    failedlist.add(map.toString().replace("=",":"));
//                }
//            }
//      return failedlist;
//    }

    @Override
    public boolean exchange(Integer goodsId,Integer channelId) throws YesmywineException {
        GoodsChannel goodsChannel = goodsChannelDao.findByGoodsIdAndChannelId(goodsId,channelId);
        if(goodsChannel.getItem().equals("single")){//单品
            String returnCode= this.http(goodsChannel.getSkuId(),goodsChannel.getChannelId());
            if(goodsChannel.getOperate()==1){//预售状态
                    Integer count= Integer.valueOf(ValueUtil.getFromJson(returnCode,"data","useCount"));
                    if(count>0){//todo
                        goodsChannel.setOperate(0);//设置为在售
                        goodsChannelDao.save(goodsChannel);
                        //todo
                        //通知商城
                        return true;
                    }else {
                        return false;
                    }
            }else {//在售状态
                Integer allcount= Integer.valueOf(ValueUtil.getFromJson(returnCode,"data","allCount"));
                if(allcount==0){
                    goodsChannel.setOperate(1);//设置为预售货
                    goodsChannelDao.save(goodsChannel);
                    //todo
                    //通知商城
                    return true;
                }else {
                    return false;
                }
            }

        }else //组合商品
        {
//            if(goodsChannel.getOperate()==1){
//                //todo
//                goodsChannel.getSkuId();
//                return true;
//            }else {
//                return  false;
//            }
            return false;
        }

    }

    public String http ( String skuId,Integer channelId){
        HttpBean bean = new HttpBean(Dictionary.MALL_HOST+ "/channelInventory/skuInventory/itf", RequestMethod.get);
        bean.addParameter("skuId",skuId);
        bean.addParameter("channelId",channelId);
        bean.run();
        return bean.getResponseContent();

    }

    public String http (String skuId){
        HttpBean bean = new HttpBean(Dictionary.MALL_HOST+ "/channelInventory/skuInventory/itf", RequestMethod.get);
        bean.addParameter("skuId",skuId);
        bean.run();
        return bean.getResponseContent();

    }

    public Integer inventory(String skuId, Integer channelId, String item) {

        if(Item.single.toString().equals(item)|| Item.single.toString() == item){//单品
            JsonParser jsonParser = new JsonParser();
            JsonArray arr2 = jsonParser.parse(skuId).getAsJsonArray();
            for (int j = 0; j < arr2.size(); j++) {
                String skuId1 = arr2.get(j).getAsJsonObject().get("skuId").getAsString();
                //根据skuId查询所有渠道的库存
                String result = this.http(skuId1, channelId);
                if(!"500".equals(ValueUtil.getFromJson(result, "code"))){
                    String useCount = ValueUtil.getFromJson(result, "data", "useCount");
                    return Integer.valueOf(useCount);
                }
            }
            return null;

        }else {//非单品
            JsonParser jsonParser = new JsonParser();
            JsonArray arr = jsonParser.parse(skuId).getAsJsonArray();
            Integer[] inventory = new Integer[arr.size()];
            for (int i = 0; i < arr.size(); i++) {
                String skuId1 = arr.get(i).getAsJsonObject().get("skuId").getAsString();

                String result = this.http(skuId1, channelId);
                if("500".equals(ValueUtil.getFromJson(result, "code"))){
                    inventory[i] = null;
                }else {
                    String useCount = ValueUtil.getFromJson(result, "data", "useCount");
                    inventory[i] = Integer.valueOf(useCount);
                }

            }
            Integer min = inventory[0];
            for(int j=1; j<inventory.length; j++){
                if(ValueUtil.notEmpity(inventory[j])){
                    if(inventory[j]<min){
                        min = inventory[j];
                    }
                }
            }
            return min;
        }
    }

    @Override
    public String synchronous(Map<String, String> map) throws YesmywineException {
        Integer goodsId=Integer.parseInt(map.get("goodsId"));
        Integer channelId=Integer.parseInt(map.get("channelId"));
        Integer operate=Integer.parseInt(map.get("operate"));
        Goods goods=goodsDao.findByPassGoodsIdAndChannelId(goodsId,channelId);
        goods.setOperate(operate);
        goodsDao.save(goods);
        return "success";
    }
}

