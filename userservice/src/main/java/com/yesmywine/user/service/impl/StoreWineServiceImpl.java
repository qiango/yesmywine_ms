package com.yesmywine.user.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.user.dao.*;
import com.yesmywine.user.entity.*;
import com.yesmywine.user.service.StoreWineService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * Created by ${shuang} on 2017/8/10.
 */
@Service
public class StoreWineServiceImpl extends BaseServiceImpl<StoreWine, Integer> implements StoreWineService {
    @Autowired
    private PerDayPrizeDao perDayPrizeDao;
    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private StoreWineAttachDao storeWineAttachDao;
    @Autowired
    private StoreWineDao storeWineDao;
    @Autowired
    private StoreWineFlowDao storeWineFlowDao;
    @Autowired
    private StoreWineflowAttachDao storeWineflowAttachDao;

    @Override
    public String storage(Integer userId, String json) throws YesmywineException {
        Double univalent = perDayPrizeDao.findByPrizeId(1).getPrize();//获取存酒单价
        UserInformation userInformation = userInformationDao.findOne(userId);
        VipRule vipRule = userInformation.getVipRule();
        Integer keepfreeDay = vipRule.getKeepDays();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, keepfreeDay);//免费存酒几天后的时间
        JSONObject jsonObject = JSONObject.parseObject(json);
        String orderNumber = jsonObject.getString("orderNumber");
        JSONArray goods = JSONArray.parseArray(jsonObject.getString("goods"));
        StoreWine storeWine = new StoreWine();
        storeWine.setChargingTime(c.getTime());
        storeWine.setOrderNumber(orderNumber);
        storeWine.setFreeDays(keepfreeDay);
        storeWine.setIsOver("0");
        storeWine.setUnivalent(univalent.toString());
        storeWine.setUserId(userInformation.getId());
        List<StoreWineAttach> list = new ArrayList<>();
        for (int i = 0; i < goods.size(); i++) {
            JSONObject jsonObject1 = (JSONObject) goods.get(i);
            String goodsId = jsonObject1.get("goodsId").toString();
            String count = jsonObject1.get("count").toString();
            String goodsName = jsonObject1.get("goodsName").toString();
            String perpirze = jsonObject1.get("perPrize").toString();
            String images = jsonObject1.get("goodsImageUrl").toString();
            StoreWineAttach storeWineAttach = new StoreWineAttach();
            storeWineAttach.setOrderNumber(orderNumber);//订单号
            storeWineAttach.setAlreadyGet(0);//已经领取
            storeWineAttach.setGoodsImageUrl(images);//商品图片
            storeWineAttach.setExtractable(Integer.valueOf(count));//可提取
            storeWineAttach.setFrozen(0);//冻结
            storeWineAttach.setPerPrice(Double.valueOf(perpirze));//单价
            storeWineAttach.setTotal(Integer.valueOf(count));//总共的
            storeWineAttach.setGoodsId(Integer.valueOf(goodsId));//商品Id
            storeWineAttach.setGoodsName(goodsName);//商品名称
            storeWineAttach.setUnivalent(univalent.toString());
            list.add(storeWineAttach);
        }
        storeWineDao.save(storeWine);
        storeWineAttachDao.save(list);
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
    }

