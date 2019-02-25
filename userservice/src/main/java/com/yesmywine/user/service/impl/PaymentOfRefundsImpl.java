package com.yesmywine.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.user.bean.ConstantData;
import com.yesmywine.user.dao.*;
import com.yesmywine.user.entity.*;
import com.yesmywine.user.service.PaymentOfRefunds;
import com.yesmywine.util.basic.*;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * Created by ${shuang} on 2017/6/23.
 */
@Service
public class PaymentOfRefundsImpl implements PaymentOfRefunds {

    @Autowired
    private ChargeFlowDao chargeFlowDao;
    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private BeanFlowDao beanFlowDao;
    @Autowired
    private VipRuleDao vipRuleDao;
    @Autowired
    private LevelHistoryDao levelHistoryDao;

    @Override
    public String payment(Map<String, String> params,Integer userId) throws YesmywineException {//全都有用酒豆和余额支付.
        Integer consumeBean = Integer.parseInt(params.get("consumeBean"));//消耗的酒豆
        Double payMoney = Double.valueOf(params.get("payMoney"));//支付使用的余额
        String orderNumber = params.get("orderNumber");//拿到订单号

        UserInformation userInformation = userInformationDao.findOne(userId);
        Double remainingSum = userInformation.getRemainingSum();//个人余额
        Double bean = userInformation.getBean();//个人酒豆
        //数据类型转换
        BigDecimal bigDecimal1 = new BigDecimal(remainingSum);
        BigDecimal bigDecimal2 = new BigDecimal(payMoney);
        //计算余额
        Double result = bigDecimal1.subtract(bigDecimal2).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
        BigDecimal bigDecimal3 = new BigDecimal(bean);
        BigDecimal bigDecimal4 = new BigDecimal(consumeBean);
        //计算剩余酒豆
        Double result2 = bigDecimal3.subtract(bigDecimal4).setScale(2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
        if(result<0||result2<0){
            return "余额不足,豆子不足" ;
        }
        userInformation.setRemainingSum(result);
        userInformation.setBean(result2);

        BeanFlow beanFlow=new BeanFlow();
        beanFlow.setBeans(consumeBean.doubleValue());
        beanFlow.setPoints(0);
        beanFlow.setUserId(userId);
        beanFlow.setOrderNumber(orderNumber);
        beanFlow.setStatus("consume");
        beanFlow.setUserName(userInformation.getUserName());
        beanFlow .setDescription("订单号"+orderNumber);
        beanFlow.setChannelCode("商城");

        ChargeFlow chargeFlow = new ChargeFlow();
        chargeFlow.setUserId(userId);
        chargeFlow.setUserName(userInformation.getUserName());
        chargeFlow.setChargeMoney(payMoney);
        chargeFlow.setOrderNumber(orderNumber);
        chargeFlow.setRemianMonney(result.doubleValue());
        chargeFlow.setStatus("1");

        HashMap<String,Object> map =new HashMap<>();
        map.put("status","consume");
        map.put("userName",userInformation.getUserName());
        map.put("orderNumber",orderNumber);
        map.put("channelCode","商城");
        map.put("bean",bean);

        String centerFlowcode = SynchronizeUtils.paramsCode(Dictionary.PAAS_HOST,
                "/user/synchro", RequestMethod.post,map);

        //        同步消耗酒豆到pass
        String code = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,"/user/beans/synchronization",ValueUtil.toJson(beanFlow),RequestMethod.post);

//        同步用户信息到pass的用户中心
        String code1 = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,"/user/userInfo/syn", ValueUtil.toJson(userInformation),RequestMethod.post);
        if(ValueUtil.notEmpity(code1)&&code1.equals("201")){
            userInformation.setSynStatus(1);
        }else {
            userInformation.setSynStatus(0);
        }
        if(ValueUtil.notEmpity(code)&&code.equals("201")){
            beanFlow.setSynStatus("1");
        }else {
            beanFlow.setSynStatus("0");
        }
        chargeFlowDao.save(chargeFlow);
        beanFlowDao.save(beanFlow);
        userInformationDao.save(userInformation);

//        发短信给用户用了多少酒豆
        HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/sms/send/sendSms/itf",RequestMethod.post);
        httpRequest.addParameter("phones",userInformation.getPhoneNumber());
        httpRequest.addParameter("code",ConstantData.yue);
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("time",df.format(new Date()));
        jsonObject.put("type","消耗");
        jsonObject.put("rechargeAmount",payMoney);
        jsonObject.put("balance",result);
        httpRequest.addParameter("json",jsonObject);
        httpRequest.run();

