package com.yesmywine.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.cart.dao.CartDao;
import com.yesmywine.cart.dao.CartItemDao;
import com.yesmywine.cart.entity.Cart;
import com.yesmywine.cart.entity.CartItem;
import com.yesmywine.cart.service.CartService;
import com.yesmywine.db.base.ehcache.CacheStatement;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by yly on 2017/2/10.
 */
@Service
@Transactional
public class CartServiceImpl extends BaseServiceImpl<Cart, Integer> implements CartService {
    @Autowired
    private CartDao cartDao;
    @Autowired
    private CartItemDao cartItemDao;

    @Override
    public String addCart(Map<String, String> param, String userInfo) throws YesmywineException {//登录后新增商品到购物车


        String cartCache = param.get("cartCache");//添加到购物车的商品
        if (ValueUtil.notEmpity(cartCache)) {
            Integer userId = Integer.valueOf(ValueUtil.getFromJson(userInfo, "id"));
            JSONArray arr = JSON.parseArray(cartCache);
            List<CartItem> cartItemList = new ArrayList<>();
            Cart cart = cartDao.findByUserId(userId);
            if (cart == null) {
                cart = new Cart();
                cart.setUserId(userId);
                cartDao.save(cart);
            }
            for (int i = 0; i < arr.size(); i++) {
                com.alibaba.fastjson.JSONObject adjustCommand = (com.alibaba.fastjson.JSONObject) arr.get(i);
                String goodsId = adjustCommand.getString("goodsId");
                String goodsCounts = adjustCommand.getString("goodsCounts");
                String regulationId = adjustCommand.getString("regulationId");
                String childGoodsId = adjustCommand.getString("childGoodsId");

                CartItem cartItem = cartItemDao.findByCartIdAndGoodsId(cart.getId(), Integer.valueOf(goodsId));
                if (cartItem != null) {
                    Integer counts = cartItem.getGoodsCounts() + Integer.valueOf(goodsCounts);
                    if(counts>200){
                        cartItem.setGoodsCounts(200);
                    }else {
                        cartItem.setGoodsCounts(counts);
                    }
                    if(ValueUtil.notEmpity(regulationId)) {
                        cartItem.setRegulationId(Integer.valueOf(regulationId));
                    }
                    if(ValueUtil.notEmpity(childGoodsId)){
                        cartItem.setChildGoodsId(Integer.valueOf(childGoodsId));
                    }
                    cartItemList.add(cartItem);
                } else {
                    Integer countUserIdGoodsCart=cartItemDao.countUserIdGoodsCart(cart.getId());
                    if(countUserIdGoodsCart>=50){
                        continue;
                    }
                    CartItem cartItem1 = new CartItem();
                    cartItem1.setCartId(cart.getId());
                    cartItem1.setGoodsId(Integer.valueOf(goodsId));
                    if(ValueUtil.notEmpity(regulationId)) {
                        cartItem1.setRegulationId(Integer.valueOf(regulationId));
                    }
                    if(ValueUtil.notEmpity(childGoodsId)){
                        cartItem1.setChildGoodsId(Integer.valueOf(childGoodsId));
                    }
                    if (Integer.valueOf(goodsCounts) > 200) {
                        cartItem1.setGoodsCounts(200);
                    } else{
                        cartItem1.setGoodsCounts(Integer.valueOf(goodsCounts));
                    }
                    cartItem1.setStatus(1);
//                    if (ValueUtil.notEmpity(gift)) {
//                        cartItem1.setGift(Integer.valueOf(gift));
//                    }
                    cartItemList.add(cartItem1);
                }
            }
            cartItemDao.save(cartItemList);
        }
        String temp=queryCartGoodsList(userInfo);
        return temp;
    }

