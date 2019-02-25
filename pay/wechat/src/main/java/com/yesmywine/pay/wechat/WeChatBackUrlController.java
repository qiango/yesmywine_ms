package com.yesmywine.pay.wechat;

import com.yesmywine.pay.bean.Notify;
import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.service.TransactionService;
import com.yesmywine.pay.entity.TransactionHistory;
import com.yesmywine.pay.wechat.utils.XmlUtils;
import com.yesmywine.util.basic.*;
import com.yesmywine.util.basic.Dictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by SJQ on 2017/2/27.
 */
@RestController
@RequestMapping("/pay/back/wechat")
public class WeChatBackUrlController {

    @Autowired
    private TransactionService transactionService;

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/8/14
    *@Param
    *@Description:下单成功回调
    */
    @RequestMapping(method = RequestMethod.POST)
    public String paySuccessCallBack(HttpServletRequest request, HttpServletResponse response) {
        SortedMap<String, Object> result = getAllRequestParam(request);
        System.out.println(result.toString());
        saveTradingRecord(result);
        return null;
    }

    public static SortedMap<String, Object> getAllRequestParam(
            final HttpServletRequest request) {

        StringBuffer info = new java.lang.StringBuffer();
        InputStream in = null;
        try {
            in = request.getInputStream();
            BufferedInputStream buf = new BufferedInputStream(in);
            byte[] buffer = new byte[1024];
            int iRead;
            while ((iRead = buf.read(buffer)) != -1) {
                info.append(new String(buffer, 0, iRead, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SortedMap resultMap = XmlUtils.parseXmlStr(info.toString());
        //商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致，防止数据泄漏导致出现“假通知”，造成资金损失。
//        Boolean validateCallback = PayCommonUtil.isTenpaySign("utf-8", resultMap, super.getApiKey());
//        System.out.println("验证返回签名结果：" + validateCallback);

        System.out.println("微信回调request参数：：：：" + resultMap.toString());
        return resultMap;
    }

    /*
   *@Author Gavin
   *@Description 保存交易记录
   *@Date 2017/2/27 10:25
   *@Email gavinsjq@sina.com
   *@Params
   */
    private void saveTradingRecord(SortedMap map) {
        String serialNum = (String) map.get("transaction_id");
        String attach = map.get("attach").toString();
        String [] atta = attach.split("_");
        String username = atta[0];
        String orderType = atta[1];;
        String orderNo = (String) map.get("out_trade_no");
        String dealSum = (String) map.get("total_fee");
        String openid = (String) map.get("openid");

        TransactionHistory old = transactionService.findBySerialNum(serialNum);
        if (old == null) {
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setSerialNum(serialNum);
            transactionHistory.setOrderNo(orderNo);
            transactionHistory.setPayWay(Payment.WeChat);
            transactionHistory.setDealSum(Double.parseDouble(dealSum) / 100);
            transactionHistory.setType(1);
            transactionHistory.setUserName(username);
            transactionHistory.setCustomParam1(openid);
            transactionService.save(transactionHistory);
            //回调订单接口，通知支付成功
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("orderNo",orderNo);
            paramsMap.put("paymentType",Payment.WeChat);
            if(orderType.equals("order")){
                Notify.toOrderService(paramsMap,Payment.WeChat.name());
            }else {
                Notify.toUserService(paramsMap);
            }
        }
    }
}