        return ValueUtil.toJson(HttpStatus.SC_CREATED,"success");
    }


    @Override
    public String returns(Map<String, String> params,Integer userId) throws YesmywineException {
        //退货退回钱,退酒豆
        Double returnBean = Double.valueOf(params.get("returnBean"));//退回酒豆
        Double returnMoney = Double.valueOf(params.get("returnMoney"));//退回钱
        String orderNumber = params.get("orderNumber");//订单号
        Integer returnPoint = Integer.valueOf(params.get("returnPoint"));//退回积分
        UserInformation userInformation = userInformationDao.findOne(userId);
        Double remainingSum = userInformation.getRemainingSum();//个人余额
        Double bean = userInformation.getBean();//个人酒豆
        Integer point = userInformation.getGrowthValue();//个人积分
        BigDecimal bean1 = new BigDecimal(bean);//个人酒豆格式化
        BigDecimal remainingSum1 = new BigDecimal(remainingSum);//个人余额格式化
        BigDecimal returnMoney1 = new BigDecimal(returnMoney);//退回的余额格式化
        Double money =remainingSum1.add(returnMoney1).setScale(2,ROUND_HALF_DOWN).doubleValue();//余额
        userInformation.setRemainingSum(money);//最后的钱
        Integer currentId = userInformation.getVipRule().getId();//当前用户等级Id
        Integer require=vipRuleDao.findOne(currentId).getRequireValue();//当前等级要求分
        Integer selfPoint= point-returnPoint;//剩余个人积分
        LevelHistory levelHistory = new LevelHistory();
        if(selfPoint<0){//退积分如果小于0直接回复普通会员身份
            Integer minId=vipRuleDao.findMinId();//最小的id
            VipRule vipRule= vipRuleDao.findOne(minId);//当前等级规则
            userInformation.setVipRule(vipRule);//最后的等级
            userInformation.setGrowthValue(0);
            levelHistory.setUserName(userInformation.getUserName());
            levelHistory.setUserId(userId);
            levelHistory.setRemarks("降级");
            levelHistory.setOperateTimes(new Date());
            levelHistoryDao.save(levelHistory);
        }else {
            if(selfPoint<require){//小于当前等级要求的保级积分
                Integer minId = vipRuleDao.findMinId();
                VipRule vipRule1 = vipRuleDao.findOne(minId);
                if(!(userInformation.getVipRule().getId()==vipRule1.getId())){
                    List<VipRule> list=vipRuleDao.findMin(require);//小于当前等级的所有集合   ASC
                    VipRule vipRule=list.get(list.size()-1);//获取下一级规则
                    userInformation.setVipRule(vipRule);//最后的等级
                    levelHistory.setUserName(userInformation.getUserName());
                    levelHistory.setUserId(userId);
                    levelHistory.setRemarks("降级");
                    levelHistory.setOperateTimes(new Date());
                    levelHistoryDao.save(levelHistory);
                }
            }
            userInformation.setGrowthValue(selfPoint);//最后的积分
        }
        String json = this.http("GW");
        String mopo =ValueUtil.getFromJson(json,"mopo");//钱兑换积分
        String mobe =ValueUtil.getFromJson(json,"mobe");//钱兑换酒豆
        String rule = mopo;
        String[] rmbpoint = rule.split(":");
        BigDecimal rmb1 = new BigDecimal(Double.valueOf(rmbpoint[0]));
        BigDecimal point1 = new BigDecimal(Double.valueOf(rmbpoint[1]));
        BigDecimal point2 = new BigDecimal(Double.valueOf(returnPoint));//传入积分
        String proportion = mobe;
        String[] rmbBeans = proportion.split(":");
        BigDecimal rmb2 = new BigDecimal(Double.valueOf(rmbBeans[0]));
        BigDecimal beans = new BigDecimal(Double.valueOf(rmbBeans[1]));
        Double newRmb = (rmb1.multiply(point2)).divide(point1, 2, ROUND_HALF_DOWN).doubleValue();//兑换的人民币
        BigDecimal newBeans = (beans.multiply(BigDecimal.valueOf(newRmb))).divide(rmb2, 2, ROUND_HALF_DOWN);//兑换的酒豆
        BigDecimal returnBean1 = new BigDecimal(Double.valueOf(returnBean));//退回的酒豆格式化
        Double remainBean =bean1.add(returnBean1.subtract(newBeans)).setScale(2,ROUND_HALF_DOWN).doubleValue();//剩余酒豆
        userInformation.setBean(remainBean);//最后的酒豆

            ChargeFlow chargeFlow = new ChargeFlow();//个人消费金额记录
            chargeFlow.setRemianMonney(money);
            chargeFlow.setOrderNumber(orderNumber);
            chargeFlow.setUserId(userId);
            chargeFlow.setChargeMoney(returnMoney);
            chargeFlow.setStatus("3");
            chargeFlow.setUserName(userInformation.getUserName());
            chargeFlowDao.save(chargeFlow);

        List<BeanFlow> list =new ArrayList<>();
        BeanFlow beanFlow = new BeanFlow();//退回记录
        beanFlow.setUserId(userId);
        beanFlow.setUserName(userInformation.getUserName());
        beanFlow.setOrderNumber(orderNumber);
        beanFlow.setBeans(returnBean.doubleValue());
        beanFlow.setPoints(0);
        beanFlow.setDescription(orderNumber+"订单退酒豆");
        beanFlow.setStatus("generate");
        BeanFlow beanFlow2 = new BeanFlow();//退回记录
        beanFlow2.setUserId(userId);
        beanFlow2.setUserName(userInformation.getUserName());
        beanFlow2.setOrderNumber(orderNumber);
        beanFlow2.setBeans(newBeans.doubleValue());
        beanFlow2.setPoints(returnPoint);
        beanFlow2.setDescription(orderNumber+"酒豆收回");
        beanFlow2.setStatus("consume");
        HashMap<String,Object> map = new HashMap<>();
        map.put("userName",userInformation.getUserName());
        map.put("returnBean",returnBean);
        map.put("status","return");
        map.put("channelCode","商城");
        map.put("orderNumber",orderNumber);
        map.put("point",returnPoint);
        map.put("newBeans",newBeans);
        map.put("userId",userId);
//同步酒豆记录到paas
        String code = SynchronizeUtils.paramsCode(Dictionary.PAAS_HOST,"/user/synchro",RequestMethod.post,map);

//        同步用户信息到pass的用户中心
        String code1 = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,"/user/userInfo/syn", ValueUtil.toJson(userInformation),RequestMethod.post);


        if(ValueUtil.notEmpity(code1)&&code1.equals("201")){
            userInformation.setSynStatus(1);
        }else {
            userInformation.setSynStatus(0);
        }
        if(ValueUtil.notEmpity(code)&&code.equals("201")){
            beanFlow.setSynStatus("1");
            beanFlow2.setSynStatus("1");
        }else {
            beanFlow.setSynStatus("0");
            beanFlow2.setSynStatus("0");
        }
        list.add(beanFlow);
        list.add(beanFlow2);
        beanFlowDao.save(list);
        userInformationDao.save(userInformation);
        //发短信告诉用户退了多少钱
        HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/sms/send/sendSms/itf",RequestMethod.post);
        httpRequest.addParameter("phones",userInformation.getPhoneNumber());
        httpRequest.addParameter("code",ConstantData.yue);
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("time",df.format(new Date()));
        jsonObject.put("type","退款");
        jsonObject.put("rechargeAmount",returnMoney);
        jsonObject.put("balance",money);
        httpRequest.addParameter("json",jsonObject);
        httpRequest.run();
        return "success";

    }

    @Override
    public String returnsAndPoint(Map<String, String> params, Integer userId) throws YesmywineException {
        Double returnBean = Double.valueOf(params.get("returnBean"));//退回酒豆
        Double returnMoney = Double.valueOf(params.get("returnMoney"));//退回钱
        String orderNumber = params.get("orderNumber");//订单号
        Integer returnPoint =0;//退回积分
        UserInformation userInformation = userInformationDao.findOne(userId);
        Double remainingSum = userInformation.getRemainingSum();//个人余额
        Double bean = userInformation.getBean();//个人酒豆
        Integer point = userInformation.getGrowthValue();//个人积分
        BigDecimal bean1 = new BigDecimal(bean);//个人酒豆格式化
        BigDecimal remainingSum1 = new BigDecimal(remainingSum);//个人余额格式化
        BigDecimal returnMoney1 = new BigDecimal(returnMoney);//退回的余额格式化
        Double money =remainingSum1.add(returnMoney1).setScale(2,ROUND_HALF_DOWN).doubleValue();//余额
        userInformation.setRemainingSum(money);//最后的钱
        String json = this.http("商城");
        String mopo =ValueUtil.getFromJson(json,"mopo");//钱兑换积分
        String mobe =ValueUtil.getFromJson(json,"mobe");//钱兑换酒豆
        String rule = mopo;
        String[] rmbpoint = rule.split(":");
        BigDecimal rmb1 = new BigDecimal(Double.valueOf(rmbpoint[0]));
        BigDecimal point1 = new BigDecimal(Double.valueOf(rmbpoint[1]));
        BigDecimal point2 = new BigDecimal(Double.valueOf(returnPoint));//传入积分
        String proportion = mobe;
        String[] rmbBeans = proportion.split(":");
        BigDecimal rmb2 = new BigDecimal(Double.valueOf(rmbBeans[0]));
        BigDecimal beans = new BigDecimal(Double.valueOf(rmbBeans[1]));
        Double newRmb = (rmb1.multiply(point2)).divide(point1, 2, ROUND_HALF_DOWN).doubleValue();//兑换的人民币
        BigDecimal newBeans = (beans.multiply(BigDecimal.valueOf(newRmb))).divide(rmb2, 2, ROUND_HALF_DOWN);//兑换的酒豆
        BigDecimal returnBean1 = new BigDecimal(Double.valueOf(returnBean));//退回的酒豆格式化
        Double remainBean =bean1.add(returnBean1.subtract(newBeans)).setScale(2,ROUND_HALF_DOWN).doubleValue();//剩余酒豆
        userInformation.setBean(remainBean);//最后的酒豆
        if(returnMoney!=0.0){
            ChargeFlow chargeFlow = new ChargeFlow();//个人消费金额记录
            chargeFlow.setRemianMonney(money);
            chargeFlow.setOrderNumber(orderNumber);
            chargeFlow.setUserId(userId);
            chargeFlow.setChargeMoney(returnMoney);
            chargeFlow.setStatus("3");
            chargeFlow.setUserName(userInformation.getUserName());
            chargeFlowDao.save(chargeFlow);
        }
        List<BeanFlow> list =new ArrayList<>();
        BeanFlow beanFlow = new BeanFlow();//退回记录
        beanFlow.setUserId(userId);
        beanFlow.setUserName(userInformation.getUserName());
        beanFlow.setOrderNumber(orderNumber);
        beanFlow.setBeans(returnBean.doubleValue());
        beanFlow.setPoints(0);
        beanFlow.setDescription(orderNumber+"订单退酒豆");
        beanFlow.setStatus("generate");
        BeanFlow beanFlow2 = new BeanFlow();//退回记录
        beanFlow2.setUserId(userId);
        beanFlow2.setUserName(userInformation.getUserName());
        beanFlow2.setOrderNumber(orderNumber);
        beanFlow2.setBeans(newBeans.doubleValue());
        beanFlow2.setPoints(returnPoint);
        beanFlow2.setDescription(orderNumber+"酒豆收回");
        beanFlow2.setStatus("consume");
        HashMap<String,Object> map = new HashMap<>();
        map.put("userName",userInformation.getUserName());
        map.put("returnBean",returnBean);
        map.put("status","return");
        map.put("channelCode","商城");
        map.put("orderNumber",orderNumber);
        map.put("point",returnPoint);
        map.put("newBeans",newBeans);
        map.put("userId",userId);

        String code = SynchronizeUtils.paramsCode(Dictionary.PAAS_HOST,"/user/synchro",RequestMethod.post,map);

//        同步用户信息到pass的用户中心
        String code1 = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,"/user/userInfo/syn", ValueUtil.toJson(userInformation),RequestMethod.post);


        if(ValueUtil.notEmpity(code1)&&code1.equals("201")){
            userInformation.setSynStatus(1);
        }else {
            userInformation.setSynStatus(0);
        }
        if(ValueUtil.notEmpity(code)&&code.equals("201")){
            beanFlow.setSynStatus("1");
            beanFlow2.setSynStatus("1");
        }else {
            beanFlow.setSynStatus("0");
            beanFlow2.setSynStatus("0");
        }
        list.add(beanFlow);
        list.add(beanFlow2);
        beanFlowDao.save(list);
        userInformationDao.save(userInformation);
//发短信告诉用户
        HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/sms/send/sendSms/itf",RequestMethod.post);
        httpRequest.addParameter("phones",userInformation.getPhoneNumber());
        httpRequest.addParameter("code",ConstantData.yue);
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("time",df.format(new Date()));
        jsonObject.put("type","退款");
        jsonObject.put("rechargeAmount",returnMoney);
        jsonObject.put("balance",money);
        httpRequest.addParameter("json",jsonObject);
        httpRequest.run();
        return "success";
    }

    public String http(String channelCode){
            HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/user/rule/itf", RequestMethod.get);
            httpRequest.addParameter("channelCode", channelCode);
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String result = ValueUtil.getFromJson(temp, "data");
            return   result;
    }
}
