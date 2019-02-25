package com.yesmywine.pay.controller;

import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.service.PaymentParamService;
import com.yesmywine.pay.dao.PaymentDao;
import com.yesmywine.pay.entity.AlipaySetting;
import com.yesmywine.pay.entity.PaymentEntity;
import com.yesmywine.pay.entity.UnionPaySetting;
import com.yesmywine.pay.entity.WeChatPaySetting;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.enums.Active;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by SJQ on 2017/2/23.
 */
@RestController
@RequestMapping("/pay/setting")
public class PaymentSettingController {

    @Autowired
    private PaymentParamService paymentParamService;
    @Autowired
    private PaymentDao paymentDao;

    @RequestMapping(method = RequestMethod.GET)
    public String getSettingInfo(String type) {
        if (type.equals(Payment.Alipay.name())) {
            AlipaySetting alipaySetting = paymentParamService.findAlipayById(1);
            PaymentEntity paymentEntity = paymentDao.findByPaymentCode(Payment.Alipay);
            alipaySetting.setActive(paymentEntity.getActive());
            return ValueUtil.toJson(alipaySetting);
        } else if (type.equals(Payment.UnionPay.name())) {
            UnionPaySetting unionPaySetting = paymentParamService.findUnionPayById(1);
            PaymentEntity paymentEntity = paymentDao.findByPaymentCode(Payment.UnionPay);
            unionPaySetting.setActive(paymentEntity.getActive());
            return ValueUtil.toJson(unionPaySetting);
        } else if (type.equals(Payment.WeChat.name())) {
            WeChatPaySetting weChatPaySetting = paymentParamService.findWeChatById(1);
            PaymentEntity paymentEntity = paymentDao.findByPaymentCode(Payment.WeChat);
            weChatPaySetting.setActive(paymentEntity.getActive());
            return ValueUtil.toJson(weChatPaySetting);
        } else {
            List list = new ArrayList<>();
            AlipaySetting alipaySetting = paymentParamService.findAlipayById(1);
            PaymentEntity paymentEntity = paymentDao.findByPaymentCode(Payment.Alipay);
            alipaySetting.setActive(paymentEntity.getActive());
            list.add(alipaySetting);
            UnionPaySetting unionPaySetting = paymentParamService.findUnionPayById(1);
            PaymentEntity paymentEntity2 = paymentDao.findByPaymentCode(Payment.UnionPay);
            unionPaySetting.setActive(paymentEntity2.getActive());
            list.add(unionPaySetting);
            WeChatPaySetting weChatPaySetting = paymentParamService.findWeChatById(1);
            PaymentEntity paymentEntity3 = paymentDao.findByPaymentCode(Payment.WeChat);
            weChatPaySetting.setActive(paymentEntity3.getActive());
            list.add(weChatPaySetting);
            return ValueUtil.toJson(list);
        }
    }

