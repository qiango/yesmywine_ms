package com.yesmywine.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.bean.GoodsTypeEnum;
import com.yesmywine.activity.bean.WareEnum;
import com.yesmywine.activity.controller.IfTTT;
import com.yesmywine.activity.entity.*;
import com.yesmywine.activity.ifttt.service.*;
import com.yesmywine.activity.service.*;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.bean.sqlpage.PaginationDao;
import com.yesmywine.db.base.ehcache.CacheStatement;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.util.number.DoubleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by SJQ on 2017/5/10.
 */
@Service
public class UseServiceImpl extends PaginationDao implements UseService {
    @Autowired
    private RegulationGoodsService regulationGoodsService;
    @Autowired
    private RegulationService regulationService;
    @Autowired
    private IfTTT ifTTT;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ActivityGoodsService activitygoodsService;
    @Autowired
    private ActivityService activityService;
//    @Autowired
//    private CacheManager cacheManager;

    @Override
    public JSONObject runCart(String jsonData, String username) throws YesmywineException {

        JSONArray array = JSON.parseArray(jsonData);
        JSONObject resultObj = new JSONObject();
        /************************
         *商品活动
         *************************/
        resultObj = getEveryGoodsActivity(array, resultObj, username);
        System.out.println("===============================完成++++++++++++++++++++++++++");
        return resultObj;
    }

    @Cacheable(value = CacheStatement.ACTIVITY_VALUE, key = "'getEveryGoodsActivity_'+#array+#username")
    private JSONObject getEveryGoodsActivity(JSONArray array, JSONObject resultObj, String username) throws YesmywineException {
        List<CartGoogsInfo> everyGoodsList = new ArrayList<>();//每個商品的信息（包含活动）
        Double originTotalPrice = 0.0;//购物车所有商品的原价
        Double userDiscount = null;
        if (username == null) {
            userDiscount = 1.0;
        } else {
            JSONObject userJson = UserUtils.getUserInfo(username);
            userDiscount = Double.valueOf(userJson.getJSONObject("vipRule").getString("discount"));
        }
        List<Map<String, Object>> allGoodsInfoList = new ArrayList<>();//用于计算随心购
        Double memberPrivilegeAmmount = 0.0;//会员优惠总额

        List<List<JSONObject>> activityAmmountList = new ArrayList<>();//活动总价集合
        List<JSONObject> activityGoodsList = null;//活动商品集合
        for (int i = 0; i < array.size(); i++) {
            JSONObject goodsObj = (JSONObject) array.get(i);
            String goodsId = goodsObj.getString("goodsId");
            GoodsMirroring goods = goodsService.findById(goodsId);
            getOneGoodsActivityInfo(goodsObj, goods, originTotalPrice, userDiscount, memberPrivilegeAmmount, everyGoodsList, allGoodsInfoList);
        }
        List<CartGoogsInfo> noActivityGoodsList = new ArrayList<>();//无活动的商品
        List<CartGoogsInfo> noChooseGoosList = new ArrayList<>();
        JSONArray allCouponArray = new JSONArray();//优惠券需要使用的总参数集合
        JSONArray noActivityCouponArray = new JSONArray();//单个集合
        Iterator<CartGoogsInfo> it = everyGoodsList.iterator();
        Double allGoogsTotalPrice = 0.0;//原商品总价
        Double activityTotalPrice = 0.0;//活动价格
        JSONArray couponArray = new JSONArray();
        while (it.hasNext()) {//移除无活动的商品，加入 noActivityGoodsList;未勾选的商品加入
            CartGoogsInfo cartGoogsInfo = it.next();
            Boolean isChoose = cartGoogsInfo.getChoose();
            if (isChoose) {
                JSONObject couponObj = new JSONObject();
                couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
                couponObj.put("price", cartGoogsInfo.getSubTotal());
                couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
                couponObj.put("brandId", cartGoogsInfo.getBrandId() == null ? 0 : cartGoogsInfo.getBrandId());//若品牌Id为空，默认为0
                couponArray.add(couponObj);
                JSONObject couponSubTotal = new JSONObject();
                couponSubTotal.put("activitySubTotal", cartGoogsInfo.getActivityPrice());
                couponSubTotal.put("goods", couponArray);
                allCouponArray.add(couponSubTotal);
                couponArray = new JSONArray();

                allGoogsTotalPrice = DoubleUtils.add(allGoogsTotalPrice, cartGoogsInfo.getSubTradeTotal() < 0 ? 0.0 : cartGoogsInfo.getSubTradeTotal());
                activityTotalPrice = DoubleUtils.add(activityTotalPrice, cartGoogsInfo.getActivityPrice() < 0 ? 0.0 : cartGoogsInfo.getActivityPrice());
//                memberPrivilegeAmmount = DoubleUtils.add(memberPrivilegeAmmount, cartGoogsInfo.getMemberRivilege());
            }
            Integer activityId = cartGoogsInfo.getActivityId();
            if (activityId == null) {
                noActivityGoodsList.add(cartGoogsInfo);
                JSONObject couponObj = new JSONObject();
                couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
                couponObj.put("price", cartGoogsInfo.getSubTotal());
                couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
                couponObj.put("brandId", cartGoogsInfo.getBrandId());
                noActivityCouponArray.add(couponObj);
                it.remove();
            }

        }
//        if (everyGoodsList.size() > 1) {
//            Collections.sort(everyGoodsList, new Comparator<CartGoogsInfo>() {//先按活动id排序，若活动id相同则按规则id排序
//                @Override
//                public int compare(CartGoogsInfo goodsA, CartGoogsInfo goodsB) {
//                    Integer activityIdA = goodsA.getActivityId();
//                    Integer activityIdB = goodsB.getActivityId();
//                    int i = activityIdA.compareTo(activityIdB);
//                    if (i == 0) {
//                        Integer regulationIdA = goodsA.getRegulationId();
//                        Integer regulationIdB = goodsB.getRegulationId();
//                        int b = regulationIdA.compareTo(regulationIdB);
//                        return b;
//                    }
//                    return i;
//                }
//            });
//        }
        //将相同规则的商品放在同一集合
        JSONArray everyGoodsJsonArray = new JSONArray();
        JSONArray changeArray = null;
        Integer oldRegulationId = null;


        Double originSubtotal = 0.0;//活动原价格小计
        Double activitySubtotal = 0.0;//活动价格小计
//        JSONArray couponArray = new JSONArray();

        JSONArray allCoupons = new JSONArray();

        changeArray = new JSONArray();
        for (int k = 0; k < everyGoodsList.size(); k++) {
            CartGoogsInfo cartGoogsInfo = everyGoodsList.get(k);
            everyGoodsJsonArray.add(cartGoogsInfo);
            changeArray.add(cartGoogsInfo);

//            JSONObject couponObj = new JSONObject();
//            couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
//            couponObj.put("price", cartGoogsInfo.getSubTotal());
//            couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
//            couponObj.put("brandId", cartGoogsInfo.getBrandId());
//            couponArray.add(couponObj);
//            JSONObject couponSubTotal = new JSONObject();
//            couponSubTotal.put("activitySubTotal", cartGoogsInfo.getActivityPrice());
//            couponSubTotal.put("goods", couponArray);
//            allCouponArray.add(couponSubTotal);
//            couponArray = new JSONArray();
        }
        addGoodsActivityInfo(changeArray, username, allCoupons);
//        for (int k = 0; k < everyGoodsList.size(); k++) {
//            CartGoogsInfo cartGoogsInfo = everyGoodsList.get(k);
//            originSubtotal = DoubleUtils.add(originSubtotal,cartGoogsInfo.getSubTotal());
//            activitySubtotal = DoubleUtils.add(activitySubtotal,cartGoogsInfo.getActivityPrice());
//            Integer regulationId = cartGoogsInfo.getRegulationId();
//            if (!regulationId.equals(oldRegulationId) && k != 0) {
//                JSONObject subTotalSub = new JSONObject();
//                addGoodsActivityInfo(changeArray,username,allCoupons);
//                CartGoogsInfo beforeOne = (CartGoogsInfo) changeArray.get(changeArray.size() - 1);
//                subTotalSub.put("activityId", beforeOne.getActivityId());
//                subTotalSub.put("actionCode", beforeOne.getActivity().getActionCode());
//                subTotalSub.put("activityName", beforeOne.getRegulation().getName());
//                subTotalSub.put("isShare", beforeOne.getActivity().getShare());
//                if(beforeOne.getActivity().getShare()){
//                    String activityType = beforeOne.getActivity().getTypeName();
//                    subTotalSub.put("activityType", activityType.substring(0,2));
//                }else{
//                    subTotalSub.put("activityType", beforeOne.getActivity().getTypeName());
//                }
//                subTotalSub.put("activityType", beforeOne.getActivity().getTypeName());
//                subTotalSub.put("originSubtotal", DoubleUtils.sub(originSubtotal,cartGoogsInfo.getSubTotal()));
//                subTotalSub.put("goodsesInfo", changeArray);
//                subTotalSub.put("activitySubtotal",  DoubleUtils.sub(activitySubtotal,cartGoogsInfo.getActivityPrice()));
//                subTotalSub.put("balance", DoubleUtils.sub(subTotalSub.getDouble("originSubtotal"), subTotalSub.getDouble("activitySubtotal")));
//                everyGoodsJsonArray.add(subTotalSub);
//                JSONObject couponSubTotal = new JSONObject();
//                couponSubTotal.put("activitySubTotal", DoubleUtils.sub(activitySubtotal,cartGoogsInfo.getActivityPrice()));
//                couponSubTotal.put("goods", couponArray);
//                allCouponArray.add(couponSubTotal);
//                couponArray = new JSONArray();
//
//                originSubtotal = 0.0;
//                activitySubtotal = 0.0;
//                originSubtotal = DoubleUtils.add(originSubtotal,cartGoogsInfo.getSubTotal());
//                activitySubtotal = DoubleUtils.add(activitySubtotal,cartGoogsInfo.getActivityPrice());
//                if(k==everyGoodsList.size()-1){
//                    if(changeArray.size()==1){
//                        changeArray = new JSONArray();
//                        changeArray.add(cartGoogsInfo);
//                    }
//                    addGoodsActivityInfo(changeArray,username,allCoupons);
//                    subTotalSub = new JSONObject();
//                    subTotalSub.put("activityId", cartGoogsInfo.getActivityId());
//                    subTotalSub.put("actionCode", cartGoogsInfo.getActivity().getActionCode());
//                    subTotalSub.put("activityName", cartGoogsInfo.getActivity().getTypeName());
//                    subTotalSub.put("isShare", cartGoogsInfo.getActivity().getShare());
//                    if(cartGoogsInfo.getActivity().getShare()){
//                        String activityType = cartGoogsInfo.getActivity().getTypeName();
//                        subTotalSub.put("activityType", activityType.substring(0,2));
//                    }else{
//                        subTotalSub.put("activityType", cartGoogsInfo.getActivity().getTypeName());
//                    }
//                    subTotalSub.put("originSubtotal", originSubtotal);
//                    subTotalSub.put("goodsesInfo", changeArray);
//                    subTotalSub.put("activitySubtotal",  activitySubtotal);
//                    subTotalSub.put("balance", DoubleUtils.sub(subTotalSub.getDouble("originSubtotal"), subTotalSub.getDouble("activitySubtotal")));
//                    everyGoodsJsonArray.add(subTotalSub);
////                    if(){
////
////                    }
//                    JSONObject couponObj = new JSONObject();
//                    couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
//                    couponObj.put("price", cartGoogsInfo.getSubTotal());
//                    couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
//                    couponObj.put("brandId", cartGoogsInfo.getBrandId());
//                    couponArray.add(couponObj);
//                    couponSubTotal = new JSONObject();
//                    couponSubTotal.put("activitySubTotal", activitySubtotal);
//                    couponSubTotal.put("goods", couponArray);
//                    allCouponArray.add(couponSubTotal);
//                }else{
//                    changeArray = new JSONArray();
//                    changeArray.add(cartGoogsInfo);
//                    JSONObject couponObj = new JSONObject();
//                    couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
//                    couponObj.put("price", cartGoogsInfo.getSubTotal());
//                    couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
//                    couponObj.put("brandId", cartGoogsInfo.getBrandId());
//                    couponArray.add(couponObj);
//                }
//
//            } else {
//                if (changeArray == null) {
//                    changeArray = new JSONArray();
//                }
//                changeArray.add(cartGoogsInfo);
//                //优惠券用的商品信息
//                JSONObject couponObj = new JSONObject();
//                couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
//                couponObj.put("price", cartGoogsInfo.getSubTotal());
//                couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
//                couponObj.put("brandId", cartGoogsInfo.getBrandId());
//                couponArray.add(couponObj);
//
//                if(k==everyGoodsList.size()-1){
//                    addGoodsActivityInfo(changeArray,username,allCoupons);
//                    JSONObject subTotalSub = new JSONObject();
//                    subTotalSub.put("activityId", cartGoogsInfo.getActivityId());
//                    subTotalSub.put("actionCode", cartGoogsInfo.getActivity().getActionCode());
//                    subTotalSub.put("activityName", cartGoogsInfo.getRegulation().getName());
//                    subTotalSub.put("isShare", cartGoogsInfo.getActivity().getShare());
//                    if(cartGoogsInfo.getActivity().getShare()){
//                        String activityType = cartGoogsInfo.getActivity().getTypeName();
//                        subTotalSub.put("activityType", activityType.substring(0,2));
//                    }else{
//                        subTotalSub.put("activityType", cartGoogsInfo.getActivity().getTypeName());
//                    }
//                    subTotalSub.put("originSubtotal", originSubtotal);
//                    subTotalSub.put("goodsesInfo", changeArray);
//                    subTotalSub.put("activitySubtotal",  activitySubtotal);
//                    subTotalSub.put("balance", DoubleUtils.sub(subTotalSub.getDouble("originSubtotal"), subTotalSub.getDouble("activitySubtotal")));
//                    everyGoodsJsonArray.add(subTotalSub);
//                    JSONObject couponSubTotal = new JSONObject();
//                    couponSubTotal.put("activitySubTotal", activitySubtotal);
//                    couponSubTotal.put("goods", couponArray);
//                    allCouponArray.add(couponSubTotal);
//                }
//            }
//            oldRegulationId = regulationId;
//        }

        if (noActivityGoodsList.size() > 0) {
            for (CartGoogsInfo cartGoogsInfo : noActivityGoodsList) {
                everyGoodsJsonArray.add(cartGoogsInfo);
            }
//            JSONObject obj = new JSONObject();
//            obj.put("activitySubTotal", null);
//            obj.put("goods", noActivityCouponArray);
//            allCouponArray.add(obj);
//            JSONObject json = new JSONObject();
//            json.put("activityId", null);
//            json.put("activityName", null);
//            json.put("goodsesInfo", noActivityGoodsList);
//            everyGoodsJsonArray.add(json);
        }

        resultObj.put("couponParams", allCouponArray);
        resultObj.put("allCoupons", allCoupons);
        resultObj.put("originTotalPrice", allGoogsTotalPrice);
        resultObj.put("nowTotalPrice", DoubleUtils.sub(activityTotalPrice, memberPrivilegeAmmount) < 0 ? 0.0 : DoubleUtils.sub(activityTotalPrice, memberPrivilegeAmmount));
        resultObj.put("everyGoodsInfo", everyGoodsJsonArray);
        resultObj.put("memberRivilege", memberPrivilegeAmmount);
        resultObj.put("balance", DoubleUtils.sub(allGoogsTotalPrice, activityTotalPrice));//差额

        return resultObj;


//            if(true){
//                return null;
//            }
        /************************
         *购物车中的商品是否可参加随心购
         *************************/
//        resultObj = isCanJoinAsOneWish(resultObj, allGoodsInfoList, resultObj.getDouble("cheapestPrice"));
//        return resultObj;
    }

