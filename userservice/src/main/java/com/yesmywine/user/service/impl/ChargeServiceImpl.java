
package com.yesmywine.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.user.IdUtil;
import com.yesmywine.user.bean.ConstantData;
import com.yesmywine.user.controller.BeanController;
import com.yesmywine.user.dao.ChargeFlowDao;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.entity.ChargeFlow;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.service.ChargeService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * Created by ${shuang} on 2017/4/5.
 */
@Service
public class ChargeServiceImpl extends BaseServiceImpl<ChargeFlow, Integer> implements ChargeService {

    @Autowired
    private UserInformationDao userInformationDao;
    @Autowired
    private ChargeFlowDao chargeFlowDao;
    @Autowired
    private BeanController beanController;

    @Override
    public Object charge(@RequestParam Map<String, String> params) throws YesmywineException {
        ChargeFlow chargeFlow = chargeFlowDao.findByOrderNumber(params.get("chargeNumber"));//通过充值订单号拿到充值订单
        Integer userId = chargeFlow.getUserId();
        UserInformation userInformation = userInformationDao.findOne(userId);
        Double remianMoney = userInformation.getRemainingSum();//拿到用户的余额
        Double chargeMoney = chargeFlow.getChargeMoney();//从记录中拿到用户充值了多少钱
        //转换这两个数据的类型
        BigDecimal bigDecimal1 = new BigDecimal(remianMoney);//余额
        BigDecimal bigDecimal2 = new BigDecimal(chargeMoney);//充值
        //为用户加上充值的余额
        BigDecimal result = bigDecimal1.add(bigDecimal2).setScale(2, ROUND_HALF_DOWN);
        userInformation.setRemainingSum(result.doubleValue());
        chargeFlow.setUserId(userId);
        chargeFlow.setUserName(userInformation.getUserName());
        chargeFlow.setRemianMonney(result.doubleValue());
        chargeFlow.setStatus("2");
        chargeFlow.setChargeWay(params.get("chargeWay"));
        //充值完成后向paas同步用户信息。
        String code = SynchronizeUtils.getCode(Dictionary.PAAS_HOST, "/user/userInfo/syn", ValueUtil.toJson(HttpStatus.SC_CREATED, userInformation), RequestMethod.post);
        if (ValueUtil.notEmpity(code) && code.equals("201")) {
            userInformation.setSynStatus(1);
        } else {
            userInformation.setSynStatus(0);
        }
        chargeFlowDao.save(chargeFlow);
        userInformationDao.save(userInformation);

//        获取充值送的积分
        Map<String, Object> payParams = new HashMap<>();
        payParams.put("money", chargeMoney);
        payParams.put("status", "充值");
        String point = null;
        String newResult = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/sso/point/itf", RequestMethod.get, payParams, null);
        if (result != null) {
            JSONObject jsonObj = JSON.parseObject(newResult);
            String code1 = jsonObj.getString("code");
            if (code1.equals("200")) {
                point = jsonObj.getString("data");
            }
        }

//        充值完成给用户发短消息
        HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/sms/send/sendSms/itf", RequestMethod.post);
        httpRequest.addParameter("phones", userInformation.getPhoneNumber());
        httpRequest.addParameter("code", ConstantData.yue);
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("time", df.format(new Date()));
        jsonObject.put("type", "充值");
        jsonObject.put("rechargeAmount", chargeMoney);
        jsonObject.put("balance", result);
        httpRequest.addParameter("json", jsonObject);
        httpRequest.run();
//        充值完成后生成酒豆
        Double bean = beanController.beansCreate(userInformation.getUserName(), userInformation.getPhoneNumber(), params.get("chargeNumber"), Integer.valueOf(point), "GW");
        return "success";

    }


    @Override
    public Object consume(Map<String, String> params, Integer userId) {
        Double payMoney = Double.valueOf(params.get("payMoney"));//减，支付金额
        String orderNumber = params.get("orderNumber");
        UserInformation userInformation = userInformationDao.findOne(userId);
        Double remainingSum = userInformation.getRemainingSum();//个人余额
        BigDecimal bigDecimal1 = new BigDecimal(remainingSum);
        BigDecimal bigDecimal2 = new BigDecimal(payMoney);
        //判断用余额支付，是否足够抵扣
        Double result = bigDecimal1.subtract(bigDecimal2).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        if (result < 0) {
            return "余额不足";
        }
        userInformation.setRemainingSum(result);
        ChargeFlow chargeFlow = new ChargeFlow();
        chargeFlow.setUserId(userId);
        chargeFlow.setUserName(userInformation.getUserName());
        chargeFlow.setChargeMoney(payMoney);
        chargeFlow.setOrderNumber(orderNumber);
        chargeFlow.setRemianMonney(result.doubleValue());
        chargeFlow.setStatus("1");

//        向paas同步用户信息
        String code = SynchronizeUtils.getCode(Dictionary.PAAS_HOST, "/user/userInfo/syn", ValueUtil.toJson(userInformation), RequestMethod.post);
        if (ValueUtil.notEmpity(code) && code.equals("201")) {
            userInformation.setSynStatus(1);
        } else {
            userInformation.setSynStatus(0);
        }

//        向用户发送短信通知消耗多少钱
        HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/sms/send/sendSms/itf", RequestMethod.post);
        httpRequest.addParameter("phones", userInformation.getPhoneNumber());
        httpRequest.addParameter("code", ConstantData.yue);
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        jsonObject.put("time", df.format(new Date()));
        jsonObject.put("type", "消耗");
        jsonObject.put("rechargeAmount", payMoney);
        jsonObject.put("balance", result);
        httpRequest.addParameter("json", jsonObject);
        httpRequest.run();


        chargeFlowDao.save(chargeFlow);
        userInformationDao.save(userInformation);
        return "success";
    }

//    生成充值记录不，包含订单号
    @Override
    public Object chargeFlow(Map<String, String> params, Integer userId) {
        ChargeFlow chargeFlow = new ChargeFlow();
        Double chargeMoney = Double.valueOf(params.get("chargeMoney"));
        UserInformation userInformation = userInformationDao.findOne(userId);
        chargeFlow.setUserId(userId);
        chargeFlow.setUserName(userInformation.getUserName());
        chargeFlow.setChargeMoney(chargeMoney);
        chargeFlow.setStatus("0");
        ChargeFlow chargeFlow1 = chargeFlowDao.save(chargeFlow);
        Long orderNo = IdUtil.genId("yyMMdd1{s}{s}{s}{r}{r}{s}{s}{r}{r}", chargeFlow1.getId(), 5);
        chargeFlow1.setOrderNumber(orderNo.toString());
        chargeFlowDao.save(chargeFlow1);
        Map<String, Object> map = new HashMap<>();
        map.put("orderNumber", orderNo);
        return map;
    }

}
