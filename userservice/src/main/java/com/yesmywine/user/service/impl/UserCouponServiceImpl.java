
package com.yesmywine.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.user.dao.CouponDao;
import com.yesmywine.user.dao.UserCouponDao;
import com.yesmywine.user.entity.Coupon;
import com.yesmywine.user.entity.UserCoupon;
import com.yesmywine.user.service.UserCouponService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * Created by ${shuang} on 2017/4/19.
 */
@Service
public class UserCouponServiceImpl extends BaseServiceImpl<UserCoupon, Integer> implements UserCouponService {
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private UserCouponDao userCouponDao;

    @Override
    public Boolean draw(Integer userId, Integer couponId) throws YesmywineException {//领取
//      领取优惠券
        Coupon coupon = couponDao.findOne(couponId);
        Integer count = coupon.getAmount();//总数
        Integer hasDraw = coupon.getUsedDrawCount();//已经被领取的
        Integer userCount = coupon.getUserCount();//每人可以领取数量
        List<UserCoupon> list = userCouponDao.findByUserIdAndCouponId(userId, couponId);
        if (hasDraw < count && list.size() < userCount) {
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setUserId(userId);
            userCoupon.setCouponId(couponId);
            userCoupon.setCodeNumber(this.candomNum());
            coupon.setUsedDrawCount(hasDraw + 1);//被抽取数量加一
            coupon.setAmount(count - 1);//总数量减一
            coupon.setStatus(4);//状态变成已使用
            couponDao.save(coupon);
            userCouponDao.save(userCoupon);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String use(Integer userId, Integer userCouponId) throws YesmywineException {//使用
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String currentDate1 = df.format(new Date());//当前日期
        UserCoupon userCoupon = userCouponDao.findOne(userCouponId);
        Integer couponId = userCoupon.getCouponId();
        Coupon coupon = couponDao.findOne(couponId);
        String activeStartTime = coupon.getActiveStartTime();
        String activeEndTime = coupon.getActiveEndTime();
        try {
//            判断是否在可用日期之内
            Date currentDate = (Date) df.parseObject(currentDate1);
            Date Start = (Date) df.parseObject(activeStartTime);
            Date End = (Date) df.parseObject(activeEndTime);
            if ((currentDate.after(Start) && currentDate.before(End)) && userCoupon.getStatus() != 5) {//在日期内
                userCoupon.setUserTime(new Date());
                userCoupon.setStatus(5);
                userCouponDao.save(userCoupon);
            } else {
                return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR, "优惠券过期");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
    }

    @Override
    public String ListOnBalance(Integer userId, String json) throws YesmywineException {
//        结算页面展示可用
        JSONArray obj = JSON.parseArray(json);
        List<JSONObject> list = new ArrayList<>();
        for (int i = 0; i < obj.size(); i++) {//遍历json
            JSONObject jsonObject = (JSONObject) obj.get(i);
            Object key1 = jsonObject.get("activitySubtotal");
            JSONArray goodsarray = (JSONArray) jsonObject.get("goods");
            if (ValueUtil.notEmpity(key1)) {
                String key = jsonObject.get("activitySubtotal").toString();
                BigDecimal acttotal = new BigDecimal(Double.valueOf(key));
                BigDecimal total = new BigDecimal(0);
                for (int j = 0; j < goodsarray.size(); j++) {
                    JSONObject jsonObject1 = (JSONObject) goodsarray.get(j);
                    String prize = jsonObject1.get("price").toString();
                    BigDecimal bigDecimal = new BigDecimal(Double.valueOf(prize));
                    total = total.add(bigDecimal).setScale(2, ROUND_HALF_DOWN);
                }
                for (int j = 0; j < goodsarray.size(); j++) {
                    JSONObject jsonObject1 = new JSONObject();
                    JSONObject jsonObject2 = (JSONObject) goodsarray.get(j);
                    String prize = jsonObject2.get("price").toString();
                    BigDecimal bigDecimal = new BigDecimal(Double.valueOf(prize));
                    Double newPrice = bigDecimal.divide(total).multiply(acttotal).setScale(2, ROUND_HALF_DOWN).doubleValue();
                    jsonObject1.put("goodsId", jsonObject2.get("goodsId"));
                    jsonObject1.put("categoryId", jsonObject2.get("categoryId"));
                    jsonObject1.put("brandId", jsonObject2.get("brandId"));
                    jsonObject1.put("price", newPrice);
                    list.add(jsonObject1);
                }
            } else {
                for (int j = 0; j < goodsarray.size(); j++) {
                    JSONObject jsonObject3 = (JSONObject) goodsarray.get(j);
                    list.add(jsonObject3);
                }
            }
        }
        Map<String, Double> categoryMap = new HashMap<>();
        Map<String, Double> brandIdMap = new HashMap<>();
        for (JSONObject jsonObject : list) {
            String category = jsonObject.getString("categoryId");
            String brandId = jsonObject.getString("brandId");
            if (ValueUtil.isEmpity(brandId)) {
                brandId = "0";
            }
            String price = jsonObject.getString("price");
            BigDecimal bigDecimal = new BigDecimal(Double.valueOf(price));

            if (categoryMap.size() == 0) {
                categoryMap.put(category, Double.valueOf(price));
            } else {
                Set set = categoryMap.keySet();
                Iterator iter = set.iterator();
                List<String> categoryList = new ArrayList<>();
                while (iter.hasNext()) {
                    String key = (String) iter.next();
                    categoryList.add(key);
                }
                String result = this.getchildhttp(Integer.valueOf(category));
                JSONArray array = JSON.parseArray(result);//子类分类
                List<String> chidList = new ArrayList<>();
                for (int i = 0; i < array.size(); i++) {
                    chidList.add(array.get(i).toString());
                }
                if (categoryList.contains(category) || !Collections.disjoint(categoryList, chidList)) {//判断两个集合是否有相同的元素
                    Double value = categoryMap.get(category);
                    BigDecimal bigDecimal1 = new BigDecimal(Double.valueOf(value));
                    Double newprize = bigDecimal1.add(bigDecimal).setScale(2, ROUND_HALF_DOWN).doubleValue();
                    categoryMap.put(category, newprize);
                } else {
                    categoryMap.put(category, Double.valueOf(price));
                }
            }

            if (brandIdMap.size() == 0) {
                brandIdMap.put(brandId, Double.valueOf(price));
            } else {
                Set set2 = brandIdMap.keySet();
                Iterator iter2 = set2.iterator();
                List<String> brandIdList = new ArrayList<>();
                while (iter2.hasNext()) {
                    String key = (String) iter2.next();
                    brandIdList.add(key);
                }
                if (brandIdList.contains(brandId)) {
                    Double value = brandIdMap.get(brandId);
                    BigDecimal bigDecimal1 = new BigDecimal(Double.valueOf(value));
                    Double newprize = bigDecimal1.add(bigDecimal).setScale(2, ROUND_HALF_DOWN).doubleValue();
                    brandIdMap.put(brandId, newprize);
                } else {
                    brandIdMap.put(brandId, Double.valueOf(price));
                }
            }
        }
        String objects = ValueUtil.getFromJson(this.selfList(userId), "data", "use");
        Set<JSONObject> all = new HashSet<>();//可选择
        JSONArray newJosn = JSON.parseArray(objects);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String currentDate1 = df.format(new Date());//当前日期
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < newJosn.size(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) newJosn.get(i);
                String couponId = jsonObject.get("couponId").toString();
                Coupon coupon = couponDao.findOne(Integer.valueOf(couponId));
                String activeStartTime = coupon.getActiveStartTime();
                String activeEndTime = coupon.getActiveEndTime();
                Date currentDate = (Date) df.parseObject(currentDate1);
                Date Start = (Date) df.parseObject(activeStartTime);
                Date End = (Date) df.parseObject(activeEndTime);
                if (currentDate.after(Start) && currentDate.before(End)) {
                    jsonArray.add(newJosn.get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String couponId = jsonObject.get("couponId").toString();
            Coupon coupon = couponDao.findOne(Integer.valueOf(couponId));
            String categroyId = coupon.getCategoryId().toString();//分类Id
            String brandId = coupon.getBrandId().toString();//品牌Id
            Double full = coupon.getFull().doubleValue();

            Set set = categoryMap.keySet();
            Iterator categoryiter = set.iterator();
            while (categoryiter.hasNext()) {
                String key = (String) categoryiter.next();
                Double value = categoryMap.get(key);
                if (categroyId.equals(key) && full <= value) {
                    all.add(jsonObject);
                    break;
                }
            }

            Set set2 = brandIdMap.keySet();
            Iterator brandIditer = set2.iterator();
            while (brandIditer.hasNext()) {
                String key = (String) brandIditer.next();
                Double value = brandIdMap.get(key);
                if (brandId.equals(key) && full <= value) {
                    all.add(jsonObject);
                    break;
                }
            }
        }
        return ValueUtil.toJson(HttpStatus.SC_OK, all);

    }


    @Override
    public String returns(Integer userId, Integer id) {
//        退优惠券
        List<UserCoupon> list = new ArrayList<>();
        UserCoupon userCoupon = userCouponDao.findOne(id);
        userCoupon.setStatus(4);
        userCouponDao.save(list);
        return "success";
    }

    @Override
    public String selfList(Integer userId) throws YesmywineException {//个人优惠券详情列表
        List<UserCoupon> list = userCouponDao.findByUserId(userId);
        List<Map<String, String>> use = new ArrayList<>();//能够使用
        List<Map<String, String>> used = new ArrayList<>();//使用过
        List<Map<String, String>> overDue = new ArrayList<>();//过期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String currentDate1 = df.format(new Date());//当前日期
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<>();
            UserCoupon userCoupon = list.get(i);
            Integer couponId = userCoupon.getCouponId();
            Coupon coupon = couponDao.findOne(couponId);
            String activeStartTime = coupon.getActiveStartTime();
            String activeEndTime = coupon.getActiveEndTime();
            map.put("full", coupon.getFull().toString());
            map.put("cut", coupon.getCut().toString());
            map.put("activeStartTime", activeStartTime);
            map.put("activeEndTime", activeEndTime);
            map.put("platforms", coupon.getPlatforms());
            map.put("codeNumber", userCoupon.getCodeNumber());
            map.put("couponId", coupon.getId().toString());
            map.put("userCouponId", userCoupon.getId().toString());
            if (coupon.getCategoryId() != 0) {
                // 通过分类Id查分类名称
                map.put("categoryName", this.http(coupon.getCategoryId(), null));
            } else {
                map.put("categoryName", "全分类可用");
            }
            if (coupon.getBrandId() != 0) {
                // 通过分类Id查分类名称
                map.put("brandName", this.http(null, coupon.getBrandId()));
            } else {
                map.put("brandName", "全品牌可用");
            }
            try {
                Date currentDate = (Date) df.parseObject(currentDate1);
                Date Start = (Date) df.parseObject(activeStartTime);
                Date End = (Date) df.parseObject(activeEndTime);
                Integer status = userCoupon.getStatus();
                if (status == 5) {
                    used.add(map);
                } else if (currentDate.after(End) && status == 4) {
                    overDue.add(map);
                } else if (currentDate.before(End) && status == 4) {
                    use.add(map);
                } else if (currentDate1.equals(activeStartTime) || currentDate1.equals(activeEndTime)) {
                    use.add(map);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return ValueUtil.toJson("use", use, "used", used, "overDue", overDue);
    }


    public String candomNum() {
        // TODO: 2017/4/18 优惠券随机数编号
        String generateSource = "0123456789";
        String rtnStr = "";
        for (int i = 0; i < 10; i++) {
            //循环随机获得当次字符，并移走选出的字符
            String nowStr = String.valueOf(generateSource.charAt((int) Math.floor(Math.random() * generateSource.length())));
            rtnStr += nowStr;
            generateSource = generateSource.replaceAll(nowStr, "");
        }
        return rtnStr;
    }

    public String http(Integer categoryId, Integer brandId) {
//        http://47.89.18.26:8184/goods/sku/findByCategoryId?categoryId=1&skuId=1
        String temp = null;
        String result = null;
        //查分类的名字
        if (ValueUtil.notEmpity(categoryId)) {
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/goods/categories/showOne/itf", RequestMethod.get);
            httpRequest.addParameter("categoryId", categoryId);
            httpRequest.run();
            temp = httpRequest.getResponseContent();
            result = ValueUtil.getFromJson(temp, "data");
        } else if (ValueUtil.notEmpity(brandId)) {
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/goods/properValue/showOne/itf", RequestMethod.get);
            httpRequest.addParameter("valueId", brandId);
            httpRequest.run();
            temp = httpRequest.getResponseContent();
            result = ValueUtil.getFromJson(temp, "data");
        }

        return result;
    }


    public String getchildhttp(Integer categoryId) {
//        获取分类的子类
        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/goods/categories/getChildren/itf", RequestMethod.get);
        httpRequest.addParameter("parentId", categoryId);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        String result = ValueUtil.getFromJson(temp, "data");
        return result;
    }


}