    //获取单个商品的活动信息
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE, key = "'getOneGoodsActivityInfo_'+#goodsObj+#goods+#userDiscount")
    private void getOneGoodsActivityInfo(JSONObject goodsObj, GoodsMirroring goods, Double originTotalPrice, Double userDiscount, Double memberPrivilegeAmmount, List<CartGoogsInfo> everyGoodsList, List<Map<String, Object>> allGoodsInfoList) throws YesmywineException {

        String goodsCount = goodsObj.getString("goodsCount");
        String childGoodsId = goodsObj.getString("childGoodsId");
        ValueUtil.verify(goodsCount, "goodsCount");
        Integer regulationId = null;
        String parmaRegulationId = goodsObj.getString("regulationId");
        if (parmaRegulationId != null) {
            regulationId = Integer.valueOf(parmaRegulationId);
        }
        Integer cartActivityId = null;
        Boolean isChoose = Boolean.valueOf(goodsObj.getString("isChoose"));

        if (goods == null) {
            ValueUtil.isError("传递参数中包含活动服务不存在的商品");
        }
        String goodsId = goods.getGoodsId();
        String saleModel = goods.getSaleModel();
        String goodsType = goods.getGoodsType();
        String goodsName = goods.getGoodsName();
        String goodsImg = goods.getGoodsImageUrl();
        Boolean goodsIsMember = goods.getMember();
        String goodsCode = goods.getGoodsCode();
        String goStatus = goods.getGoStatus();
        String virtualType = goods.getVirtualType();
        Boolean isKeep = goods.getKeep();
        Set<GoodsSku> skuInfo = goods.getSkuInfo();
        Integer goodsCategoryId = Integer.valueOf(goods.getCategoryId());
        Integer goodsBrandId = null;
        String parmasBrandId = goods.getBrandId();
        if (parmasBrandId != null) {
            goodsBrandId = Integer.valueOf(parmasBrandId);
        }
        Double goodsPrice = Double.valueOf(goods.getSalePrice());//商品单价
        Double singleGoodsTotalPrice = goodsPrice * Integer.valueOf(goodsCount);//单个商品总价

        ValueUtil.verify(singleGoodsTotalPrice, "goodsTotalPrice");

        originTotalPrice = DoubleUtils.add(originTotalPrice, singleGoodsTotalPrice);

        Double nowPrice = 0.0;//结果
        String bestRegulationType = null;//最优规则类型
        String meetRegulationId = null;//该商品满足的最优规则Id
        String activityName = null;
        List<Map<String, Object>> joinRegulationList = new ArrayList<>();//该商品可参加哪些活动

        //查看商品参加了哪些活动
        List<ActivityGoods> AGList = activitygoodsService.findByGoodsIdAndActivityStatus(goodsId, ActivityStatus.current);
        if (AGList.size() == 0) {//该商品无活动
            Double privilegeAmmount = 0.0;
            Double memberAmmount = singleGoodsTotalPrice;
            if (goodsIsMember != null && goodsIsMember) {//是会员商品
                memberAmmount = DoubleUtils.mul(singleGoodsTotalPrice, userDiscount);//会员价
                privilegeAmmount = DoubleUtils.sub(singleGoodsTotalPrice, memberAmmount);//原价与会员价的差价
                memberPrivilegeAmmount = DoubleUtils.add(privilegeAmmount, memberPrivilegeAmmount);
            }
            CartGoogsInfo cartGoogsInfo = new CartGoogsInfo(goodsId, goodsCode, goodsName, goodsImg,
                    Integer.valueOf(goodsCount), isKeep, isChoose, goodsPrice,
                    nowPrice, singleGoodsTotalPrice, privilegeAmmount, null,
                    null, null, null,
                    goodsCategoryId, goodsBrandId, skuInfo, saleModel, goodsType, singleGoodsTotalPrice, goStatus, virtualType,memberAmmount);
            cartGoogsInfo.setSubTradeTotal(cartGoogsInfo.getSubTotal());
            everyGoodsList.add(cartGoogsInfo);
//            continue;
            return;
        }
        //商品有活动
        List<Integer> activityIdList = new ArrayList<>();//活动ID集合
        String activityIds = "";
        for (ActivityGoods activityGoods : AGList) {
            Integer activityId = activityGoods.getActivityId();
            if (!activityIdList.contains(activityId)) {
                activityIdList.add(activityId);
                activityIds += activityId + ";";
            }
        }
        List<Activity> activityList = activityService.findByIdList(activityIdList);
        if (activityList.size() == 1 && activityList.get(0).getShare()) {//该商品只有一个活动，并且是共享活动
            Map<String, String> map = new HashMap<>();
            map.put("goodsId", goodsId);
            Object obj = runSingle(map);
            JSONArray joinOneActArray = null;
            Activity activity = null;
            IftttRegulation firstRegulation = null;
            if (obj != null) {
                joinOneActArray = (JSONArray) obj;
                JSONObject firstActivity = (JSONObject) joinOneActArray.get(0);
                activity = (Activity) firstActivity.get("activity");
                List<IftttRegulation> regulations = (List<IftttRegulation>) firstActivity.get("regulations");
                firstRegulation = regulations.get(0);
                regulationId = regulationId==null?firstRegulation.getId():regulationId;
            }
            JSONObject activityObj = new JSONObject();
            activityObj.put("name",activity.getName());
            activityObj.put("type",activity.getType());
            activityObj.put("id",activity.getId());
            activityObj.put("typeName",activity.getTypeAlias());
            activityObj.put("actionCode",activity.getActionCode());
            activityObj.put("isShare",activity.getShare());
            List<IftttRegulation> regulationList = regulationService.findByActivityId(activity.getId());
//            IftttRegulation firRegulation = regulationList.get(0);
            Double privilegeAmmount = 0.0;
            Double memberAmmount = singleGoodsTotalPrice;
            if (goodsIsMember != null && goodsIsMember&&activity.getMember()) {//是会员商品
                memberAmmount = DoubleUtils.mul(singleGoodsTotalPrice, userDiscount);//会员价
                privilegeAmmount = DoubleUtils.sub(singleGoodsTotalPrice, memberAmmount);//原价与会员价的差价
                memberPrivilegeAmmount = DoubleUtils.add(privilegeAmmount, memberPrivilegeAmmount);
            }
            CartGoogsInfo assignCartGoogsInfo = new CartGoogsInfo(goodsId, goodsCode, goodsName, goodsImg,
                    Integer.valueOf(goodsCount), isKeep, isChoose, goodsPrice,
                    nowPrice, singleGoodsTotalPrice, privilegeAmmount, activityObj
                    , joinOneActArray, activity.getId(), regulationId,
                    goodsCategoryId, goodsBrandId, skuInfo, saleModel, goodsType, null, goStatus, virtualType,memberAmmount);
            if (activity.getActionCode().indexOf("tradeInA") >= 0 && childGoodsId != null) {
                setChildGoodsInfo(assignCartGoogsInfo, childGoodsId);
            }
            isJoinSubMoneyActivity(assignCartGoogsInfo);
            everyGoodsList.add(assignCartGoogsInfo);
//            continue;
            return;
        }

        if (!isChoose) {//该商品未勾选
            //判断未勾选的商品是否选择了某个活动
            if (regulationId == null) {//自动判断参加某个活动
                Map<String, String> map = new HashMap<>();
                map.put("goodsId", goodsId);
                Object obj = runSingle(map);
                JSONArray joinOneActArray = null;
                Activity activity = null;
                IftttRegulation firstRegulation = null;
                if (obj != null) {
                    joinOneActArray = (JSONArray) obj;
                    JSONObject firstActivity = (JSONObject) joinOneActArray.get(0);
                    activity = (Activity) firstActivity.get("activity");
                    cartActivityId = activity.getId();
                    activityName = activity.getName();
                    List<IftttRegulation> regulations = (List<IftttRegulation>) firstActivity.get("regulations");
                    firstRegulation = regulations.get(0);
                    regulationId = firstRegulation.getId();
                }
                JSONObject activityObj = new JSONObject();
                activityObj.put("name",activity.getName());
                activityObj.put("type",activity.getType());
                activityObj.put("id",activity.getId());
                activityObj.put("typeName",activity.getTypeAlias());
                activityObj.put("actionCode",activity.getActionCode());
                activityObj.put("isShare",activity.getShare());
                Double privilegeAmmount = 0.0;
                Double memberAmmount = singleGoodsTotalPrice;
                if (goodsIsMember != null && goodsIsMember&&activity.getMember()) {//是会员商品
                    memberAmmount = DoubleUtils.mul(singleGoodsTotalPrice, userDiscount);//会员价
                    privilegeAmmount = DoubleUtils.sub(singleGoodsTotalPrice, memberAmmount);//原价与会员价的差价
                    memberPrivilegeAmmount = DoubleUtils.add(privilegeAmmount, memberPrivilegeAmmount);
                }
                //判断是否参加了减少金额的活动
                CartGoogsInfo assignCartGoogsInfo = new CartGoogsInfo(goodsId, goodsCode, goodsName, goodsImg,
                        Integer.valueOf(goodsCount), isKeep, isChoose, goodsPrice,
                        nowPrice, singleGoodsTotalPrice, privilegeAmmount, activityObj,joinOneActArray, cartActivityId, regulationId,
                        goodsCategoryId, goodsBrandId, skuInfo, saleModel, goodsType, null, goStatus, virtualType,memberAmmount);
                if (activity.getActionCode().indexOf("tradeInA")>=0 && childGoodsId != null) {
                    setChildGoodsInfo(assignCartGoogsInfo, childGoodsId);
                }
                isJoinSubMoneyActivity(assignCartGoogsInfo);
                everyGoodsList.add(assignCartGoogsInfo);
            } else if (regulationId == 0) {//不参加优惠
                List<JSONObject> otherActivityList = new ArrayList<>();
                for (Activity activity : activityList) {
                    JSONObject otherActivityObj = new JSONObject();
                    otherActivityObj.put("activity", activity);
                    List<IftttRegulation> regulationList = regulationService.findByActivityId(activity.getId());
                    for (IftttRegulation regulation : regulationList) {

                        if (regulationId.equals(regulation.getId())) {
                            cartActivityId = activity.getId();
                        }
                    }
                    otherActivityObj.put("regulations", regulationList);
                    otherActivityList.add(otherActivityObj);
                }
                Double privilegeAmmount = 0.0;
                Double memberAmmount = singleGoodsTotalPrice;
                if (goodsIsMember != null && goodsIsMember) {//是会员商品
                    memberAmmount = DoubleUtils.mul(singleGoodsTotalPrice, userDiscount);//会员价
                    privilegeAmmount = DoubleUtils.sub(singleGoodsTotalPrice, memberAmmount);//原价与会员价的差价
                    memberPrivilegeAmmount = DoubleUtils.add(privilegeAmmount, memberPrivilegeAmmount);
                }
                CartGoogsInfo cartGoogsInfo = new CartGoogsInfo(goodsId, goodsCode, goodsName, goodsImg,
                        Integer.valueOf(goodsCount), isKeep, isChoose, goodsPrice,
                        nowPrice, singleGoodsTotalPrice, privilegeAmmount, null,JSON.parseArray(ValueUtil.getFromJson(ValueUtil.toJson(otherActivityList),
                        "data")), 0, 0,
                        goodsCategoryId, goodsBrandId, skuInfo, saleModel, goodsType, null, goStatus, virtualType,memberAmmount);
                isJoinSubMoneyActivity(cartGoogsInfo);
                everyGoodsList.add(cartGoogsInfo);
            } else {//指定参加某个活动
                Map<String, Object> assignMap = new HashMap<>();
                assignMap.put("goodsTotalPrice", singleGoodsTotalPrice);
                IftttRegulation assignRegulation = regulationService.findById(regulationId);
                Activity assignActivity = activityService.findByRegulationId(regulationId);
                //查看该商品还有有哪些活动
                Map<String, String> map = new HashMap<>();
                map.put("goodsId", goodsId);
                Object obj = runSingle(map);
                JSONArray joinOneActArray = null;
                if (obj != null) {
                    joinOneActArray = (JSONArray) obj;
                }
                JSONObject assignActivityObj = new JSONObject();
                assignActivityObj.put("name",assignActivity.getName());
                assignActivityObj.put("type",assignActivity.getType());
                assignActivityObj.put("id",assignActivity.getId());
                assignActivityObj.put("typeName",assignActivity.getTypeAlias());
                assignActivityObj.put("actionCode",assignActivity.getActionCode());
                assignActivityObj.put("isShare",assignActivity.getShare());
                Double privilegeAmmount = 0.0;
                Double memberAmmount = singleGoodsTotalPrice;
                if (goodsIsMember != null && goodsIsMember&&assignActivity.getMember()) {//是会员商品
                    memberAmmount = DoubleUtils.mul(singleGoodsTotalPrice, userDiscount);//会员价
                    privilegeAmmount = DoubleUtils.sub(singleGoodsTotalPrice, memberAmmount);//原价与会员价的差价
                    memberPrivilegeAmmount = DoubleUtils.add(privilegeAmmount, memberPrivilegeAmmount);
                }
                CartGoogsInfo assignCartGoogsInfo = new CartGoogsInfo(goodsId, goodsCode, goodsName, goodsImg,
                        Integer.valueOf(goodsCount), isKeep, isChoose, goodsPrice,
                        nowPrice, singleGoodsTotalPrice, privilegeAmmount, assignActivityObj,
                         joinOneActArray, assignActivity.getId(), assignRegulation.getId(),
                        goodsCategoryId, goodsBrandId, skuInfo, saleModel, goodsType, null, goStatus, virtualType,memberAmmount);
                if (assignActivity.getActionCode().indexOf("tradeInA")>0 && childGoodsId != null) {
                    setChildGoodsInfo(assignCartGoogsInfo, childGoodsId);
                }
                isJoinSubMoneyActivity(assignCartGoogsInfo);
                everyGoodsList.add(assignCartGoogsInfo);
            }
//            continue;
            return;
        } else {//该商品已勾选
            if (regulationId != null && regulationId.equals("0")) {//不参加活动的商品
                Double privilegeAmmount = 0.0;
                Double memberAmmount = singleGoodsTotalPrice;
                if (goodsIsMember != null && goodsIsMember) {//是会员商品
                    memberAmmount = DoubleUtils.mul(singleGoodsTotalPrice, userDiscount);//会员价
                    privilegeAmmount = DoubleUtils.sub(singleGoodsTotalPrice, memberAmmount);//原价与会员价的差价
                    memberPrivilegeAmmount = DoubleUtils.add(privilegeAmmount, memberPrivilegeAmmount);
                }
                //查看该商品有哪些活动
                Map<String, String> map = new HashMap<>();
                map.put("goodsId", goodsId);
                Object obj = runSingle(map);
                JSONArray noJoinActArray = null;
                if (obj != null) {
                    noJoinActArray = (JSONArray) obj;
                }
                CartGoogsInfo cartGoogsInfo = new CartGoogsInfo(goodsId, goodsCode, goodsName, goodsImg,
                        Integer.valueOf(goodsCount), isKeep, isChoose, goodsPrice,
                        nowPrice, singleGoodsTotalPrice, privilegeAmmount, null,
                        noJoinActArray, 0, 0,
                        goodsCategoryId, goodsBrandId, skuInfo, saleModel, goodsType, singleGoodsTotalPrice, goStatus, virtualType,memberAmmount);//若不参加活动，设定活动id及规则id为0
                everyGoodsList.add(cartGoogsInfo);
//                continue;
                return;
            } else if (regulationId != null && !regulationId.equals("0")) {
                //商品参加指定活动
                Map<String, Object> assignMap = new HashMap<>();
                assignMap.put("goodsTotalPrice", singleGoodsTotalPrice);
                IftttRegulation assignRegulation = regulationService.findById(regulationId);
                Activity assignActivity = activityService.findByRegulationId(regulationId);
                Double privilegeAmmount = 0.0;
                Double memberAmmount = singleGoodsTotalPrice;
                if (goodsIsMember != null && goodsIsMember&&assignActivity.getMember()) {//是会员商品
                    memberAmmount = DoubleUtils.mul(singleGoodsTotalPrice, userDiscount);//会员价
                    privilegeAmmount = DoubleUtils.sub(singleGoodsTotalPrice, memberAmmount);//原价与会员价的差价
                    memberPrivilegeAmmount = DoubleUtils.add(privilegeAmmount, memberPrivilegeAmmount);
                }
                //查看该商品还有有哪些活动
                Map<String, String> map = new HashMap<>();
                map.put("goodsId", goodsId);
                Object obj = runSingle(map);
                JSONArray joinOneActArray = null;
                if (obj != null) {
                    joinOneActArray = (JSONArray) obj;
                }
                JSONObject assignActivityObj = new JSONObject();
                assignActivityObj.put("name",assignActivity.getName());
                assignActivityObj.put("type",assignActivity.getType());
                assignActivityObj.put("id",assignActivity.getId());
                assignActivityObj.put("typeName",assignActivity.getTypeAlias());
                assignActivityObj.put("actionCode",assignActivity.getActionCode());
                assignActivityObj.put("isShare",assignActivity.getShare());
                CartGoogsInfo assignCartGoogsInfo = new CartGoogsInfo(goodsId, goodsCode, goodsName, goodsImg,
                        Integer.valueOf(goodsCount), isKeep, isChoose, goodsPrice,
                        nowPrice, singleGoodsTotalPrice, privilegeAmmount, assignActivityObj,
                        joinOneActArray, assignActivity.getId(), assignRegulation.getId(),
                        goodsCategoryId, goodsBrandId, skuInfo, saleModel, goodsType, null, goStatus, virtualType,memberAmmount);
                if (assignActivity.getActionCode().indexOf("tradeInA")>=0 && childGoodsId != null) {
                    setChildGoodsInfo(assignCartGoogsInfo, childGoodsId);
                }
                isJoinSubMoneyActivity(assignCartGoogsInfo);
                everyGoodsList.add(assignCartGoogsInfo);

                //用于计算随心购
                Map<String, Object> allGoodsInfo = new HashMap<>();
                allGoodsInfo.put("goodsId", 0);
                allGoodsInfo.put("goodsCount", Integer.valueOf(goodsCount));
                allGoodsInfo.put("originGoodsPrice", goodsPrice);
                allGoodsInfo.put("nowGoodsPrice", nowPrice);//现在的总价
                allGoodsInfoList.add(allGoodsInfo);
//                continue;
                return;
            }

            //根据活动优先级给商品匹配活动信息
            Map<String, String> map = new HashMap<>();
            map.put("goodsId", goodsId);
            Object obj = runSingle(map);
            JSONArray joinOneActArray = null;
            Activity activity = null;
            IftttRegulation firstRegulation = null;
            if (obj != null) {
                joinOneActArray = (JSONArray) obj;
                JSONObject firstActivity = (JSONObject) joinOneActArray.get(0);
                activity = (Activity) firstActivity.get("activity");
                cartActivityId = activity.getId();
                activityName = activity.getName();
                List<IftttRegulation> regulations = (List<IftttRegulation>) firstActivity.get("regulations");
                firstRegulation = regulations.get(0);
                regulationId = firstRegulation.getId();
            }
            JSONObject activityObj = new JSONObject();
            activityObj.put("name",activity.getName());
            activityObj.put("type",activity.getType());
            activityObj.put("id",activity.getId());
            activityObj.put("typeName",activity.getTypeAlias());
            activityObj.put("actionCode",activity.getActionCode());
            activityObj.put("isShare",activity.getShare());
            Double privilegeAmmount = 0.0;
            Double memberAmmount = singleGoodsTotalPrice;
            if (goodsIsMember != null && goodsIsMember&&activity.getMember()) {//是会员商品
                memberAmmount = DoubleUtils.mul(singleGoodsTotalPrice, userDiscount);//会员价
                privilegeAmmount = DoubleUtils.sub(singleGoodsTotalPrice, memberAmmount);//原价与会员价的差价
                memberPrivilegeAmmount = DoubleUtils.add(privilegeAmmount, memberPrivilegeAmmount);
            }
            CartGoogsInfo assignCartGoogsInfo = new CartGoogsInfo(goodsId, goodsCode, goodsName, goodsImg,
                    Integer.valueOf(goodsCount), isKeep, isChoose, goodsPrice,
                    nowPrice, singleGoodsTotalPrice, privilegeAmmount, activityObj,
                    joinOneActArray, cartActivityId, regulationId,
                    goodsCategoryId, goodsBrandId, skuInfo, saleModel, goodsType, null, goStatus, virtualType,memberAmmount);
            if (activity.getActionCode().indexOf("tradeInA")>=0 && childGoodsId != null) {
                setChildGoodsInfo(assignCartGoogsInfo, childGoodsId);
            }
            isJoinSubMoneyActivity(assignCartGoogsInfo);
            everyGoodsList.add(assignCartGoogsInfo);
        }
    }


    private void isJoinSubMoneyActivity(CartGoogsInfo cartGoogsInfo) throws YesmywineException {
        JSONObject activityObj = (JSONObject) cartGoogsInfo.getActivity();
        if(activityObj==null){
            cartGoogsInfo.setSubTradeTotal(cartGoogsInfo.getMemberAmmount());
            cartGoogsInfo.setActivityPrice(cartGoogsInfo.getMemberAmmount());
            cartGoogsInfo.setBalance(DoubleUtils.sub(cartGoogsInfo.getSubTotal(), cartGoogsInfo.getActivityPrice()));
            return;
        }
        String actionCode = activityObj.getString("actionCode");
        if (actionCode.indexOf("reductionA") >= 0 || actionCode.indexOf("discountA") >= 0 ||
                actionCode.indexOf("rushPurA") >= 0 || actionCode.indexOf("tradeInA") >= 0) {
            IftttRegulation firstRegulation = regulationService.findById(cartGoogsInfo.getRegulationId());
            if (activityObj.getBoolean("isShare")) {//如果是共享活动
                Set<IftttRegulation> children = firstRegulation.getChildren();
                for (IftttRegulation regulation : children) {
                    if (regulation.getActionCode().indexOf("reductionA") >= 0 || regulation.getActionCode().indexOf("discountA") >= 0 || regulation.getActionCode().indexOf("tradeInA") >= 0) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("goodsTotalPrice", cartGoogsInfo.getMemberAmmount());
                        map.put("goodsId", cartGoogsInfo.getGoodsId());
                        map.put("regulationId", cartGoogsInfo.getRegulationId());
                        map.put("triggerValue", regulation.getTriggerValue());
                        map.put("actionValue", regulation.getActionValue());
                        String response = ifTTT.iftttGoods(map, regulation, cartGoogsInfo.getRegulationId());
                        //如果是换购更改活动价（暂用）
                        if (actionCode.indexOf("tradeInA") >= 0 && map.get("regulationType").toString().equals("tradeInA") && (Boolean) map.get("isMeet").equals(true)) {
                            if(cartGoogsInfo.getChlidGoodsInfo()!=null){
                                cartGoogsInfo.getChlidGoodsInfo().setNowPrice(Double.valueOf(regulation.getActionValue()));
                            }
                            if(cartGoogsInfo.getActivityPrice()!=null&&cartGoogsInfo.getChlidGoodsInfo()!=null){
                                cartGoogsInfo.setActivityPrice(DoubleUtils.add(cartGoogsInfo.getActivityPrice(), Double.valueOf(regulation.getActionValue())));
                                cartGoogsInfo.setSubTradeTotal(DoubleUtils.add(cartGoogsInfo.getMemberAmmount(), Double.valueOf(map.get("actionValue").toString())));
                            }else if(cartGoogsInfo.getActivityPrice()==null&&cartGoogsInfo.getChlidGoodsInfo()!=null){
                                cartGoogsInfo.setActivityPrice(DoubleUtils.add(cartGoogsInfo.getMemberAmmount(), Double.valueOf(regulation.getActionValue())));
                                cartGoogsInfo.setSubTradeTotal(DoubleUtils.add(cartGoogsInfo.getMemberAmmount(), Double.valueOf(map.get("actionValue").toString())));
                                cartGoogsInfo.setBalance(0.0);
                            }else if(cartGoogsInfo.getActivityPrice()==null&&cartGoogsInfo.getChlidGoodsInfo()==null){
                                cartGoogsInfo.setActivityPrice(cartGoogsInfo.getMemberAmmount());
                                cartGoogsInfo.setSubTradeTotal(cartGoogsInfo.getMemberAmmount());
                                cartGoogsInfo.setBalance(0.0);
                            }
                        } else if ((map.get("regulationType").toString().equals("reductionA") || map.get("regulationType").toString().equals("discountA")) && (Boolean) map.get("isMeet").equals(true)) {
                            if(cartGoogsInfo.getActivityPrice()!=null){
                                Double actionValue = Double.valueOf(ValueUtil.getFromJson(response, "data", "actionValue"));
                                cartGoogsInfo.setActivityPrice(DoubleUtils.sub(cartGoogsInfo.getActivityPrice(),actionValue));
                                cartGoogsInfo.setSubTradeTotal(cartGoogsInfo.getMemberAmmount());
                            }else{
                                Double goodsActivityPrice = Double.valueOf(ValueUtil.getFromJson(response, "data", "action"));
                                cartGoogsInfo.setActivityPrice(goodsActivityPrice);
                                cartGoogsInfo.setSubTradeTotal(cartGoogsInfo.getMemberAmmount());
                            }
                            cartGoogsInfo.setBalance(DoubleUtils.sub(cartGoogsInfo.getMemberAmmount(), cartGoogsInfo.getActivityPrice()));
                        } else {
                            if(cartGoogsInfo.getActivityPrice()!=null){
                                cartGoogsInfo.setActivityPrice(cartGoogsInfo.getActivityPrice());
                                cartGoogsInfo.setSubTradeTotal(cartGoogsInfo.getMemberAmmount());
                            }else{
                                cartGoogsInfo.setActivityPrice(cartGoogsInfo.getMemberAmmount());
                                cartGoogsInfo.setSubTradeTotal(cartGoogsInfo.getMemberAmmount());
                                cartGoogsInfo.setBalance(DoubleUtils.sub(cartGoogsInfo.getMemberAmmount(), cartGoogsInfo.getActivityPrice()));
                            }
                        }
                    }
                }
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("goodsTotalPrice", cartGoogsInfo.getMemberAmmount());
                map.put("goodsId", cartGoogsInfo.getGoodsId());
                map.put("regulationId", cartGoogsInfo.getRegulationId());
                map.put("triggerValue", firstRegulation.getTriggerValue());
                map.put("actionValue", firstRegulation.getActionValue());
                String response = ifTTT.iftttGoods(map, firstRegulation, null);
                //如果是换购更改活动价（暂用）
                if (actionCode.indexOf("tradeInA") >= 0 && map.get("regulationType").toString().equals("tradeInA") && (Boolean) map.get("isMeet").equals(true)) {
                    if(cartGoogsInfo.getChlidGoodsInfo()!=null){
                        cartGoogsInfo.getChlidGoodsInfo().setNowPrice(Double.valueOf(firstRegulation.getActionValue()));//换购商品的换购价
                        cartGoogsInfo.setActivityPrice(DoubleUtils.add(cartGoogsInfo.getMemberAmmount(), Double.valueOf(map.get("actionValue").toString())));
                        cartGoogsInfo.setSubTradeTotal(DoubleUtils.add(cartGoogsInfo.getMemberAmmount(), Double.valueOf(map.get("actionValue").toString())));
                        cartGoogsInfo.setBalance(0.0);
                    }else{
                        cartGoogsInfo.setActivityPrice(cartGoogsInfo.getMemberAmmount());
                        cartGoogsInfo.setSubTradeTotal(cartGoogsInfo.getMemberAmmount());
                        cartGoogsInfo.setBalance(0.0);
                    }
                }else if ((map.get("regulationType").toString().equals("reductionA") || map.get("regulationType").toString().equals("discountA")) && (Boolean) map.get("isMeet").equals(true)) {
                    Double goodsActivityPrice = Double.valueOf(ValueUtil.getFromJson(response, "data", "action"));
                    cartGoogsInfo.setActivityPrice(goodsActivityPrice);
                    cartGoogsInfo.setSubTradeTotal(cartGoogsInfo.getMemberAmmount());
                    cartGoogsInfo.setBalance(DoubleUtils.sub(cartGoogsInfo.getMemberAmmount(), cartGoogsInfo.getActivityPrice()));
                } else {
                    cartGoogsInfo.setActivityPrice(cartGoogsInfo.getMemberAmmount());
                    cartGoogsInfo.setSubTradeTotal(cartGoogsInfo.getMemberAmmount());
                    cartGoogsInfo.setBalance(DoubleUtils.sub(cartGoogsInfo.getMemberAmmount(), cartGoogsInfo.getActivityPrice()));
                }
            }
        } else {
            cartGoogsInfo.setSubTradeTotal(cartGoogsInfo.getMemberAmmount());
            cartGoogsInfo.setActivityPrice(cartGoogsInfo.getMemberAmmount());
            cartGoogsInfo.setBalance(DoubleUtils.sub(cartGoogsInfo.getMemberAmmount(), cartGoogsInfo.getActivityPrice()));
        }
    }


