package com.yesmywine.pay.service;

import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.dao.PaymentDao;
import com.yesmywine.pay.entity.AlipaySetting;
import com.yesmywine.pay.entity.PaymentEntity;
import com.yesmywine.pay.entity.UnionPaySetting;
import com.yesmywine.pay.entity.WeChatPaySetting;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.enums.Active;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/1/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Component
public class PayFactory {

    @Autowired
    private BeanFactory beanFactory;
    @Autowired
    private PaymentDao paymentDao;
    @Autowired
    private PaymentParamService paymentParamService;

    private static Map<Payment, Class> allInstance = new HashMap<>();

    //                 code    classPath
    private static Map<Payment, String> allInstanceR = new HashMap<>();

    private static Map<Payment, Boolean> instanceStatus = new HashMap<>();

    public static AlipaySetting alipaySetting;
    public static UnionPaySetting unionPaySetting;
    public static WeChatPaySetting weChatPaySetting;

    static {
        allInstanceR.clear();
        allInstanceR.put(Payment.Alipay, "com.yesmywine.pay.alipay.AlipayBiz");
        allInstanceR.put(Payment.WeChat, "com.yesmywine.pay.wechat.WeChatPay");
        allInstanceR.put(Payment.UnionPay, "com.yesmywine.pay.unionpay.UnionPay");
//		allInstanceR.put(Payment.CashOnDelivery,"");

        allInstance.clear();
        for (Map.Entry<Payment, String> entry : allInstanceR.entrySet()) {
            try {
                Class impl = Class.forName(entry.getValue());
                allInstance.put(entry.getKey(), impl);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public Object refreshPayment() {
        List<PaymentEntity> paymentEntities = paymentDao.findAll();
        instanceStatus.clear();
        paymentEntities.forEach(p -> {
            Boolean flag = false;
            if (p.getActive().equals(Active.inActive)) {
                flag = true;
            }
            instanceStatus.put(p.getPaymentCode(), flag);
        });

        alipaySetting = paymentParamService.getAlipay();
        unionPaySetting = paymentParamService.getUnionPay();
        weChatPaySetting = paymentParamService.getWeChat();
        return instanceStatus;
    }

    public PaymentBiz getInstance(Payment code) throws YesmywineException {//得到一个实例

        PaymentBiz biz = null;
        if (allInstance == null) {
            ValueUtil.isError("需先运行refresh");
        }
        if (null == instanceStatus.get(code) || instanceStatus.get(code).equals(false)) {
            ValueUtil.isError("noActivityPayment");
        }

        biz = (PaymentBiz) beanFactory.getBean(allInstance.get(code));
        return biz;
    }

    public static AlipaySetting getAlipaySetting() {
        return alipaySetting;
    }

    public static void setAlipaySetting(AlipaySetting alipaySetting) {
        PayFactory.alipaySetting = alipaySetting;
    }

    public static UnionPaySetting getUnionPaySetting() {
        return unionPaySetting;
    }

    public static void setUnionPaySetting(UnionPaySetting unionPaySetting) {
        PayFactory.unionPaySetting = unionPaySetting;
    }

    public static WeChatPaySetting getWeChatPaySetting() {
        return weChatPaySetting;
    }

    public static void setWeChatPaySetting(WeChatPaySetting weChatPaySetting) {
        PayFactory.weChatPaySetting = weChatPaySetting;
    }
}