/*
 * @author: HS_JAVA
 * @date: 2017/11/14 16:27
 * @param: [userId, json]
 * @return: java.lang.String
 * @describe: 提酒订单展示接口
 */
    @Override
    public String show(Integer userId, String json) {

        JSONObject jsonObject1 = new JSONObject();
        JSONArray storeWineAttachs = JSONArray.parseArray(json);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < storeWineAttachs.size(); i++) {
            Double price = null;
            JSONObject jsonObject = (JSONObject) storeWineAttachs.get(i);
            Integer id = jsonObject.getInteger("id");//Id
            Integer goodsId = jsonObject.getInteger("goodsId");//Id
            Integer count = jsonObject.getInteger("count");//提酒数量
            StoreWineAttach storeWineAttach = storeWineAttachDao.findOne(id);//附表查询一个
            Integer Extractable = storeWineAttach.getExtractable();
            String univalent = storeWineAttach.getUnivalent();
            String orderNumber = storeWineAttach.getOrderNumber();
            StoreWine storeWine = storeWineDao.findByOrderNumber(orderNumber);
            if (Extractable < count) {
                try {
                    ValueUtil.isError("取酒数量大于存酒数量");
                } catch (YesmywineException e) {
                    return ValueUtil.toError(e.getCode(), e.getMessage());
                }
            }
            Date chargeTime = storeWine.getChargingTime();
            Date date = new Date();
            long endTime = date.getTime();
            long beginTime = chargeTime.getTime();
            long betweenDays = (long) ((endTime - beginTime) / (1000 * 60 * 60 * 24) + 0.5);
            if (chargeTime.after(date)) {
                price = 0.0;
            } else {
                BigDecimal bigDecimal1 = new BigDecimal(betweenDays);
                BigDecimal bigDecimal2 = new BigDecimal(Double.valueOf(univalent));
                BigDecimal bigDecima3 = new BigDecimal(Double.valueOf(count));
                price = bigDecimal1.multiply(bigDecimal2).multiply(bigDecima3).setScale(2, ROUND_HALF_DOWN).doubleValue();
            }
            jsonObject.put("fee", price);
            jsonArray.add(jsonObject);
        }

        jsonObject1.put("storeWineAttach", jsonArray);
        return ValueUtil.toJson(HttpStatus.SC_CREATED, jsonObject1);

    }


    @Override
    public String extract(String json, String extractorderNumber, Integer userId) throws YesmywineException {

        JSONArray storeWineAttachs = JSONArray.parseArray(json);
//        JSONArray newStoreWineAttachs = new JSONArray();

        for (int i = 0; i < storeWineAttachs.size(); i++) {//检查是否能提取
            JSONObject jsonObject = (JSONObject) storeWineAttachs.get(i);
            Integer id = jsonObject.getInteger("id");//Id
            Integer count = jsonObject.getInteger("count");//提酒数量
            StoreWineAttach storeWineAttach = storeWineAttachDao.findOne(id);
            Integer Extractable = storeWineAttach.getExtractable();
            if (Extractable < count) {
                try {
                    ValueUtil.isError("存酒数量不足");
                } catch (YesmywineException e) {
                    return ValueUtil.toError(e.getCode(), e.getMessage());
                }
            }
        }
        for (int i = 0; i < storeWineAttachs.size(); i++) {
            JSONObject jsonObject = (JSONObject) storeWineAttachs.get(i);
            Integer id = jsonObject.getInteger("id");//Id
            Integer count = jsonObject.getInteger("count");//提酒数量
            StoreWineAttach storeWineAttach = storeWineAttachDao.findOne(id);
            Integer Extractable = storeWineAttach.getExtractable();

            Integer frozen = storeWineAttach.getFrozen();
            storeWineAttach.setFrozen(frozen + count);//冻结

            storeWineAttach.setExtractable(Extractable - count);//可提取减少
            storeWineAttachDao.save(storeWineAttach);


        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "提酒成功冻结");
    }


    @Override
    public String cancel(String json, String extractorderNumber, Integer userId) throws YesmywineException {

        JSONArray storeWineAttachs = JSONArray.parseArray(json);

        for (int i = 0; i < storeWineAttachs.size(); i++) {//检查是否能提取
            JSONObject jsonObject = (JSONObject) storeWineAttachs.get(i);
            Integer id = jsonObject.getInteger("id");//Id
            Integer count = jsonObject.getInteger("count");//提酒数量
            StoreWineAttach storeWineAttach = storeWineAttachDao.findOne(id);
            Integer frozen = storeWineAttach.getFrozen();
            if (frozen < count) {
                try {
                    ValueUtil.isError("违规取消");
                } catch (YesmywineException e) {
                    return ValueUtil.toError(e.getCode(), e.getMessage());
                }
            }
        }

        for (int i = 0; i < storeWineAttachs.size(); i++) {
            JSONObject jsonObject = (JSONObject) storeWineAttachs.get(i);
            Integer id = jsonObject.getInteger("id");//Id
            Integer count = jsonObject.getInteger("count");//提酒数量
            StoreWineAttach storeWineAttach = storeWineAttachDao.findOne(id);
            Integer Extractable = storeWineAttach.getExtractable();
            Integer frozen = storeWineAttach.getFrozen();
            storeWineAttach.setFrozen(frozen - count);//冻结
            storeWineAttach.setExtractable(Extractable + count);//可提取减少
            storeWineAttachDao.save(storeWineAttach);
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "提酒取消成功");
    }


    @Override
    public PageModel page(PageModel pageModel) throws YesmywineException {
        List<JSONObject> list = pageModel.getContent();
        List<Object> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String storewine = ValueUtil.toJson(list.get(i));
            JSONObject newJson = JSONObject.parseObject(ValueUtil.getFromJson(storewine, "data"));
            String orderNumber = newJson.getString("orderNumber");
            List<StoreWineAttach> list1 = storeWineAttachDao.findExtractable(orderNumber);
            newJson.put("goods", list1);
            newList.add(newJson);
        }
        pageModel.setContent(newList);