//    for (int k = 0; k < everyGoodsList.size(); k++) {
//        CartGoogsInfo cartGoogsInfo = everyGoodsList.get(k);
//        if (everyGoodsList.size() == 1) {//购物车中只有一个商品
//            changeArray = new JSONArray();
//            changeArray.add(cartGoogsInfo);
//
//            //匹配其他活动信息
//            addGoodsActivityInfo(changeArray, username);
//            JSONObject subTotalSub = new JSONObject();
////                allGoogsTotalPrice = DoubleUtils.add(allGoogsTotalPrice,cartGoogsInfo.getSubTotal());
//            originSubtotal = DoubleUtils.add(originSubtotal, cartGoogsInfo.getSubTotal());
//            subTotalSub.put("activityId", cartGoogsInfo.getActivityId());
//            subTotalSub.put("activityName", cartGoogsInfo.getActivity().getName());
//            subTotalSub.put("originSubtotal", originSubtotal);
//            subTotalSub.put("goodsesInfo", changeArray);
//            String actionCode = cartGoogsInfo.getActivity().getActionCode();
//            if (actionCode.indexOf("reductionA") >= 0 || actionCode.indexOf("discountA") >= 0 || actionCode.indexOf("rushPurA") >= 0) {
//                if (actionCode.split("_").length > 1) {//如果是共享活动
//                    for (int a = 0; a < actionCode.split("_").length; a++) {
//                        if (actionCode.indexOf("reductionA") >= 0 || actionCode.indexOf("discountA") >= 0 || actionCode.indexOf("rushPurA") >= 0) {
//                            if (actionCode.split("_").length > 1) {//如果是共享活动
//                                Set<IftttRegulation> children = cartGoogsInfo.getRegulation().getChildren();
//                                int b = 0;
//                                for (IftttRegulation regulation : children) {
//                                    if (regulation.getActionCode().indexOf("reductionA") >= 0 || regulation.getActionCode().indexOf("discountA") >= 0 || regulation.getActionCode().indexOf("rushPurA") >= 0) {
//                                        Map<String, Object> map = new HashMap<>();
//                                        map.put("goodsTotalPrice", originSubtotal);
//                                        map.put("goodsId", cartGoogsInfo.getGoodsId());
//                                        map.put("regulationId", cartGoogsInfo.getRegulation().getId());
//                                        map.put("triggerValue", regulation.getTriggerValue());
//                                        map.put("actionValue", regulation.getActionValue());
//                                        String response = ifTTT.iftttGoods(map, regulation, cartGoogsInfo.getRegulation().getId());
//                                        subTotalSub.put("activitySubtotal", ValueUtil.getFromJson(response, "data", "action"));
//                                        subTotalSub.put("balance", DoubleUtils.sub(subTotalSub.getDouble("originSubtotal"), subTotalSub.getDouble("activitySubtotal")));
//                                        everyGoodsJsonArray.add(subTotalSub);
//                                        activityTotalPrice = Double.valueOf(subTotalSub.getString("activitySubtotal"));
//                                        if (b == 0) {
//                                            JSONObject couponObj = new JSONObject();
//                                            couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
//                                            couponObj.put("price", cartGoogsInfo.getSubTotal());
//                                            couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
//                                            couponObj.put("brandId", cartGoogsInfo.getBrandId());
//                                            couponArray.add(couponObj);
//                                            JSONObject obj = new JSONObject();
//                                            obj.put("activitySubTotal", null);
//                                            obj.put("goods", couponArray);
//                                            allCouponArray.add(obj);
//                                        }
//                                    } else {
//                                        if (b == 0) {
//                                            subTotalSub.put("activitySubtotal", originSubtotal);
//                                            JSONObject couponObj = new JSONObject();
//                                            couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
//                                            couponObj.put("price", cartGoogsInfo.getSubTotal());
//                                            couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
//                                            couponObj.put("brandId", cartGoogsInfo.getBrandId());
//                                            couponArray.add(couponObj);
//                                            JSONObject obj = new JSONObject();
//                                            obj.put("activitySubTotal", null);
//                                            obj.put("goods", couponArray);
//                                            allCouponArray.add(obj);
//                                        }
//                                    }
//                                    b++;
//                                }
//                                break;
//                            }
//                        }
//                        subTotalSub.put("balance", DoubleUtils.sub(subTotalSub.getDouble("originSubtotal"), subTotalSub.getDouble("activitySubtotal")));
//                        everyGoodsJsonArray.add(subTotalSub);
//                        activityTotalPrice = DoubleUtils.add(activityTotalPrice, Double.valueOf(subTotalSub.getString("activitySubtotal")));
////                            everyGoodsJsonArray.add(changeArray);
//
//                    }
//                    break;
//
//                } else {
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("goodsTotalPrice", originSubtotal);
//                    map.put("goodsId", cartGoogsInfo.getGoodsId());
//                    map.put("regulationId", cartGoogsInfo.getRegulation().getId());
//                    map.put("triggerValue", cartGoogsInfo.getRegulation().getTriggerValue());
//                    map.put("actionValue", cartGoogsInfo.getRegulation().getActionValue());
//                    String response = ifTTT.iftttGoods(map, cartGoogsInfo.getRegulation(), cartGoogsInfo.getRegulation().getId());
//                    subTotalSub.put("activitySubtotal", ValueUtil.getFromJson(response, "data", "action"));
//                }
//            } else {
//                subTotalSub.put("activitySubtotal", originSubtotal);
//            }
//            subTotalSub.put("balance", DoubleUtils.sub(subTotalSub.getDouble("originSubtotal"), subTotalSub.getDouble("activitySubtotal")));
//            everyGoodsJsonArray.add(subTotalSub);
//            activityTotalPrice = DoubleUtils.add(activityTotalPrice, Double.valueOf(subTotalSub.getString("activitySubtotal")));
////                everyGoodsJsonArray.add(changeArray);
//            JSONObject couponObj = new JSONObject();
//            couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
//            couponObj.put("price", cartGoogsInfo.getSubTotal());
//            couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
//            couponObj.put("brandId", cartGoogsInfo.getBrandId());
//            couponArray.add(couponObj);
//            JSONObject obj = new JSONObject();
//            obj.put("activitySubTotal", null);
//            obj.put("goods", couponArray);
//            allCouponArray.add(obj);
//            break;
//        }
//
//        //购物车中有多个商品
//        String actionCode = cartGoogsInfo.getActivity().getActionCode();
//        Integer regulationId = cartGoogsInfo.getRegulationId();
//        originSubtotal = DoubleUtils.add(originSubtotal, cartGoogsInfo.getSubTotal());
//        CartGoogsInfo childGoods = cartGoogsInfo.getChlidGoodsInfo();
//        if (childGoods != null) {
//            originSubtotal = DoubleUtils.add(originSubtotal, childGoods.getSubTotal());
//        }
//        if (!regulationId.equals(oldRegulationId)&&k!=0) {
//            if (changeArray != null) {
//                JSONObject subTotalSub = new JSONObject();
////                    if (changeArray.size() == 1) {//相同规则的商品只有一个
//                //上一个活动维度的商品
//                CartGoogsInfo beforeOne = (CartGoogsInfo) changeArray.get(changeArray.size() - 1);
//                String beforeOneActionCode = beforeOne.getActivity().getActionCode();
//                subTotalSub.put("activityId", beforeOne.getActivityId());
//                subTotalSub.put("activityName", beforeOne.getActivity().getName());
//                subTotalSub.put("originSubtotal", DoubleUtils.sub(originSubtotal,cartGoogsInfo.getSubTotal()));
//                subTotalSub.put("goodsesInfo", changeArray);
//
//                addGoodsActivityInfo(changeArray, username);
//
//                if (beforeOneActionCode.indexOf("reductionA") >= 0 || beforeOneActionCode.indexOf("discountA") >= 0 || beforeOneActionCode.indexOf("rushPurA") >= 0) {
//                    if (beforeOneActionCode.split("_").length > 1) {//如果是共享活动
//                        Set<IftttRegulation> children = beforeOne.getRegulation().getChildren();
//                        int b = 0;
//                        for (IftttRegulation regulation : children) {
//                            if (regulation.getActionCode().indexOf("reductionA") >= 0 || regulation.getActionCode().indexOf("discountA") >= 0 || regulation.getActionCode().indexOf("rushPurA") >= 0) {
//                                Map<String, Object> map = new HashMap<>();
//                                map.put("goodsTotalPrice", beforeOne.getSubTotal());
//                                map.put("goodsId", beforeOne.getGoodsId());
//                                map.put("regulationId", beforeOne.getRegulation().getId());
//                                map.put("triggerValue", regulation.getTriggerValue());
//                                map.put("actionValue", regulation.getActionValue());
//                                String response = ifTTT.iftttGoods(map, regulation, beforeOne.getRegulation().getId());
//                                subTotalSub.put("activitySubtotal", ValueUtil.getFromJson(response, "data", "action"));
//                                subTotalSub.put("balance", DoubleUtils.sub(subTotalSub.getDouble("originSubtotal"), subTotalSub.getDouble("activitySubtotal")));
//                                everyGoodsJsonArray.add(subTotalSub);
//                                activityTotalPrice = Double.valueOf(subTotalSub.getString("activitySubtotal"));
//                                if (b == 0) {
//                                    JSONObject couponObj = new JSONObject();
//                                    couponObj.put("goodsId", beforeOne.getGoodsId());
//                                    couponObj.put("price", beforeOne.getSubTotal());
//                                    couponObj.put("categoryId", beforeOne.getCategoryId());
//                                    couponObj.put("brandId", beforeOne.getBrandId());
//                                    couponArray.add(couponObj);
//                                    JSONObject obj = new JSONObject();
//                                    obj.put("activitySubTotal", null);
//                                    obj.put("goods", couponArray);
//                                    allCouponArray.add(obj);
//                                }
//                            } else {
//                                if (b == 0) {
//                                    subTotalSub.put("activitySubtotal", originSubtotal);
//                                    JSONObject couponObj = new JSONObject();
//                                    couponObj.put("goodsId", beforeOne.getGoodsId());
//                                    couponObj.put("price", beforeOne.getSubTotal());
//                                    couponObj.put("categoryId", beforeOne.getCategoryId());
//                                    couponObj.put("brandId", beforeOne.getBrandId());
//                                    couponArray.add(couponObj);
//                                    JSONObject obj = new JSONObject();
//                                    obj.put("activitySubTotal", null);
//                                    obj.put("goods", couponArray);
//                                    allCouponArray.add(obj);
//                                }
//                            }
//                            b++;
//                        }
//                        break;
//                    } else {
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("goodsTotalPrice", beforeOne.getSubTotal());
//                        map.put("goodsId", beforeOne.getGoodsId());
//                        map.put("username", username);
//                        map.put("regulationId", beforeOne.getRegulation().getId());
//                        map.put("triggerValue", beforeOne.getRegulation().getTriggerValue());
//                        map.put("actionValue", beforeOne.getRegulation().getActionValue());
//                        String response = ifTTT.iftttGoods(map, beforeOne.getRegulation(), beforeOne.getRegulation().getId());
//                        subTotalSub.put("activitySubtotal", ValueUtil.getFromJson(response, "data", "action"));
//                    }
//
//                } else {
//                    subTotalSub.put("activitySubtotal", originSubtotal);
//                }
//                subTotalSub.put("balance", DoubleUtils.sub(subTotalSub.getDouble("originSubtotal"), subTotalSub.getDouble("activitySubtotal")));
//                everyGoodsJsonArray.add(subTotalSub);
//                activityTotalPrice = DoubleUtils.add(activityTotalPrice, Double.valueOf(subTotalSub.getString("activitySubtotal")));
//                //优惠券用的全部商品信息
//                JSONObject couponSubTotal = new JSONObject();
//                couponSubTotal.put("activitySubTotal", activityTotalPrice);
//                couponSubTotal.put("goods", couponArray);
//                allCouponArray.add(couponSubTotal);
//
//                changeArray = new JSONArray();
//                //下一个活动维度的商品
//                changeArray.add(cartGoogsInfo);
//
////                    } else {//一个规则多个商品
////
////                    }
//                if (k == everyGoodsList.size() - 1) {
//                    subTotalSub = new JSONObject();
//                    subTotalSub.put("activityId", cartGoogsInfo.getActivityId());
//                    subTotalSub.put("activityName", cartGoogsInfo.getActivity().getName());
//                    subTotalSub.put("originSubtotal", cartGoogsInfo.getSubTotal());
//                    subTotalSub.put("goodsesInfo", changeArray);
//                    if (actionCode.indexOf("reductionA") >= 0 || actionCode.indexOf("discountA") >= 0 || actionCode.indexOf("rushPurA") >= 0) {
//                        if (actionCode.split("_").length > 1) {//如果是共享活动
//                            Set<IftttRegulation> children = cartGoogsInfo.getRegulation().getChildren();
//                            int b = 0;
//                            for (IftttRegulation regulation : children) {
//                                if (regulation.getActionCode().indexOf("reductionA") >= 0 || regulation.getActionCode().indexOf("discountA") >= 0 || regulation.getActionCode().indexOf("rushPurA") >= 0) {
//                                    Map<String, Object> map = new HashMap<>();
//                                    map.put("goodsTotalPrice", cartGoogsInfo.getSubTotal());
//                                    map.put("goodsId", cartGoogsInfo.getGoodsId());
//                                    map.put("regulationId", cartGoogsInfo.getRegulation().getId());
//                                    map.put("triggerValue", regulation.getTriggerValue());
//                                    map.put("actionValue", regulation.getActionValue());
//                                    String response = ifTTT.iftttGoods(map, regulation, cartGoogsInfo.getRegulation().getId());
//                                    subTotalSub.put("activitySubtotal", ValueUtil.getFromJson(response, "data", "action"));
//                                    subTotalSub.put("balance", DoubleUtils.sub(subTotalSub.getDouble("originSubtotal"), subTotalSub.getDouble("activitySubtotal")));
//                                    everyGoodsJsonArray.add(subTotalSub);
//                                    activityTotalPrice = DoubleUtils.add(activityTotalPrice, Double.valueOf(subTotalSub.getString("activitySubtotal")));
//                                    if (b == 0) {
//                                        JSONObject couponObj = new JSONObject();
//                                        couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
//                                        couponObj.put("price", cartGoogsInfo.getSubTotal());
//                                        couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
//                                        couponObj.put("brandId", cartGoogsInfo.getBrandId());
//                                        couponArray.add(couponObj);
//                                        JSONObject obj = new JSONObject();
//                                        obj.put("activitySubTotal", null);
//                                        obj.put("goods", couponArray);
//                                        allCouponArray.add(obj);
//                                    }
//                                } else {
//                                    if (b == 0) {
//                                        subTotalSub.put("activitySubtotal", originSubtotal);
//                                        JSONObject couponObj = new JSONObject();
//                                        couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
//                                        couponObj.put("price", cartGoogsInfo.getSubTotal());
//                                        couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
//                                        couponObj.put("brandId", cartGoogsInfo.getBrandId());
//                                        couponArray.add(couponObj);
//                                        JSONObject obj = new JSONObject();
//                                        obj.put("activitySubTotal", null);
//                                        obj.put("goods", couponArray);
//                                        allCouponArray.add(obj);
//                                    }
//                                }
//                                b++;
//                            }
//                            break;
//                        } else {
//                            Map<String, Object> map = new HashMap<>();
//                            map.put("goodsTotalPrice", cartGoogsInfo.getSubTotal());
//                            map.put("goodsId", cartGoogsInfo.getGoodsId());
//                            map.put("username", username);
//                            map.put("regulationId", cartGoogsInfo.getRegulation().getId());
//                            map.put("triggerValue", cartGoogsInfo.getRegulation().getTriggerValue());
//                            map.put("actionValue", cartGoogsInfo.getRegulation().getActionValue());
//                            String response = ifTTT.iftttGoods(map, cartGoogsInfo.getRegulation(), cartGoogsInfo.getRegulation().getId());
//                            subTotalSub.put("activitySubtotal", ValueUtil.getFromJson(response, "data", "action"));
//                        }
//                    } else {
//                        subTotalSub.put("activitySubtotal", originSubtotal);
//                    }
//                    subTotalSub.put("balance", DoubleUtils.sub(subTotalSub.getDouble("originSubtotal"), subTotalSub.getDouble("activitySubtotal")));
//                    everyGoodsJsonArray.add(subTotalSub);
//                    activityTotalPrice = DoubleUtils.add(activityTotalPrice, Double.valueOf(subTotalSub.getString("activitySubtotal")));
//                    //优惠券用的全部商品信息
//                    JSONObject couponSubTotal_next = new JSONObject();
//                    couponSubTotal_next.put("activitySubTotal", activityTotalPrice);
//                    couponSubTotal_next.put("goods", couponArray);
//                    allCouponArray.add(couponSubTotal_next);
//                }else{
//                    changeArray = new JSONArray();
//                    changeArray.add(cartGoogsInfo);
//                    //优惠券用的商品信息
//                    JSONObject couponObj = new JSONObject();
//                    couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
//                    couponObj.put("price", cartGoogsInfo.getSubTotal());
//                    couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
//                    couponObj.put("brandId", cartGoogsInfo.getBrandId());
//                    couponArray.add(couponObj);
//                }
//
//            }
//
//        } else {
//            if (changeArray == null) {
//                changeArray = new JSONArray();
//            }
//            changeArray.add(cartGoogsInfo);
//            //优惠券用的商品信息
//            JSONObject couponObj = new JSONObject();
//            couponObj.put("goodsId", cartGoogsInfo.getGoodsId());
//            couponObjL.put("price", cartGoogsInfo.getSubTotal());
//            couponObj.put("categoryId", cartGoogsInfo.getCategoryId());
//            couponObj.put("brandId", cartGoogsInfo.getBrandId());
//            couponArray.add(couponObj);
//        }
//        oldRegulationId = regulationId;
//    }

    //追加商品其余活动信息
    private void addGoodsActivityInfo(JSONArray changeArray, String username, JSONArray allCoupons) throws YesmywineException {

        for (int i = 0; i < changeArray.size(); i++) {
            CartGoogsInfo info = (CartGoogsInfo) changeArray.get(i);
            getIftttResult(info, username, allCoupons);
        }
    }

    //获取商品根据Ifttt规则返回的结果
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE, key = "'getIftttResult_'+#info+#username")
    private void getIftttResult(CartGoogsInfo info, String username, JSONArray allCoupons) throws YesmywineException {
        Double memberAmmount = info.getMemberAmmount();
        JSONArray otherActivitys = info.getOtherActivity();
        Iterator<Object> it = otherActivitys.iterator();
        IftttRegulation firRegulation = regulationService.findById(info.getRegulationId());
        while (it.hasNext()) {
            JSONObject oneActivity = (JSONObject) it.next();
            Activity activity = (Activity) oneActivity.get("activity");
            if (activity.getShare()) {
                List<Map<String, Object>> mapList = null;
                JSONObject shareRegulation = null;
                JSONArray shareArray = new JSONArray();
                for (IftttRegulation regulation : (List<IftttRegulation>) oneActivity.get("regulations")) {
                    shareRegulation = new JSONObject();
                    shareRegulation.put("regulationName", regulation.getName());
                    shareRegulation.put("regulationId", regulation.getId());

                    if (regulation.getId().equals(firRegulation.getId())) {
                        mapList = new ArrayList<>();
                        for (IftttRegulation child : regulation.getChildren()) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("goodsTotalPrice", memberAmmount);
                            map.put("username", username);
                            map.put("regulationName", child.getName());
                            map.put("triggerValue", child.getTriggerValue());
                            map.put("actionValue", child.getActionValue());
                            map.put("goodsId", info.getGoodsId());
                            map.put("regulationId", child.getId());
                            ifTTT.runCart(map, child, regulation.getId());
                            mapList.add(map);

                            if (child.getActionCode().equals("tradeInA") && !(Boolean) map.get("isMeet")) {
                                info.setChlidGoodsInfo(null);
                            }
                        }

//                        shareRegulation.put("children", mapList);
                        info.setRegulationInfo(mapList);
                        shareRegulation.put("isChoose", true);
                    } else {
                        shareRegulation.put("isChoose", false);
                    }
                    shareArray.add(shareRegulation);
                }

                oneActivity.clear();
//                oneActivity.put("activity", activity);
                oneActivity.put("regulations", shareArray);
            } else {
                List<IftttRegulation> regulationList = (List<IftttRegulation>) oneActivity.get("regulations");
                List<Map<String, Object>> mapList = new ArrayList<>();
                for (IftttRegulation regulation : regulationList) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("regulationName", regulation.getName());
                    map.put("regulationId", regulation.getId());


                    if (regulation.getId().equals(firRegulation.getId())) {
                        map.put("goodsTotalPrice", memberAmmount);
                        map.put("username", username);
                        map.put("triggerValue", regulation.getTriggerValue());
                        map.put("actionValue", regulation.getActionValue());
                        map.put("goodsId", info.getGoodsId());
                        ifTTT.runCart(map, regulation, null);
                        if (!map.get("regulationType").toString().equals("couponsA")) {
                            mapList.add(map);
                        }

                        if (regulation.getActionCode().equals("tradeInA") && !(Boolean) map.get("isMeet")) {
                            info.setChlidGoodsInfo(null);
                        }
                        info.setRegulationInfo(map);
                        map.put("isChoose", true);
                    } else {
                        map.put("isChoose", false);
                        if (!regulation.getActionCode().equals("couponsA")) {
                            mapList.add(map);
                        }
                    }
                    if (regulation.getActionCode().equals("couponsA")) {
                        map.put("goodsTotalPrice", memberAmmount);
                        map.put("username", username);
                        map.put("triggerValue", regulation.getTriggerValue());
                        map.put("actionValue", regulation.getActionValue());
                        map.put("goodsId", info.getGoodsId());
                        ifTTT.runCart(map, regulation, null);
                        if (!map.get("regulationType").toString().equals("couponsA")) {
                            mapList.add(map);
                        }
                        String coupons = (String) map.get("action");
                        if (coupons != null && !coupons.equals("")&&(Boolean) map.get("isMeet")) {
                            JSONArray couponsJsonArr = JSON.parseArray(coupons);
                            for (int m = 0; m < couponsJsonArr.size(); m++) {
                                JSONObject coupon = (JSONObject) couponsJsonArr.get(m);
                                if (!allCoupons.contains(coupon)) {
                                    allCoupons.add(coupon);
                                }
                            }
                        }
                    }
                }


                oneActivity.clear();
                if (!activity.getActionCode().equals("couponsA") && !activity.getActionCode().equals("rushPurA")) {
//                    oneActivity.put("activity", activity);
                    oneActivity.put("regulations", mapList);
                } else {
                    it.remove();
                }
            }
        }