    @Override
    public String queryCartGoodsList(String userInfo) throws YesmywineException {//查询用户购物车中的商品列表
        JSONArray jsonArray = new JSONArray();
        Integer userId = Integer.valueOf(ValueUtil.getFromJson(userInfo, "id"));
        String userName= ValueUtil.getFromJson(userInfo, "userName");
        Cart cart = cartDao.findByUserId(userId);
        if(cart==null){
           return ValueUtil.toJson();
        }
        List<CartItem> cartItemList = cartItemDao.findByCartIdOrderByCreateTimeDesc(cart.getId());
        if (cartItemList.size()==0) {
            return ValueUtil.toJson();
        }
        for (int i = 0; i < cartItemList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("goodsId", cartItemList.get(i).getGoodsId());
            jsonObject.put("goodsCount", cartItemList.get(i).getGoodsCounts());
            jsonObject.put("regulationId", cartItemList.get(i).getRegulationId());
//                jsonObject.put("gift", cartItemList.get(i).getGift());//是否是赠品（0不是，1是）
            jsonObject.put("childGoodsId", cartItemList.get(i).getChildGoodsId());
            jsonObject.put("createTime", cartItemList.get(i).getCreateTime());
            Integer status = cartItemList.get(i).getStatus();
            if (status == 0) {////状态（0未选中，1选中）
                jsonObject.put("isChoose", false);
            } else {
                jsonObject.put("isChoose", true);
            }
            jsonArray.add(jsonObject);
        }
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("jsonData",jsonArray.toJSONString());
        requestParams.put("username",userName);
        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST,"/web/activity/cart",RequestMethod.post,requestParams,null);
        return result;
    }
    @Override
    public String webCartGoodsList(String cartCache) throws YesmywineException {//查询未登录购物车中的商品列表
        JSONArray jsonArray = new JSONArray();
        if (ValueUtil.notEmpity(cartCache)) {
            JSONArray arr = JSON.parseArray(cartCache);
            for (int i = 0; i < arr.size(); i++) {
                com.alibaba.fastjson.JSONObject adjustCommand = (com.alibaba.fastjson.JSONObject) arr.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("goodsId", adjustCommand.getString("goodsId"));
                jsonObject.put("goodsCount", adjustCommand.getString("goodsCounts"));
                jsonObject.put("regulationId", adjustCommand.getString("regulationId"));
//                    jsonObject.put("gift", adjustCommand.getString("gift"));//是否是赠品（0不是，1是）
                jsonObject.put("childGoodsId", adjustCommand.getString("childGoodsId"));
                Integer status = adjustCommand.getInteger("status");
                if (status == 1) {//状态（0未选中，1选中）
                    jsonObject.put("isChoose", true);
                } else {
                    jsonObject.put("isChoose", false);
                }
                jsonArray.add(jsonObject);
            }
        Map<String, Object> requestParams = new HashMap<>();
        requestParams.put("jsonData", jsonArray.toJSONString());
//        requestParams.put("username",null);
        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/web/activity/cart", RequestMethod.post, requestParams, null);
        return result;
    }
        else {
            return ValueUtil.toJson();
        }
    }

    @Override
    public String deleteGoodsFromCart(String userInfo, Integer goodsId,String appType,Integer childGoodsId) throws YesmywineException {//删除购物车中的指定商品
        //通过用户id找到购物车id
        Integer userId = Integer.valueOf(ValueUtil.getFromJson(userInfo, "id"));
        Cart cart = cartDao.findByUserId(userId);
        CartItem cartItem = cartItemDao.findByCartIdAndGoodsId(cart.getId(), goodsId);
        if (cartItem == null) {
            ValueUtil.isError("购物车没有此商品！");
        }
        if (childGoodsId != null) {
            cartItem.setChildGoodsId(null);
            cartItemDao.save(cartItem);
        } else {
        cartItemDao.delete(cartItem);
        }
        if(appType!=null&&appType.equals("cancel")){
            return ValueUtil.toJson(HttpStatus.SC_OK, "success");
        }else {
            String temp = queryCartGoodsList(userInfo);
            return temp;
        }
    }
    @Override
    public String batchdDleteGoodsFromCart(String userInfo) throws YesmywineException {//删除购物车中的指定商品
        //通过用户id找到购物车id
        Integer userId = Integer.valueOf(ValueUtil.getFromJson(userInfo, "id"));
        Cart cart = cartDao.findByUserId(userId);
        List<CartItem> cartItem = cartItemDao.findByCartIdAndStatus(cart.getId(), 1);
        if (cartItem == null) {
            ValueUtil.isError("购物车没有此商品！");
        }
        cartItemDao.delete(cartItem);
        String temp = queryCartGoodsList(userInfo);
        return temp;
    }

    @Override
    public String clearCart(Integer userId) throws YesmywineException {//清空购物车
        //通过用户id找到购物车id
        Cart cart = cartDao.findByUserId(userId);
        List<CartItem> cartItemList = cartItemDao.findByCartId(cart.getId());
        if (cartItemList == null) {
            ValueUtil.isError("购物车已清空");
        }
        cartItemDao.delete(cartItemList);
        return "success";
    }

    @Override
    public Integer queryGoodsAmount(Integer userId) {//查询指定用户购物车中的所有商品数量
        int cartGoodsCounts = 0;
        Cart cart = cartDao.findByUserId(userId);
        List<CartItem> cartItemList = cartItemDao.findByCartId(cart.getId());
        for (CartItem cartItem : cartItemList) {
            cartGoodsCounts += cartItem.getGoodsCounts();
        }
        return cartGoodsCounts;
    }

    @Override
    public String deleteOrderGoods(Integer userId) throws YesmywineException {//下单后订单系统要调用我的删除购物车中的商品的接口

        //通过用户id找到购物车id
        Cart cart = cartDao.findByUserId(userId);
        List<CartItem> cartItemList = cartItemDao.findByCartIdAndStatus(cart.getId(), 1);
        cartItemDao.delete(cartItemList);
        return "success";
    }

    @Override
    public String addAndSubtract(Integer status, Integer goodsId, Integer count,String userInfo, Integer childGoodsId,Integer regulationId,Integer selectAll) throws YesmywineException {
        Integer userId = Integer.valueOf(ValueUtil.getFromJson(userInfo, "id"));
        Cart cart = cartDao.findByUserId(userId);
        if(ValueUtil.notEmpity(selectAll)&&selectAll==1){
            List<CartItem> cartItemList = cartItemDao.findByCartId(cart.getId());
            cartItemList.forEach(r -> {
                r.setStatus(1);//0未选中，1选中
            });
            cartItemDao.save(cartItemList);
        }else if(ValueUtil.notEmpity(selectAll)&&selectAll==0) {
            List<CartItem> cartItemList = cartItemDao.findByCartId(cart.getId());
            cartItemList.forEach(r -> {
                r.setStatus(0);//0未选中，1选中
            });
            cartItemDao.save(cartItemList);
        }
            CartItem cartItem = cartItemDao.findByCartIdAndGoodsId(cart.getId(), goodsId);
            if (cartItem != null) {
                if (childGoodsId != null) {
                    cartItem.setChildGoodsId(childGoodsId);
                }
                if (regulationId != null) {
                    cartItem.setRegulationId(regulationId);
                    cartItem.setChildGoodsId(null);
                }
                if (status != null) {
                    cartItem.setStatus(status);
                }
                if (count != null) {
                    cartItem.setGoodsCounts(count);
                }
                cartItemDao.save(cartItem);
            }

        String temp = queryCartGoodsList(userInfo);
        return temp;

    }
    public String settlement(String userInfo)throws YesmywineException {
        JSONArray jsonArray = new JSONArray();
        Integer userId = Integer.valueOf(ValueUtil.getFromJson(userInfo, "id"));
        String userName = ValueUtil.getFromJson(userInfo, "userName");
        Cart cart = cartDao.findByUserId(userId);
        List<CartItem> cartItemList = cartItemDao.findByCartIdAndStatus(cart.getId(),1);
        if(cartItemList==null|| cartItemList.size() ==0){
            ValueUtil.isError("请选择您要购买的商品！");
        }
        for (int i = 0; i < cartItemList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("goodsId", cartItemList.get(i).getGoodsId());
            jsonObject.put("goodsCount", cartItemList.get(i).getGoodsCounts());
            jsonObject.put("regulationId", cartItemList.get(i).getRegulationId());
//            jsonObject.put("gift", cartItemList.get(i).getGift());//是否是赠品（0不是，1是）
            jsonObject.put("childGoodsId", cartItemList.get(i).getChildGoodsId());
            Integer status = cartItemList.get(i).getStatus();
            if (status == 1) {//状态（0未选中，1选中）
                jsonObject.put("isChoose", true);
                jsonArray.add(jsonObject);
            }
        }
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("jsonData",jsonArray.toJSONString());
        requestParams.put("username",userName);
        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST,"/web/activity/cart",RequestMethod.post,requestParams,null);
        String everyGoodsInfo = ValueUtil.getFromJson(result, "data", "everyGoodsInfo");
        JSONArray adjustArray = JSON.parseArray(everyGoodsInfo);
        String goodsSaleType=null;
        String otherSaleType = null;
        String luckBagGoodsType=null;
        String otherGoodsType = null;
        String virtualGoodsType=null;//虚拟商品
        String othervirtualGoodsType=null;
        List<Object> preGoodsList = new ArrayList<>();//判断购物车中的预售商品数量，一次结算只能购买一种商品
        Map<String,Integer> skuMap = new HashMap<>();
        Map<String,String> goodsMap = new HashMap<>();
        for (int i = 0; i < adjustArray.size(); i++) {
            JSONObject goodsInfo = (JSONObject) adjustArray.get(i);
            JSONArray skuInfoArr = goodsInfo.getJSONArray("skuInfo");
            Integer goodsCount = goodsInfo.getInteger("goodsCount");
            String goodsName = goodsInfo.getString("goodsName");
            Integer goStatus =Integer.valueOf(goodsInfo.getString("goStatus"));
            if(goStatus==2){
                ValueUtil.isError("商品"+goodsName+"已下架,不能结算");
            }
            if(skuInfoArr!=null&&skuInfoArr.size()>0){
                for(int m=0;m<skuInfoArr.size();m++){
                    JSONObject skuInfo = (JSONObject) skuInfoArr.get(m);
                    String skuId = skuInfo.getString("skuId");
                    Integer skuCount = Integer.valueOf(skuInfo.getString("count"))*goodsCount;
                    if(!skuMap.containsKey(skuId)){
                        skuMap.put(skuId,skuCount);
                        goodsMap.put(skuId,goodsName);
                    }else{
                        Integer beforeCount = skuMap.get(skuId);
                        skuMap.put(skuId,beforeCount+skuCount);
                    }
                }
            }
            String saleModel = goodsInfo.getString("saleModel");
            if(saleModel.equals("1")){
                preGoodsList.add(adjustArray);
            }

            String saleType = saleModel;
            switch (saleType){
                case "0":
                    goodsSaleType = saleType;
                    break;
                case "1":
                    otherSaleType = saleType;
            }

            String goodsType = goodsInfo.getString("goodsType");
            switch (goodsType){
                case "luckBage":
                    luckBagGoodsType = goodsType;
                    break;
                default:
                    otherGoodsType = goodsType;
            }

            String virtualType  = goodsInfo.getString("virtualType")==null?"":goodsInfo.getString("virtualType");
            switch (virtualType) {
                    case "giftCard":
                        virtualGoodsType = virtualType;
                        break;
                    default:
                        othervirtualGoodsType = virtualType;
                }

        }
        if(goodsSaleType!=null&&otherSaleType!=null){
            ValueUtil.isError("预售商品不能与普通商品一起结算");
        }

        if(preGoodsList.size()>0){
            ValueUtil.isError("每次只能结算一种预售商品");
        }

        if(luckBagGoodsType!=null&&otherGoodsType!=null){
            ValueUtil.isError("福袋商品与普通商品不能一起结算");
        }
        if(virtualGoodsType!=null&&othervirtualGoodsType!=null){
            ValueUtil.isError("虚拟商品与普通商品不能一起结算");
        }

        //判断商品库存是否足够
        Set<String> noEnoughGoodsSet = getNoEnoughGoods(skuMap,goodsMap);//库存不足的商品
        if(noEnoughGoodsSet.size()>0){
            ValueUtil.isError("商品："+noEnoughGoodsSet.toString()+"库存不足");
        }

        return result;
    }

    private Set<String> getNoEnoughGoods(Map<String, Integer> skuMap, Map<String, String> goodsMap) {
        Set<String> noEnoughGoods = new HashSet<>();
        Iterator<Map.Entry<String, Integer>> skuIt = skuMap.entrySet().iterator();
        Iterator goodsIt = goodsMap.entrySet().iterator();
        while (skuIt.hasNext()){
            Map.Entry skuEntry = skuIt.next();
            String skuId = (String) skuEntry.getKey();
            Integer count = (Integer) skuEntry.getValue();

            Map.Entry goodsEntry = (Map.Entry) goodsIt.next();
            String goodsName = (String) goodsEntry.getValue();
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put("skuId",skuId);
            requestParams.put("count",count);
            String code = SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/inventory/channelInventory/isInvEnough/itf",RequestMethod.get,requestParams,null);
            if(code!=null&&code.equals("500")){
                if(!noEnoughGoods.contains(goodsName)){
                    noEnoughGoods.add(goodsName);
                }
            }
        }
        return noEnoughGoods;
    }
    public String webCartOrders(Integer userId,String username)throws YesmywineException {
        JSONArray jsonArray = new JSONArray();
//        Integer userId = Integer.valueOf(ValueUtil.getFromJson(userInfo, "id"));
//        String userName = ValueUtil.getFromJson(userInfo, "userName");
        Cart cart = cartDao.findByUserId(userId);
        List<CartItem> cartItemList = cartItemDao.findByCartIdAndStatus(cart.getId(),1);
        for (int i = 0; i < cartItemList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("goodsId", cartItemList.get(i).getGoodsId());
            jsonObject.put("goodsCount", cartItemList.get(i).getGoodsCounts());
            jsonObject.put("regulationId", cartItemList.get(i).getRegulationId());
//            jsonObject.put("gift", cartItemList.get(i).getGift());//是否是赠品（0不是，1是）
            jsonObject.put("childGoodsId", cartItemList.get(i).getChildGoodsId());
            Integer status = cartItemList.get(i).getStatus();
            if (status == 0) {////状态（0未选中，1选中）
                jsonObject.put("isChoose", false);
            } else {
                jsonObject.put("isChoose", true);
            }
            jsonArray.add(jsonObject);
        }
        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/web/activity/cart", RequestMethod.post);httpRequest.addParameter("jsonData", jsonArray.toJSONString());
        httpRequest.addParameter("username", username);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        return temp;
    }

    public String cartOrders(Integer userId,String username)throws YesmywineException {
        JSONArray jsonArray = new JSONArray();
//        Integer userId = Integer.valueOf(ValueUtil.getFromJson(userInfo, "id"));
//        String userName = ValueUtil.getFromJson(userInfo, "userName");
        Cart cart = cartDao.findByUserId(userId);
        List<CartItem> cartItemList = cartItemDao.findByCartIdAndStatus(cart.getId(),1);
        for (int i = 0; i < cartItemList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("goodsId", cartItemList.get(i).getGoodsId());
            jsonObject.put("goodsCount", cartItemList.get(i).getGoodsCounts());
            jsonObject.put("regulationId", cartItemList.get(i).getRegulationId());
//            jsonObject.put("gift", cartItemList.get(i).getGift());//是否是赠品（0不是，1是）
            jsonObject.put("childGoodsId", cartItemList.get(i).getChildGoodsId());
            Integer status = cartItemList.get(i).getStatus();
            if (status == 0) {////状态（0未选中，1选中）
                jsonObject.put("isChoose", false);
            } else {
                jsonObject.put("isChoose", true);
            }
            jsonArray.add(jsonObject);
        }
        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/web/activity/cart", RequestMethod.post);httpRequest.addParameter("jsonData", jsonArray.toJSONString());
        httpRequest.addParameter("username", username);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        deleteOrderGoods(userId);
        return temp;
    }

    public String memberAdd(Map<String, String> param,String userInfo) throws YesmywineException {
        String cartCache = param.get("cartCache");//添加到购物车的商品
        if (ValueUtil.notEmpity(cartCache)) {
            Integer userId = Integer.valueOf(ValueUtil.getFromJson(userInfo, "id"));
            String userName= ValueUtil.getFromJson(userInfo, "userName");
            JSONArray arr = JSON.parseArray(cartCache);
            List<CartItem> cartItemList = new ArrayList<>();
            Cart cart = cartDao.findByUserId(userId);
            if (cart == null) {
                cart = new Cart();
                cart.setUserId(userId);
                cartDao.save(cart);
            }
            for (int i = 0; i < arr.size(); i++) {
                com.alibaba.fastjson.JSONObject adjustCommand = (com.alibaba.fastjson.JSONObject) arr.get(i);
                String goodsId = adjustCommand.getString("goodsId");
                String goodsCounts = adjustCommand.getString("goodsCounts");
                String regulationId = adjustCommand.getString("regulationId");
                String childGoodsId = adjustCommand.getString("childGoodsId");


                //判断已下架的商品不能加入购物车
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("goodsId", goodsId);
                jsonObject.put("goodsCount", goodsCounts);
                jsonArray.add(jsonObject);
                Map<String,Object> requestParams = new HashMap<>();
                requestParams.put("jsonData",jsonArray.toJSONString());
                requestParams.put("username",userName);
                String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST,"/web/activity/cart",RequestMethod.post,requestParams,null);
                String everyGoodsInfo = ValueUtil.getFromJson(result, "data", "everyGoodsInfo");
                JSONArray goodsArr = JSON.parseArray(everyGoodsInfo);
                for (int j = 0; j < goodsArr.size(); j++) {
                    JSONObject goodsInfo = (JSONObject) goodsArr.get(j);

                    Integer goStatus = goodsInfo.getInteger("goStatus");
                    if(goStatus==2){
                        ValueUtil.isError("商品已下架不能加入购物车！");
                    }
                }
                CartItem cartItem = cartItemDao.findByCartIdAndGoodsId(cart.getId(), Integer.valueOf(goodsId));
                if (cartItem != null) {
                    Integer counts = cartItem.getGoodsCounts() + Integer.valueOf(goodsCounts);
                    if(counts>200){
                        cartItem.setGoodsCounts(200);
                    }else {
                        cartItem.setGoodsCounts(counts);
                    }
                    if(ValueUtil.notEmpity(regulationId)) {
                        cartItem.setRegulationId(Integer.valueOf(regulationId));
                    }
                    if(ValueUtil.notEmpity(childGoodsId)){
                        cartItem.setChildGoodsId(Integer.valueOf(childGoodsId));
                    }
                    cartItemList.add(cartItem);
                } else {
                    Integer countUserIdGoodsCart=cartItemDao.countUserIdGoodsCart(cart.getId());
                    if(countUserIdGoodsCart>=50){
                        ValueUtil.isError("购物车已达到上限！");
                    }
                    CartItem cartItem1 = new CartItem();
                    cartItem1.setCartId(cart.getId());
                    cartItem1.setGoodsId(Integer.valueOf(goodsId));
                    if (Integer.valueOf(goodsCounts) > 200) {
                        cartItem1.setGoodsCounts(200);
                    } else{
                        cartItem1.setGoodsCounts(Integer.valueOf(goodsCounts));
                     }
                    if(ValueUtil.notEmpity(regulationId)) {
                        cartItem1.setRegulationId(Integer.valueOf(regulationId));
                    }
                    if(ValueUtil.notEmpity(childGoodsId)){
                        cartItem1.setChildGoodsId(Integer.valueOf(childGoodsId));
                    }
                    cartItem1.setStatus(1);
                    cartItemList.add(cartItem1);
                }
            }
            cartItemDao.save(cartItemList);
        }
        return "success";
    }

    public Map<String, Object> library(Integer userId) throws YesmywineException {

        Cart cart = cartDao.findByUserId(userId);
        List<CartItem> cartItemList = cartItemDao.findByCartIdAndStatus(cart.getId(), 1);

        StringBuilder goodsId = new StringBuilder();

        for (int i = 0; i < cartItemList.size(); i++) {
            if (i == cartItemList.size() - 1)//当循环到最后一个的时候 就不添加逗号,
            {
                goodsId.append(cartItemList.get(i).getGoodsId());
            } else {
                goodsId.append(cartItemList.get(i).getGoodsId());
                goodsId.append(",");
            }
        }

        Map<String, Object> payParams = new HashMap<>();
        payParams.put("goodsId", goodsId);
        Map<String, Object> map = new HashMap<>();
        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/goods/goods/itf/manyGoods", RequestMethod.get, payParams, null);
        if (result != null) {
            JSONObject jsonObj = JSON.parseObject(result);
            String code1 = jsonObj.getString("code");
            Boolean A = true;
            List<Integer> goodsIds = new ArrayList<>();
            if (code1.equals("200")) {
                String data = jsonObj.getString("data");
                JsonParser jsonParser = new JsonParser();
                JsonArray arr = jsonParser.parse(data).getAsJsonArray();
                for (int i = 0; i < arr.size(); i++) {
                    String library = arr.get(i).getAsJsonObject().get("library").getAsString();
                    if(null!=library){
                        if(Integer.valueOf(library)==1){
                            A = false;
                            String id = arr.get(i).getAsJsonObject().get("id").getAsString();
                            goodsIds.add(Integer.valueOf(id));
                        }
                    }
                }
            }
            if (A) {
                map.put("library", A);
            } else {
                map.put("library", A);
                map.put("goodsIds", goodsIds);
            }
        }
        return map;
    }
    public Map<String, Object> cashOnDelivery(Integer userId) throws YesmywineException {
        Cart cart = cartDao.findByUserId(userId);
        List<CartItem> cartItemList = cartItemDao.findByCartIdAndStatus(cart.getId(), 1);

        StringBuilder goodsId = new StringBuilder();

        for (int i = 0; i < cartItemList.size(); i++) {
            if (i == cartItemList.size() - 1)//当循环到最后一个的时候 就不添加逗号,
            {
                goodsId.append(cartItemList.get(i).getGoodsId());
            } else {
                goodsId.append(cartItemList.get(i).getGoodsId());
                goodsId.append(",");
            }
        }

        Map<String, Object> payParams = new HashMap<>();
        payParams.put("goodsId", goodsId);
        Map<String, Object> map = new HashMap<>();
        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/goods/goods/itf/manyGoods", RequestMethod.get, payParams, null);
        if (result != null) {
            JSONObject jsonObj = JSON.parseObject(result);
            String code1 = jsonObj.getString("code");
            Boolean A = true;
            List<Integer> goodsIds = new ArrayList<>();
            if (code1.equals("200")) {
                String data = jsonObj.getString("data");
                JsonParser jsonParser = new JsonParser();
                JsonArray arr = jsonParser.parse(data).getAsJsonArray();
                for (int i = 0; i < arr.size(); i++) {
                    String cashOnDelivery = arr.get(i).getAsJsonObject().get("cashOnDelivery").getAsString();
                    if(null!=cashOnDelivery){
                        if(Integer.valueOf(cashOnDelivery)==1){
                            A = false;
                            String id = arr.get(i).getAsJsonObject().get("id").getAsString();
                            goodsIds.add(Integer.valueOf(id));
                        }
                    }
                }
            }
            if (A) {
                map.put("cashOnDelivery", A);
            } else {
                map.put("cashOnDelivery", A);
                map.put("goodsIds", goodsIds);
            }
        }
        return map;
    }

}