    /*
    *@Author Gavin
    *@Description   启动支付方式
    *@Date 2017/2/23 14:43
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public String startSetting(@RequestParam Map<String, Object> map) {
        try {
            Integer[] ids = (Integer[]) map.get("ids");
            for (int i = 0; i < ids.length; i++) {
                Integer id = ids[i];
                PaymentEntity paymentEntity = paymentDao.findOne(id);

                ValueUtil.verifyNotExist(paymentEntity, "无此交易类型");

                paymentEntity.setActive(Active.inActive);
                paymentDao.save(paymentEntity);
            }
            return ValueUtil.toJson("OK");
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }

    /*
    *@Author Gavin
    *@Description   停用支付方式
    *@Date 2017/2/23 14:43
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public String stop(@RequestParam Map<String, Object> map) {
        Integer[] ids = (Integer[]) map.get("ids");
        for (int i = 0; i < ids.length; i++) {
            Integer id = ids[i];
            PaymentEntity paymentEntity = paymentDao.findOne(id);
            if (paymentEntity != null) {
                paymentEntity.setActive(Active.notActive);
                paymentDao.save(paymentEntity);
                return ValueUtil.toJson(paymentEntity);
            } else {
                return ValueUtil.toError("000", "支付方式为空");
            }
        }
        return null;
    }

    /*
    *@Author Gavin
    *@Description 修改参数
    *@Date 2017/2/23 14:37
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.PUT)
    public String updateParam(@RequestParam Map<String, String> map, String type) {
        try {
            System.out.println("请求参数==》"+map.toString());
            ValueUtil.verify(map, new String[]{"type", "id"});
            if (type.equals(Payment.Alipay.name())) {
                return alipaySetting(map);
            } else if (type.equals(Payment.UnionPay.name())) {
                return unionPaySetting(map);
            } else if (type.equals(Payment.WeChat.name())) {
                return wechatPaySetting(map);
            } else {
                return null;
            }
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 支付宝支付参数配置
    *@Date 2017/2/23 14:37
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/alipay", method = RequestMethod.PUT)
    public String alipaySetting(Map<String, String> map) {
        try {
            System.out.println("修改Alipay");
            ValueUtil.verify(map, new String[]{"name","code","sign_type","input_charset","partner", "sellerEmail", "notifyUrl", "body", "MD5Key", "active"});
            AlipaySetting oldAlipaySetting = paymentParamService.findAlipayById(Integer.valueOf(map.get("id")));
            oldAlipaySetting.setBody(map.get("body"));
            oldAlipaySetting.setMD5Key(map.get("MD5Key"));
            oldAlipaySetting.setNotifyUrl(map.get("notifyUrl"));
            oldAlipaySetting.setReturnUrl(map.get("refundBackUrl"));
            oldAlipaySetting.setSellerEmail(map.get("sellerEmail"));
            paymentParamService.updateAlipay(oldAlipaySetting);
            PaymentEntity paymentEntity = paymentDao.findByPaymentCode(Payment.Alipay);

            switch (map.get("active")) {
                case "inActive":
                    System.out.println("修改状态为启动==》");
                    paymentEntity.setActive(Active.inActive);
                    paymentDao.save(paymentEntity);
                    break;
                case "notActive":
                    System.out.println("修改状态为禁用==》");
                    paymentEntity.setActive(Active.notActive);
                    paymentDao.save(paymentEntity);
                    break;
            }
            oldAlipaySetting.setActive(paymentEntity.getActive());
            return ValueUtil.toJson(HttpStatus.SC_CREATED, oldAlipaySetting);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 银联支付参数配置
    *@Date 2017/2/23 14:37
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "unionPay", method = RequestMethod.PUT)
    public String unionPaySetting(Map<String, String> map) {
        try {
            ValueUtil.verify(map, new String[]{"name","code","merId", "frontUrl", "payBackUrl", "active"});
            UnionPaySetting oldUnionPaySetting = paymentParamService.findUnionPayById(Integer.valueOf(map.get("id")));
            oldUnionPaySetting.setMerId(map.get("merId"));
            oldUnionPaySetting.setPayBackUrl(map.get("payBackUrl"));
            oldUnionPaySetting.setFrontUrl(map.get("frontUrl"));
            paymentParamService.updateUnionPay(oldUnionPaySetting);
            PaymentEntity paymentEntity = paymentDao.findByPaymentCode(Payment.UnionPay);
            switch (map.get("active")) {
                case "inActive":
                    System.out.println("修改状态为启动==》");
                    paymentEntity.setActive(Active.inActive);
                    paymentDao.save(paymentEntity);
                    break;
                case "notActive":
                    System.out.println("修改状态为禁用==》");
                    paymentEntity.setActive(Active.notActive);
                    paymentDao.save(paymentEntity);
                    break;
            }
            oldUnionPaySetting.setActive(paymentEntity.getActive());
            return ValueUtil.toJson(HttpStatus.SC_CREATED, oldUnionPaySetting);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description   微信支付参数配置
    *@Date 2017/2/23 14:34
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "wechatPay", method = RequestMethod.PUT)
    public String wechatPaySetting(Map<String, String> map) {
        try {
            ValueUtil.verify(map, new String[]{"name","code","appId", "mchId", "apiKey", "notifyUrl", "body", "active"});

            WeChatPaySetting oldWeChatPaySetting = paymentParamService.findWeChatById(Integer.valueOf(map.get("id")));
            oldWeChatPaySetting.setAppId(map.get("appId"));
            oldWeChatPaySetting.setMchId(map.get("mchId"));
            oldWeChatPaySetting.setNotifyUrl(map.get("notifyUrl"));
            oldWeChatPaySetting.setApiKey(map.get("apiKey"));
            oldWeChatPaySetting.setBody(map.get("body"));
            paymentParamService.updateUnionPay(oldWeChatPaySetting);
            PaymentEntity paymentEntity = paymentDao.findByPaymentCode(Payment.WeChat);
            switch (map.get("active")) {
                case "inActive":
                    System.out.println("修改状态为启动==》");
                    paymentEntity.setActive(Active.inActive);
                    paymentDao.save(paymentEntity);
                    break;
                case "notActive":
                    System.out.println("修改状态为禁用==》");
                    paymentEntity.setActive(Active.notActive);
                    paymentDao.save(paymentEntity);
                    break;
            }
            oldWeChatPaySetting.setActive(paymentEntity.getActive());
            return ValueUtil.toJson(HttpStatus.SC_CREATED, oldWeChatPaySetting);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

}
