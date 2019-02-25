package com.yesmywine.goods.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.IdUtil;
import com.yesmywine.goods.common.SynchronizeGiftCard;
import com.yesmywine.goods.dao.GiftCardDao;
import com.yesmywine.goods.dao.GiftCardHistoryDao;
import com.yesmywine.goods.entity.GiftCard;
import com.yesmywine.goods.entity.GiftCardHistory;
import com.yesmywine.goods.service.GiftCardService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.date.DateUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by SJQ on 2017/2/13.
 */
@Service
public class GiftCardServiceImpl extends BaseServiceImpl<GiftCard, Long> implements GiftCardService {
    @Autowired
    private GiftCardDao giftCardDao;
    @Autowired
    private GiftCardHistoryDao giftCardHistoryDao;

    public String synchronizeGiftCard(String jsonDatas) throws YesmywineException {//礼品卡审核后同步
        String jsonData = ValueUtil.getFromJson(jsonDatas, "data");

        JSONArray adjustArray = JSON.parseArray(jsonData);
        List<GiftCard> list = new ArrayList<>();
        for (int i = 0; i < adjustArray.size(); i++) {
            JSONObject adjustCommand = (JSONObject) adjustArray.get(i);
            GiftCard giftCard = new GiftCard();
            String cardName = adjustCommand.getString("cardName");
            giftCard.setCardName(cardName);//礼品卡名称
            Integer type = Integer.valueOf(adjustCommand.getString("type"));
            giftCard.setType(type);//礼品卡类型（0,电子/1,实体）
            String batchNumber = adjustCommand.getString("batchNumber");
            giftCard.setBatchNumber(batchNumber);//批次编号
            Long cardNumber = Long.valueOf(adjustCommand.getString("cardNumber"));
            giftCard.setCardNumber(cardNumber);//卡号
            String password = adjustCommand.getString("password");
            giftCard.setPassword(password);//密码
            Double amounts = Double.valueOf(adjustCommand.getString("amounts"));
            giftCard.setAmounts(amounts);//礼品卡面值
            Double remainingSum = Double.valueOf(adjustCommand.getString("remainingSum"));
            giftCard.setRemainingSum(remainingSum);//礼品卡余额

            String skuId=adjustCommand.getString("skuId");
            if(ValueUtil.notEmpity(skuId)){
                giftCard.setSkuId(Integer.valueOf(skuId));
            }
            String latestTime = adjustCommand.getString("latestTime");
            if (latestTime != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
                Date d2 = null;
                try {
                    d2 = sdf.parse(latestTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d2);
                calendar.add(Calendar.HOUR_OF_DAY, -12);
                giftCard.setLatestTime(calendar.getTime());
            }
            Integer inDate = Integer.valueOf(adjustCommand.getString("inDate"));
            giftCard.setInDate(inDate);//有效期（单位：天）
            Long giftCardRecordId = Long.valueOf(adjustCommand.getString("giftCardRecordId"));
            giftCard.setGiftCardRecordId(giftCardRecordId);//生成记录id
            String activityId1 = adjustCommand.getString("activityId");
            if (activityId1 != null) {
                giftCard.setActivityId(Integer.valueOf(activityId1));//活动id
            }
            Integer status = Integer.valueOf(adjustCommand.getString("status"));
            giftCard.setStatus(status);//激活状态（0待激活/1已激活）
            Integer boundStatus = Integer.valueOf(adjustCommand.getString("boundStatus"));
            giftCard.setBoundStatus(boundStatus);//绑定状态（0未绑定/1已绑定）
            String userId = adjustCommand.getString("userId");
            if (userId != null) {
                giftCard.setUserId(Integer.valueOf(userId));
            }
            giftCard.setIfBuy(adjustCommand.getInteger("ifBuy"));
            list.add(giftCard);
        }
        giftCardDao.save(list);
        return "success";
    }

//    public static void main(String[] args) {
//        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
//        Date d2 = null;
//        try {
//            d2 = sdf.parse("Jan 10, 2017 12:10:00 AM");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.HOUR_OF_DAY, 12);
//        Date d3 = new Date();
//        System.out.println("d2 ====== " + d2.getTime());
//        System.out.println("d3 ====== " + sdf.format(d3));
//    }


    public String addGiftCard(Map<String, String> param) throws YesmywineException {//商城新增礼品卡
        GiftCard giftCard = new GiftCard();
        giftCard.setCardName(param.get("cardName"));//礼品卡名称
        giftCard.setType(0);//礼品卡类型（0,电子/1,实体）
        String password = getData();
        giftCard.setPassword(password);//密码
        giftCard.setAmounts(Double.valueOf(param.get("amounts")));//礼品卡面值
        giftCard.setRemainingSum(Double.valueOf(param.get("amounts")));//礼品卡余额
        giftCard.setLatestTime(DateUtil.toDate(param.get("latestTime"), "yyyy-mm-dd"));
        Integer inDate = Integer.valueOf(param.get("inDate"));
        giftCard.setInDate(inDate);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, inDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(sdf.format(calendar.getTime()));
        giftCard.setActualMaturity(calendar.getTime());
        giftCard.setActivityId(Integer.valueOf(param.get("activityId")));//活动id
        Long cardNumber = IdUtil.genId("yyMMdd1{s}{s}{s}{r}{r}{s}{s}{r}{r}", Integer.valueOf(param.get("activityId")), 5);
        giftCard.setCardNumber(cardNumber);//卡号
        giftCard.setStatus(1);//0待激活/1已激活
        giftCard.setActivationTime(new Date());
        giftCard.setBoundStatus(1);//0未绑定/1绑定
        giftCard.setIfBuy(1);//是否购买(0否/1是)
        giftCard.setBoundTime(new Date());
        giftCard.setUserId(Integer.valueOf(param.get("userId")));
        giftCardDao.save(giftCard);

        //礼品卡新增后，商城将自动把礼品卡明细同步给PAAS。
        String result = SynchronizeGiftCard.create(ValueUtil.toJson(HttpStatus.SC_CREATED, giftCard));
        if (result == null || !result.equals("201")) {
            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
            ValueUtil.isError("同步失败");
        }
        return "success";
    }

    private String getData() {
        Random r = new Random();
        String str = "";
        for (int i = 0; i < 10; i++) { // 循环10次
            Integer x = r.nextInt(10); // 0-9的随机数
            str += x.toString(); // 拼成10位数 因为int类型只能存放200000000+的数据，所以只能用字符串拼接
        }
        return str;
    }
    public GiftCard updateLoad(Long id) throws YesmywineException {//加载显示礼品卡
        ValueUtil.verify(id, "idNull");
        GiftCard giftCard = giftCardDao.findOne(id);
        return giftCard;
    }
    public GiftCard loadCardNumber(Long cardNumber) throws YesmywineException {//查看礼品卡详情内部调用
        ValueUtil.verify(cardNumber, "idNull");
        GiftCard giftCard = giftCardDao.findByCardNumber(cardNumber);
        return giftCard;
    }

    public String boundGiftCard(String jsonDatas) throws YesmywineException {//pass礼品卡绑定后同步给商城接口
        Long cardNumber = Long.valueOf(ValueUtil.getFromJson(jsonDatas, "data", "cardNumber"));
        GiftCard giftCard = giftCardDao.findByCardNumber(cardNumber);
        Integer boundStatus = Integer.valueOf(ValueUtil.getFromJson(jsonDatas, "data", "boundStatus"));
        giftCard.setBoundStatus(boundStatus);//绑定状态（0未绑定/1已绑定）
        String boundTime = ValueUtil.getFromJson(jsonDatas, "data", "boundTime");
        if (boundTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
            Date d2 = null;
            try {
                d2 = sdf.parse(boundTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d2);
            giftCard.setBoundTime(d2);
        }
        String userId = ValueUtil.getFromJson(jsonDatas, "data", "userId");
        if (userId != null) {
            giftCard.setUserId(Integer.valueOf(userId));
        }
        giftCardDao.save(giftCard);
        return "success";
    }

    public String spendGiftCard(String jsonDatas) throws YesmywineException {//pass礼品卡消费后同步给商城接口
        Long cardNumber = Long.valueOf(ValueUtil.getFromJson(jsonDatas, "data", "cardNumber"));
        GiftCard giftCard = giftCardDao.findByCardNumber(cardNumber);
        Integer status = Integer.valueOf(ValueUtil.getFromJson(jsonDatas, "data", "status"));
        giftCard.setStatus(status);//激活状态（0待激活/1已激活）
        Double remainingSum = Double.valueOf(ValueUtil.getFromJson(jsonDatas, "data", "remainingSum"));
        giftCard.setRemainingSum(remainingSum);//礼品卡余额
        String activationTime = ValueUtil.getFromJson(jsonDatas, "data", "activationTime");
        if (giftCard.getActivationTime() == null) {
            if (activationTime != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
                Date d2 = null;
                try {
                    d2 = sdf.parse(activationTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d2);
                giftCard.setActivationTime(d2);
            }
        }
        String actualMaturity = ValueUtil.getFromJson(jsonDatas, "data", "actualMaturity");
        if (giftCard.getActualMaturity() == null) {
            if (actualMaturity != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
                Date d2 = null;
                try {
                    d2 = sdf.parse(actualMaturity);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d2);
                giftCard.setActualMaturity(d2);
            }
        }
        giftCardDao.save(giftCard);
        return "success";
    }


    public String giftCardHistory(String jsonDatas) throws YesmywineException {//pass礼品卡消费后记录同步给商城接口
        Long cardNumber = Long.valueOf(ValueUtil.getFromJson(jsonDatas, "data", "cardNumber"));
        Long orderNo = Long.valueOf(ValueUtil.getFromJson(jsonDatas, "data", "orderNo"));
        Double usedAmount = Double.valueOf(ValueUtil.getFromJson(jsonDatas, "data", "usedAmount"));
        String usedTime = ValueUtil.getFromJson(jsonDatas, "data", "usedTime");
        Integer channel = Integer.valueOf(ValueUtil.getFromJson(jsonDatas, "data", "channel"));
        Long giftCardId = Long.valueOf(ValueUtil.getFromJson(jsonDatas, "data", "giftCardId"));
        Integer type = Integer.valueOf(ValueUtil.getFromJson(jsonDatas, "data", "type"));

        GiftCardHistory giftCardHistory = new GiftCardHistory();
        giftCardHistory.setCardNumber(cardNumber);//卡号
        giftCardHistory.setOrderNo(orderNo);//订单编号
        giftCardHistory.setUsedAmount(usedAmount);//消费金额
        if (usedTime != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
            Date d2 = null;
            try {
                d2 = sdf.parse(usedTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d2);
            giftCardHistory.setUsedTime(d2);//消费时间
        }
        giftCardHistory.setChannel(channel);//消费渠道（0官网、1门店）
        giftCardHistory.setGiftCardId(giftCardId);//礼品卡id
        giftCardHistory.setType(type);//类型：0消费，1退还
        giftCardHistoryDao.save(giftCardHistory);
        return "success";

    }
    public String spendPassCard(String jsonData) throws YesmywineException {//礼品卡消费信息同步
       // 礼品卡在商城消费了，会把消费记录自动同步给PAAS。
        JSONArray adjustArray = JSON.parseArray(jsonData);
        for (int i = 0; i < adjustArray.size(); i++) {
            JSONObject adjustCommand = (JSONObject) adjustArray.get(i);
            Long cardNumber = Long.valueOf(adjustCommand.getString("cardNumber"));
            Long orderNo = Long.valueOf(adjustCommand.getString("orderNo"));
            Double usedAmount = Double.valueOf(adjustCommand.getString("usedAmount"));
            Integer channel = Integer.valueOf(adjustCommand.getString("channel"));

            GiftCard giftCard = giftCardDao.findByCardNumber(cardNumber);
            if (giftCard.getStatus() == 0) {
                giftCard.setStatus(1);
                giftCard.setActivationTime(new Date());
                Integer inDate = giftCard.getInDate();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, inDate);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                System.out.println(sdf.format(calendar.getTime()));
                giftCard.setActualMaturity(calendar.getTime());
            }if(giftCard.getBoundStatus()==0){
                giftCard.setBoundStatus(1);
                giftCard.setBoundTime(new Date());
                String userId = adjustCommand.getString("userId");
                String userName =adjustCommand.getString("userName");
                giftCard.setUserId(Integer.valueOf(userId));
                giftCard.setUserName(userName);
            }

            GiftCardHistory giftCardHistory = new GiftCardHistory();
            giftCardHistory.setCardNumber(cardNumber);//卡号
            giftCardHistory.setOrderNo(orderNo);//订单编号
            giftCardHistory.setUsedAmount(usedAmount);//消费金额
            giftCardHistory.setUsedTime(new Date());//消费时间
            giftCardHistory.setChannel(channel);//消费渠道（0官网、1门店）
            giftCardHistory.setGiftCardId(giftCard.getId());//礼品卡id
            giftCardHistory.setType(0);//类型：0消费
            Double remainingSum = giftCard.getRemainingSum();
            BigDecimal c1 = new BigDecimal(Double.toString(remainingSum));
            BigDecimal c2 = new BigDecimal(Double.toString(usedAmount));
            BigDecimal remainingSum1 = c1.subtract(c2);
            giftCard.setRemainingSum(remainingSum1.doubleValue());//剩余金额
            //商城或门店把礼品卡消费后，将通过此接口把消费信息同步给 PAAS，并自动同步给商城。
            String  result= SynchronizeGiftCard.spendGiftCard(ValueUtil.toJson(HttpStatus.SC_CREATED, giftCard));
            String  result1= SynchronizeGiftCard.synchronizeHistory(ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardHistory));

            if(result==null||!result.equals("201")||result1==null||!result1.equals("201")){
                TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                ValueUtil.isError("同步失败");
            }
            giftCardHistoryDao.save(giftCardHistory);
            giftCardDao.save(giftCard);
        }
        return "success";
    }

    public String bound(String jsonData) throws YesmywineException {//内部商城礼品卡绑定信息同步到pass
        JSONArray adjustArray = JSON.parseArray(jsonData);
        for (int i = 0; i < adjustArray.size(); i++) {
            JSONObject adjustCommand = (JSONObject) adjustArray.get(i);
            Integer userId = Integer.valueOf(adjustCommand.getString("userId"));
            String userName = adjustCommand.getString("userName");
            Long cardNumber = Long.valueOf(adjustCommand.getString("cardNumber"));
            String password = adjustCommand.getString("password");

            GiftCard giftCard = giftCardDao.findByCardNumberAndPassword(cardNumber, password);
            giftCard.setBoundTime(new Date());
            giftCard.setBoundStatus(1);//绑定状态 1已绑定
            giftCard.setUserId(Integer.valueOf(userId));
            giftCard.setUserName(userName);
            giftCardDao.save(giftCard);

            //商城或门店把礼品卡绑定到个人账户下后，将通过此接口把相关绑定信息同步给 PAAS，并自动同步给商城。
            String  result= SynchronizeGiftCard.boundGiftCard(ValueUtil.toJson(HttpStatus.SC_CREATED, giftCard));
            if(result==null||!result.equals("201")) {
                TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                ValueUtil.isError("同步失败");
            }
        }
        return "success";
    }
    public String mallbound(Long cardNumber, String password,String userInfo) throws YesmywineException {//商城礼品卡绑定
        GiftCard giftCard = giftCardDao.findByCardNumberAndIfBuy(cardNumber,1 );
        if(giftCard==null){
            ValueUtil.isError("没有此卡号"+cardNumber);
        }else if(!giftCard.getPassword().equals(password)){
            ValueUtil.isError("输入密码错误");
        }else if(giftCard.getBoundStatus()==1) {
                ValueUtil.isError("礼品卡已绑定过！");
        }
        String userId = ValueUtil.getFromJson(userInfo, "id");
        String username = ValueUtil.getFromJson(userInfo, "userName");
        giftCard.setBoundTime(new Date());
        giftCard.setBoundStatus(1);//绑定状态 1已绑定
        giftCard.setUserId(Integer.valueOf(userId));
        giftCard.setUserName(username);
        giftCardDao.save(giftCard);

        //商城或门店把礼品卡绑定到个人账户下后，将通过此接口把相关绑定信息同步给 PAAS，并自动同步给商城。
        String  result= SynchronizeGiftCard.boundGiftCard(ValueUtil.toJson(HttpStatus.SC_CREATED, giftCard));
        if(result==null||!result.equals("201")) {
            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
            ValueUtil.isError("同步失败");
        }
        return "success";
    }
    public String returnPassCard(String jsonData) throws YesmywineException {//订单退礼品卡金额
        // 礼品卡在商城退回了金额，会把退回记录自动同步给PAAS。
        Long cardNumber=Long.valueOf(ValueUtil.getFromJson(jsonData,"cardNumber"));
        Long orderNo=Long.valueOf(ValueUtil.getFromJson(jsonData,"orderNo"));
        Double usedAmount = Double.valueOf(ValueUtil.getFromJson(jsonData,"usedAmount"));
        Integer channel = Integer.valueOf(ValueUtil.getFromJson(jsonData,"channel"));
            GiftCard giftCard = giftCardDao.findByCardNumber(cardNumber);
            GiftCardHistory giftCardHistory = new GiftCardHistory();
            giftCardHistory.setCardNumber(cardNumber);//卡号
            giftCardHistory.setOrderNo(orderNo);//订单编号
            giftCardHistory.setUsedAmount(usedAmount);//退还金额
            giftCardHistory.setUsedTime(new Date());//时间
            giftCardHistory.setChannel(channel);//渠道（0官网、1门店）
            giftCardHistory.setGiftCardId(giftCard.getId());//礼品卡id
            giftCardHistory.setType(1);//退金额
            giftCardHistoryDao.save(giftCardHistory);
            Double remainingSum = giftCard.getRemainingSum();
            Double remainingSum1 = remainingSum + usedAmount;
            giftCard.setRemainingSum(remainingSum1);//剩余金额
            giftCardDao.save(giftCard);
            //商城或门店把礼品卡消费后，将通过此接口把消费信息同步给 PAAS，并自动同步给商城。
            String  result= SynchronizeGiftCard.spendGiftCard(ValueUtil.toJson(HttpStatus.SC_CREATED, giftCard));
            String  result1= SynchronizeGiftCard.synchronizeHistory(ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardHistory));

            if(result==null||!result.equals("201")||result1==null||!result1.equals("201")){
                TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                ValueUtil.isError("同步失败");
            }
//        }
        return "success";
    }

    public GiftCard useOneGiftCard(Long cardNumber,String password) throws YesmywineException {
        GiftCard giftCard = giftCardDao.findByCardNumber(cardNumber);
        if (giftCard == null) {
            ValueUtil.isError("没有此卡号");
        } else {
            GiftCard card = giftCardDao.findByCardNumberAndPassword(giftCard.getCardNumber(), password);
            if (card == null) {
                ValueUtil.isError("输入密码错误");
            } else {
                if(giftCard.getBoundStatus()==1){
                    ValueUtil.isError("此卡已经绑定请再可用礼品卡查看");
                }
                if (giftCard.getStatus() == 0) {
                    String latestTime = giftCard.getLatestTime().toString();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date d2 = null;
                    try {
                        d2 = sdf1.parse(latestTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date date = new Date();
                    if (d2.getTime() > date.getTime()) {
                        ValueUtil.isError("超过最迟激活时间，不可使用！");
                    }
                } else {
                    Date date = new Date();
                    if (card.getActivationTime().getTime() < date.getTime()) {
                        ValueUtil.isError("此卡已过期！");
                    } else if (card.getRemainingSum() <= 0) {
                        ValueUtil.isError("此卡已用完金额！");
                    }
                }
            }
            }
        return giftCard;
    }
    public Map<String, Object>  userGiftCart(Integer userId) throws YesmywineException {
        //type(0全部，1可用，2过期)
        Map<String, Object> map = new HashMap<>();
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject jsonObject2 = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject jsonObject3 = new com.alibaba.fastjson.JSONObject();

        List<GiftCard> giftCardList=giftCardDao.findByUserId(userId);
        jsonObject.put("giftCard",giftCardList);
        map.put("giftCartAll",jsonObject);


        List<GiftCard> giftCards2=new ArrayList<>();
        List<GiftCard> giftCards3=new ArrayList<>();

        Date date=new Date();
            List<GiftCard> giftCardList2 = giftCardDao.findByUserIdAndRemainingSumGreaterThan(userId,0.00);
            for(int i=0;i<giftCardList2.size();i++){
                Date actualMaturity=giftCardList2.get(i).getActualMaturity();
                if(actualMaturity==null||actualMaturity.getTime()>= date.getTime()){//未过期
                    giftCards2.add(giftCardList2.get(i));
                }else {
                    giftCards3.add(giftCardList2.get(i));//过期
                }
            }
        jsonObject2.put("giftCard",giftCards2);
        jsonObject3.put("giftCard",giftCards3);

        map.put("giftCartEnabLe",jsonObject2);
        map.put("giftCartExpire",jsonObject3);

        return map;
    }
//    public String buyGiftCard(Long cardNumber)throws YesmywineException{//商城购买礼品卡
//        GiftCard giftCard=giftCardDao.findByCardNumber(cardNumber);
//        if(giftCard==null){
//            ValueUtil.isError("没有此号礼品卡，请联系维护人员");
//        }else {
//            giftCard.setIfBuy(1);//是否购买(0否/1是)
//            String  result= SynchronizeGiftCard.buyGiftCard(ValueUtil.toJson(HttpStatus.SC_CREATED, giftCard));
//
//            if(result==null||!result.equals("201")||result==null||!result.equals("201")){
//                TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
//                ValueUtil.isError("同步失败");
//            }
//            giftCardDao.save(giftCard);
//        }
//        return "success";
//    }
    public List<GiftCard> randomGiftCard(Integer skuId,Integer counts) throws YesmywineException{//随机抽取未购买的礼品卡

        List<GiftCard> giftCardList=giftCardDao.findNoByGiftCard(skuId,counts);
        for (int i=0;i<giftCardList.size();i++) {
            giftCardList.get(i).setIfBuy(1);
        }
            String  result= SynchronizeGiftCard.buyGiftCard(ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardList));
            if(result==null||!result.equals("201")||result==null||!result.equals("201")){
                TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
                ValueUtil.isError("同步失败");

        }
        giftCardDao.save(giftCardList);
        return giftCardList;
    }
    public List<GiftCard> ordersGift(String cardNumbers)throws YesmywineException {//购买礼品卡后显示信息
        JSONArray adjustArray = JSON.parseArray(cardNumbers);
        List<GiftCard> giftCardList=new ArrayList<>();
        for (int i = 0; i < adjustArray.size(); i++) {
            JSONObject adjustCommand = (JSONObject) adjustArray.get(i);
            Long  cardNumber = Long.valueOf(adjustCommand.getString("cardNumber"));
            GiftCard giftCard=giftCardDao.findByCardNumber(cardNumber);
            if(giftCard==null){
                ValueUtil.isError("没有此礼品卡"+cardNumber);
            }
            giftCardList.add(giftCard);
        }
        return giftCardList;
    }
}
