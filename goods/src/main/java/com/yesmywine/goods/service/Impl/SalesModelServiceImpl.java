//package com.yesmywine.goods.service.Impl;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonParser;
//import com.yesmywine.goods.bean.Item;
//import com.yesmywine.goods.dao.GoodsDao;
//import com.yesmywine.goods.entity.Goods;
//import com.yesmywine.goods.entity.GoodsSku;
//import com.yesmywine.goods.service.GoodsChannelService;
//import com.yesmywine.goods.service.SalesModelService;
//import com.yesmywine.util.basic.ValueUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//
///**
// * Created by light on 2017/3/16.
// */
//@Service
//public class SalesModelServiceImpl implements SalesModelService{
//
//    @Autowired
//    private GoodsDao goodsDao;
//    @Autowired
//    private GoodsChannelService goodsChannelService;
//    @Override
//    public Object choose(Integer goodsId, Integer salesModelCode) {
//        Goods goods = goodsDao.findOne(goodsId);
//        if(null == goods){
//            return "没有此商品！";
//        }
//        if(0 == salesModelCode){//预售
//         if(Item.single.equals(goods.getItem()) || Item.single == goods.getItem()){//单品
////             Integer skuId = Integer.parseInt(goods.getSkuIdString());
//             //根据skuId查询所有渠道的库存
//             String result = this.goodsChannelService.http(goods.getGoodsSku().get(0).getSkuId().toString());
//             String fromJson = ValueUtil.getFromJson(result, "data");
//             JsonParser jsonParser = new JsonParser();
//             JsonArray arr = jsonParser.parse(fromJson).getAsJsonArray();
//             int length = arr.size();
//             for (int i = 0; i < length;i++) {
//                 if(0 == arr.size()){
//                     break;
//                 }
//                 String useCount = arr.get(i).getAsJsonObject().get("useCount").getAsString();
//                 if(0 != Integer.parseInt(useCount)){
//                     arr.remove(i);
//                     i--;
//                 }
//             }
//             return arr;
//
//         }else {//非单品
//             return "只有单品可以选择预售。";
//         }
//        }else if(1 == salesModelCode){//普通
//            if(Item.single.equals(goods.getItem())|| Item.single == goods.getItem()){//单品
////                Integer skuId = Integer.parseInt(goods.getSkuIdString());
//                //根据skuId查询所有渠道的库存
//                String result = this.goodsChannelService.http(goods.getGoodsSku().get(0).getSkuId().toString());
//                String fromJson = ValueUtil.getFromJson(result, "data");
//                JsonParser jsonParser = new JsonParser();
//                JsonArray arr = jsonParser.parse(fromJson).getAsJsonArray();
//                int length = arr.size();
//                for (int i = 0; i < length; i++) {
//                    if(0 == arr.size()){
//                        break;
//                    }
//                    String useCount = arr.get(i).getAsJsonObject().get("useCount").getAsString();
//                    if(0 == Integer.parseInt(useCount)){
//                        arr.remove(i);
//                        i--;
//                    }
//                }
//                return arr;
////                Integer[] inventorys = null;
////                for (Integer inventory : inventorys) {
////                    if(0 == inventory){
////                        return "库存不足，此商品不可做为普通商品下发";
////                    }else {
////                        return "";//TODO
////                    }
////                }
//
//            }else {//非单品
//                List<GoodsSku> list=goods.getGoodsSku();
////                JsonParser jsonParser = new JsonParser();
////                JsonArray arr = jsonParser.parse(goods.getSkuIdString()).getAsJsonArray();
//                for (GoodsSku goodsSku:list) {
//                    String skuId = goodsSku.getSkuId().toString();
//                    //根据skuId查询所有渠道的库存
//                    String result = this.goodsChannelService.http(skuId);
//                    String fromJson = ValueUtil.getFromJson(result, "data");
//                    JsonParser jsonParser2 = new JsonParser();
//                    JsonArray arr2 = jsonParser2.parse(fromJson).getAsJsonArray();
//                    int length = arr2.size();
//                    for (int j = 0; j < length; j++) {
//                        if(0 == arr2.size()){
//                            break;
//                        }
//                        String useCount = arr2.get(j).getAsJsonObject().get("useCount").getAsString();
//                        if(0 == Integer.parseInt(useCount)){
//                            arr2.remove(j);
//                            j--;
//                        }
//                    }
//                    return arr2;
////                    Integer[] inventorys = null;
////                    for (Integer inventory : inventorys) {
////                        if(0 == inventory){
////                            return "库存不足，此商品不可做为普通商品下发";
////                        }else {
////                            return "";//TODO
////                        }
////                    }
//                }
//            }
//        }else {
//            return "参数错误！";
//        }
//        return "系统异常，稍后再试！";
//    }
//}