//        System.out.println(list.get(0));
        return pageModel;
    }


    @Override
    public String returns(String json, String returnOrderNumber, Integer userId) throws YesmywineException {

        JSONArray storeWineAttachs = JSONArray.parseArray(json);
//        JSONArray newStoreWineAttachs = new JSONArray();

        for (int i = 0; i < storeWineAttachs.size(); i++) {//检查是否能够退货
            JSONObject jsonObject = (JSONObject) storeWineAttachs.get(i);
            Integer goodsId = jsonObject.getInteger("goodsId");//Id
            String orderNumber = jsonObject.getString("orderNumber");//订单号
            Integer count = jsonObject.getInteger("count");//提酒数量
            StoreWineAttach storeWineAttach = storeWineAttachDao.findByOrderNumberAndGoodsId(orderNumber, goodsId);
            Integer Extractable = storeWineAttach.getExtractable();
            if (Extractable < count) {
                try {
                    ValueUtil.isError("存酒数量不足不能退货");
                } catch (YesmywineException e) {
                    return ValueUtil.toError(e.getCode(), e.getMessage());
                }
            }
        }
        for (int i = 0; i < storeWineAttachs.size(); i++) {
            JSONObject jsonObject = (JSONObject) storeWineAttachs.get(i);
            Integer goodsId = jsonObject.getInteger("goodsId");//Id
            String orderNumber = jsonObject.getString("orderNumber");//订单号
            Integer count = jsonObject.getInteger("count");//提酒数量
            StoreWineAttach storeWineAttach = storeWineAttachDao.findByOrderNumberAndGoodsId(orderNumber, goodsId);
            Integer Extractable = storeWineAttach.getExtractable();
            Integer frozen = storeWineAttach.getFrozen();
            storeWineAttach.setFrozen(frozen + count);
            storeWineAttach.setExtractable(Extractable - count);//可提取减少
            storeWineAttachDao.save(storeWineAttach);

        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "退酒冻结成功");
    }

    @Override
    @Transactional
    public String returnComfirm(String json, String returnOrderNumber, Integer userId) throws YesmywineException {
        JSONArray storeWineAttachs = JSONArray.parseArray(json);
        Set<String> set = new HashSet<>();

        for (int i = 0; i < storeWineAttachs.size(); i++) {
            JSONObject jsonObject = (JSONObject) storeWineAttachs.get(i);
            Integer goodsId = jsonObject.getInteger("goodsId");//Id
            String orderNumber = jsonObject.getString("orderNumber");//订单号
            Integer count = jsonObject.getInteger("count");//提酒数量
            StoreWineAttach storeWineAttach = storeWineAttachDao.findByOrderNumberAndGoodsId(orderNumber, goodsId);
            Integer frozen = storeWineAttach.getFrozen();
            Integer returnAmount = storeWineAttach.getReturnAmount();
            storeWineAttach.setReturnAmount(returnAmount + count);//退货量增加
            storeWineAttach.setFrozen(frozen - count);//冻结解除
            storeWineAttachDao.save(storeWineAttach);
            set.add(storeWineAttach.getOrderNumber());
        }
        for (String orderNumber : set) {
            Integer amount = storeWineAttachDao.sumByOrderNumber(orderNumber);
            if (amount == 0) {
                storeWineDao.findByOrderNumbersetIsover(orderNumber);
            }
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "退货完成");
    }

    @Override
    @Transactional
    public String returnComfirmcancel(String json, String returnOrderNumber, Integer userId) throws YesmywineException {
        JSONArray storeWineAttachs = JSONArray.parseArray(json);
        Set<String> set = new HashSet<>();

        for (int i = 0; i < storeWineAttachs.size(); i++) {
            JSONObject jsonObject = (JSONObject) storeWineAttachs.get(i);
            Integer goodsId = jsonObject.getInteger("goodsId");//Id
            String orderNumber = jsonObject.getString("orderNumber");//订单号
            Integer count = jsonObject.getInteger("count");//提酒数量
            StoreWineAttach storeWineAttach = storeWineAttachDao.findByOrderNumberAndGoodsId(orderNumber, goodsId);
            Integer frozen = storeWineAttach.getFrozen();
            Integer extractable = storeWineAttach.getExtractable();
            Integer returnAmount = storeWineAttach.getReturnAmount();
            storeWineAttach.setFrozen(frozen - count);//冻结解除
            storeWineAttach.setExtractable(extractable + count);
            storeWineAttachDao.save(storeWineAttach);
            set.add(storeWineAttach.getOrderNumber());
        }
        for (String orderNumber : set) {
            Integer amount = storeWineAttachDao.sumByOrderNumber(orderNumber);
            if (amount == 0) {
                storeWineDao.findByOrderNumbersetIsover(orderNumber);
            }
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "退货取消完成");
    }


    @Override
    public String returnShow(Integer userId, String json) {
        JSONObject jsonObject1 = new JSONObject();
        JSONArray storeWineAttachs = JSONArray.parseArray(json);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < storeWineAttachs.size(); i++) {
            Double price = null;
            JSONObject jsonObject = (JSONObject) storeWineAttachs.get(i);
            Integer goodsId = jsonObject.getInteger("goodsId");//Id
            String orderNumber = jsonObject.getString("orderNumber");//订单号
            Integer count = jsonObject.getInteger("count");//提酒数量
            StoreWineAttach storeWineAttach = storeWineAttachDao.findByOrderNumberAndGoodsId(orderNumber, goodsId);
            Integer Extractable = storeWineAttach.getExtractable();
            String univalent = storeWineAttach.getUnivalent();
            StoreWine storeWine = storeWineDao.findByOrderNumber(orderNumber);
            if (Extractable < count) {
                try {
                    ValueUtil.isError("存酒数量不足");
                } catch (YesmywineException e) {
                    return ValueUtil.toError(e.getCode(), e.getMessage());
                }
            }
            //计算存酒价格
            Date chargeTime = storeWine.getChargingTime();
            Date date = new Date();
            long endTime = date.getTime();
            long beginTime = chargeTime.getTime();
            long betweenDays = (long) ((endTime - beginTime) / (1000 * 60 * 60 * 24) + 0.5);
            if (chargeTime.after(date)) {
                price = 0.0;
            } else {
                BigDecimal bigDecimal1 = new BigDecimal(betweenDays);
                BigDecimal bigDecimal2 = new BigDecimal(Double.valueOf(univalent));
                BigDecimal bigDecima3 = new BigDecimal(Double.valueOf(count));
                price = bigDecimal1.multiply(bigDecimal2).multiply(bigDecima3).setScale(2, ROUND_HALF_DOWN).doubleValue();
            }
            jsonObject.put("fee", price);
            jsonArray.add(jsonObject);
        }
        jsonObject1.put("storeWineAttach", jsonArray);
        return ValueUtil.toJson(HttpStatus.SC_CREATED, jsonObject1);

    }


    @Override
    @Transactional
    public String comfirm(String json, String extractorderNumber, Integer userId) throws YesmywineException {
//        提酒确认
        JSONArray storeWineAttachs = JSONArray.parseArray(json);
        Set<String> set = new HashSet<>();

        List<StoreWineflowAttach> list = new ArrayList<>();
        BigDecimal fees = new BigDecimal(0);
        for (int i = 0; i < storeWineAttachs.size(); i++) {
            JSONObject jsonObject = (JSONObject) storeWineAttachs.get(i);
            Integer id = jsonObject.getInteger("id");//主键Id
            Integer count = jsonObject.getInteger("count");//提酒数量
            String fee = jsonObject.getString("fee");//提酒费用
            BigDecimal bigDecimal = new BigDecimal(Double.valueOf(fee));
            fees = fees.add(bigDecimal);
            StoreWineAttach storeWineAttach = storeWineAttachDao.findOne(id);
            Integer AlreadyGet = storeWineAttach.getAlreadyGet();
            Integer frozen = storeWineAttach.getFrozen();
            StoreWineflowAttach storeWineflowAttach = new StoreWineflowAttach();
            storeWineflowAttach.setGoodsId(storeWineAttach.getGoodsId());
            storeWineflowAttach.setGoodsName(storeWineAttach.getGoodsName());
            storeWineflowAttach.setGoodsImageUrl(storeWineAttach.getGoodsImageUrl());
            storeWineflowAttach.setPerPrice(storeWineAttach.getPerPrice().toString());
            storeWineflowAttach.setOrderNumber(storeWineAttach.getOrderNumber());
            storeWineflowAttach.setFee(fee);
            storeWineflowAttach.setAmount(count);
            storeWineflowAttach.setExtractorderNumber(extractorderNumber);
            list.add(storeWineflowAttach);
            set.add(storeWineAttach.getOrderNumber());
            storeWineAttach.setFrozen(frozen - count);//冻结解除
            storeWineAttach.setAlreadyGet(AlreadyGet + count);
            storeWineAttachDao.save(storeWineAttach);
        }
//        保存提酒流水
        StoreWineFlow storeWineFlow = new StoreWineFlow();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        storeWineFlow.setExtractTime(sdf.format(new Date()));
        storeWineFlow.setExtractorderNumber(extractorderNumber);
        storeWineFlow.setFee(fees.setScale(2, ROUND_HALF_DOWN).toString());
        storeWineFlow.setUserId(userId);
        storeWineFlowDao.save(storeWineFlow);
        storeWineflowAttachDao.save(list);
        for (String orderNumber : set) {
            Integer amount = storeWineAttachDao.sumByOrderNumber(orderNumber);
            if (amount == 0) {
                storeWineDao.findByOrderNumbersetIsover(orderNumber);
            }
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "支付完成");
    }
}