//            for (int j = 0; j < otherActivitys.size(); j++) {
//                JSONObject oneActivity = (JSONObject) otherActivitys.get(j);
//                Activity activity = (Activity) oneActivity.get("activity");
//
//            }
    }

    private void setChildGoodsInfo(CartGoogsInfo assignCartGoogsInfo, String childGoodsId) {
        GoodsMirroring goods = goodsService.findById(childGoodsId);
        CartGoogsInfo childGoods = new CartGoogsInfo(goods.getGoodsId(), goods.getGoodsCode(), goods.getGoodsName(),
                goods.getGoodsImageUrl(), 1, goods.getKeep(), null,
                Double.valueOf(goods.getSalePrice()), null,
                null, null,
                null, null,
                assignCartGoogsInfo.getActivityId(), assignCartGoogsInfo.getRegulationId(), null,
                null, goods.getSkuInfo(), goods.getSaleModel(), goods.getGoodsType(), null, goods.getGoStatus(), goods.getVirtualType(),Double.valueOf(goods.getSalePrice())
        );
        assignCartGoogsInfo.setChlidGoodsInfo(childGoods);
    }


    private void goodsTypeEnumTotalPrice(List<RegulationGoods> allRegulations, List<Integer> regulationIdList, List<Integer> targetIdList, Double singleGoodsTotalPrice, List<Map<GoodsTypeEnum, Double>> typeEnumList) {
        for (RegulationGoods dg : allRegulations) {
            Integer targetId = dg.getTargetId();
            GoodsTypeEnum goodsTypeEnum = dg.getType();
            Integer regulation_Id = dg.getRegulationId();
            if (!regulationIdList.contains(regulation_Id)) {
                regulationIdList.add(regulation_Id);
                targetIdList.add(targetId);
                Map<GoodsTypeEnum, Double> map = new HashMap<>();
                map.put(goodsTypeEnum, singleGoodsTotalPrice);
                typeEnumList.add(map);
            } else {
                Iterator<Map.Entry<GoodsTypeEnum, Double>> it = typeEnumList.get(regulationIdList.indexOf(regulation_Id)).entrySet().iterator();
                GoodsTypeEnum typeEnum = null;
                Double totalPrice = 0.0;
                while (it.hasNext()) {
                    Map.Entry<GoodsTypeEnum, Double> entry = it.next();
                    typeEnum = entry.getKey();
                    totalPrice = DoubleUtils.add(entry.getValue(), singleGoodsTotalPrice);
                    entry.setValue(totalPrice);
                }
            }
        }
    }


    private void doNotParticipateActivity(List<JSONObject> everyGoodsArray, List<Map<String, Object>> allGoodsInfoList, String goodsId, String goodsPrice, String goodsCount, Double singleGoodsTotalPrice) {
        JSONObject oneBestObj = new JSONObject();
        oneBestObj.put("goodsId", goodsId);//商品Id
        oneBestObj.put("originPrice", goodsPrice);//原价
        oneBestObj.put("nowPrice", goodsPrice);
        oneBestObj.put("activityType", null);
        oneBestObj.put("regulationId", null);
        oneBestObj.put("otherActivity", null);
        everyGoodsArray.add(oneBestObj);
        Map<String, Object> allGoodsInfo = new HashMap<>();
        allGoodsInfo.put("goodsId", 0);
        allGoodsInfo.put("goodsCount", Integer.valueOf(goodsCount));
        allGoodsInfo.put("originGoodsPrice", Double.parseDouble(goodsPrice));
        allGoodsInfo.put("nowGoodsPrice", Double.parseDouble(goodsPrice));
        allGoodsInfoList.add(allGoodsInfo);
        Map<String, Object> allGoodsInfo1 = new HashMap<>();
        allGoodsInfo1.put("goodsId", 0);
        allGoodsInfo1.put("goodsCount", Integer.valueOf(goodsCount));
        allGoodsInfo1.put("originGoodsPrice", Double.parseDouble(goodsPrice));
        allGoodsInfo1.put("nowGoodsPrice", Double.parseDouble(goodsPrice));
        allGoodsInfoList.add(allGoodsInfo1);
    }


    private JSONObject isCanJoinAsOneWish(JSONObject resultObj, List<Map<String, Object>> allGoodsInfoList, Double cheapestPrice) {
        List<Double> regulationTotalPirceList = new ArrayList<>();
        List<IftttRegulation> regulationList = regulationService.findByActionIdAndIsDeleteAndStatus(10, DeleteEnum.NOT_DELETE, ActivityStatus.current);
        for (IftttRegulation regulation : regulationList) {
            Integer regulationId = regulation.getId();
            String triggerValue = regulation.getTriggerValue();
            String actionValue = regulation.getActionValue();
            if (allGoodsInfoList.size() < Integer.valueOf(triggerValue)) {//购物车商品不满足随心购触发条件
                continue;
            }
            //参加该 regulationId 的商品有哪些
            List<RegulationGoods> regulationGoodsList = regulationGoodsService.findByRegulationIdAndTypeAndWare(regulationId, GoodsTypeEnum.Goods, WareEnum.Main);
            List<Map<String, Object>> newAllGoodsInfoList = new ArrayList<>();//购物车中在 regulationGoodsList 的商品有哪些

            int k = 0;//标识是否满足触发某个随心购活动的条件
            //判断购物车中的商品是否属于活动商品
            Iterator<Map<String, Object>> it = allGoodsInfoList.iterator();
            while (it.hasNext()) {
                Map<String, Object> goodsInfo = it.next();
                Integer goodsId = (Integer) goodsInfo.get("goodsId");
                Integer count = (Integer) goodsInfo.get("goodsCount");
                for (RegulationGoods regulationGoods : regulationGoodsList) {
                    Integer targetId = regulationGoods.getTargetId();
                    if (targetId.equals(goodsId)) {
                        newAllGoodsInfoList.add(goodsInfo);
                        it.remove();
                        k++;
                    }
                }
            }
            if (k < Integer.valueOf(triggerValue)) {//不满足触发条件
                continue;
            } else {
                Double regulationTotalPrice = 0.0;
                Double asOneWishPrice = Double.valueOf(actionValue);
                //按商品价格排序
                Collections.sort(newAllGoodsInfoList, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> mapA, Map<String, Object> mapB) {
                        Double goodsPriceA = (Double) mapA.get("originGoodsPrice");
                        Double goodsPriceB = (Double) mapB.get("originGoodsPrice");
                        return goodsPriceA.compareTo(goodsPriceB);
                    }
                });

                if (newAllGoodsInfoList.size() > 3) {
                    for (int i = 0; i < newAllGoodsInfoList.size() - 3; i++) {
                        allGoodsInfoList.add(newAllGoodsInfoList.get(i));
                    }
                } else {
                    for (int j = newAllGoodsInfoList.size() - 1; j > newAllGoodsInfoList.size() - 4; j--) {
                        Map<String, Object> newGoodsInfo = newAllGoodsInfoList.get(j);
                        Integer count = (Integer) newGoodsInfo.get("goodsCount");
                        Double originGoodsPirce = (Double) newGoodsInfo.get("originGoodsPrice");
//                        Double price = DoubleUtils.add(asOneWishPrice,originGoodsPirce*(count-1));
                        regulationTotalPrice = DoubleUtils.add(regulationTotalPrice, originGoodsPirce * (count - 1));
                    }
                }
                regulationTotalPrice = DoubleUtils.add(regulationTotalPrice, asOneWishPrice);
                for (int m = 0; m < allGoodsInfoList.size(); m++) {
                    Map<String, Object> oldGoodsInfo = allGoodsInfoList.get(m);
                    Integer count = (Integer) oldGoodsInfo.get("goodsCount");
                    Double originGoodsPirce = (Double) oldGoodsInfo.get("originGoodsPrice");
                    regulationTotalPrice = DoubleUtils.add(regulationTotalPrice, originGoodsPirce * count);
                }
                regulationTotalPirceList.add(regulationTotalPrice);
            }
        }
        if (regulationTotalPirceList.size() > 0) {
            Collections.sort(regulationTotalPirceList);
            Double newTotalPrice = regulationTotalPirceList.get(0);
            if (newTotalPrice < cheapestPrice) {
                resultObj.put("cheapestPrice", newTotalPrice);
            }
        }
        return resultObj;
    }


    @Override
    public List<Map<String, Object>> runOrder(Double totalPrice) throws YesmywineException {
        String response = ifTTT.iftttOrder(totalPrice);
        Gson gson = new GsonBuilder().serializeNulls().create();
        List<Map<String, Object>> list = gson.fromJson(ValueUtil.getFromJson(response, "data"), new TypeToken<List<Map<String, Object>>>() {
        }.getType());
        List<Map<String, Object>> subPriceList = new ArrayList<>();
        list.forEach(map -> {
            String type = map.get("type").toString();
            if (type.equals("subPrice")) {
                subPriceList.add(map);
            }
        });

        Collections.sort(subPriceList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> mapA, Map<String, Object> mapB) {
                Double actionA = Double.parseDouble(mapA.get("action").toString());
                Double actionB = Double.parseDouble(mapB.get("action").toString());
                return actionA.compareTo(actionB);
            }
        });

        Iterator<Map<String, Object>> it = list.iterator();

        while (it.hasNext()) {
            Map<String, Object> map = it.next();
            if (subPriceList.contains(map)) {
                it.remove();
            }
        }


        List<Map<String, Object>> sortList = new ArrayList<>();
        sortList.addAll(subPriceList);
        sortList.addAll(list);
        return sortList;
    }

    @Override
    public Object runSingle(Map<String, String> params) throws YesmywineException {
        String goodsId = params.get("goodsId");
        GoodsMirroring goods = goodsService.findById(goodsId);
        ValueUtil.verify(goodsId, "goodsId");
        //查看商品参加了哪些活动
        List<ActivityGoods> AGList = activitygoodsService.findByGoodsIdAndActivityStatus(goodsId, ActivityStatus.current);
        //商品有活动
        List<Integer> activityIdList = new ArrayList<>();//活动ID集合
        String activityIds = "";
        for (ActivityGoods activityGoods : AGList) {
            Integer activityId = activityGoods.getActivityId();
            if (!activityIdList.contains(activityId)) {
                activityIdList.add(activityId);
                activityIds += activityId + ";";
            }
        }
        if (activityIdList != null) {
            List<Activity> activityList = activityService.findByIdList(activityIdList);


//            if (activityList.size() > 1) {//被重新排序 导致报错  Disk Write of ActivityServiceImplfindByIdList_2,3,4 failed:
//                Collections.sort(activityList, new Comparator<Activity>() {//多个活动按活动优先级排序
//                    @Override
//                    public int compare(Activity o1, Activity o2) {
//                        return o2.getPriority().compareTo(o1.getPriority());
//                    }
//                });
//            }
            JSONArray activityArray = new JSONArray();
            for (Activity activity : activityList) {
                JSONObject activityObj = new JSONObject();
                activityObj.put("activity", activity);
                List<IftttRegulation> regulationList = regulationService.findByActivityId(activity.getId());
//                if (regulationList.size() > 1) {
//                    Collections.sort(regulationList, new Comparator<IftttRegulation>() {//多个规则按规则优先级排序
//                        @Override
//                        public int compare(IftttRegulation o1, IftttRegulation o2) {
//                            return o2.getPriority().compareTo(o1.getPriority());
//                        }
//                    });
//                }
                activityObj.put("regulations", regulationList);
                activityArray.add(activityObj);
            }
            return activityArray;
        }
        return null;
    }

    @Override
    public PageModel getActivityGoods(Integer activityId, Integer pageNo, Integer pageSize) {
        PageModel page = activitygoodsService.findByActivityId(activityId, pageNo, pageSize);
        return page;
    }

    @Override
    public Object getActivityInfo(Integer activityId, Integer pageNo, Integer pageSize) {
        JSONObject activityJson = new JSONObject();
        activityJson.put("goodsList", getActivityGoods(activityId, pageNo, pageSize));

        Activity activity = activityService.findById(activityId);

        activityJson.put("activityName", activity.getName());
        activityJson.put("activityId", activity.getId());
        activityJson.put("activityStatus", activity.getStatus());

        Date endTime = activity.getEndTime();
        Date startTime = new Date();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startTime);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endTime);
        Long endSecond = endCalendar.getTimeInMillis();
        Long startSecond = startCalendar.getTimeInMillis();
        Long balance = endSecond - startSecond;
        Integer s_day = 60 * 60 * 24 * 1000;
        Integer s_hour = 60 * 60 * 1000;
        Integer s_min = 60 * 1000;
        Long day = balance / s_day;//还剩多少天
        Long hour = (balance - day * s_day) / s_hour;//还剩多少小时
        Long min = (balance - day * s_day - hour * s_hour) / s_min;//还剩多少分钟
        Long sec = (balance - day * s_day - hour * s_hour - min * s_min) / 1000;//还剩多少秒

        JSONObject dataJson = new JSONObject();
        dataJson.put("day", day);
        dataJson.put("hour", hour);
        dataJson.put("min", min);
        dataJson.put("sec", sec);

        activityJson.put("date", dataJson);

        return activityJson;
    }
}
