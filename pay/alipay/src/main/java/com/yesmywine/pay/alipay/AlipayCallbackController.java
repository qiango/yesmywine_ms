package com.yesmywine.pay.alipay;

import com.yesmywine.pay.bean.Notify;
import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.service.TransactionService;
import com.yesmywine.pay.entity.TransactionHistory;
import com.yesmywine.util.basic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by SJQ on 2017/5/4.
 */
@RestController
@RequestMapping("/pay/back/alipay")
public class AlipayCallbackController {
    @Autowired
    private TransactionService transactionService;

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/8/14
    *@Param
    *@Description:订单支付成功回调接口
    */
    @RequestMapping
    public String paySuccessCallBack(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("下单：支付宝支付成功，开始接收返回值");
        Map<String, String> result = getAllRequestParam(request);
        System.out.println("返回值为==> " + result.toString());
        System.out.println("保存交易记录");
        saveTradingRecord(result);
        return null;
    }

    @RequestMapping("/refund")
    public String refundSuccessCallBack(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("支付宝退款成功，开始接收返回值");
        Map<String, String> result = getAllRequestParam(request);
        System.out.println("返回值为==> " + result.toString());
        System.out.println("修改交易记录");
        udateTradingRecord(result);
        return null;
    }

    private void udateTradingRecord(Map<String, String> result) {
        String returnNo = result.get("returnNo");
        String result_details = result.get("result_details");
        String[] strings = result_details.replace("^","-").split("-");
        if(strings[2].equals("SUCCESS")){
            TransactionHistory transactionHistory = transactionService.findBySerialNum(strings[0]);
            TransactionHistory oldHistory = transactionService.findByReturnNo(returnNo);
            if(oldHistory==null){
                TransactionHistory newHistory = new TransactionHistory();
                newHistory.setReturnNo(returnNo);
                newHistory.setRefundTime(new Date());
                newHistory.setRefundSum(Double.valueOf(strings[1]));
                newHistory.setOrderNo(transactionHistory.getOrderNo());
                newHistory.setSerialNum(transactionHistory.getSerialNum());
                newHistory.setPayWay(transactionHistory.getPayWay());
                newHistory.setType(2);
                transactionService.save(newHistory);
            }
        }
    }

    public static Map<String, String> getAllRequestParam(
            final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                System.out.println("en==>" + en + "    value==>" + value);
                res.put(en, value);
                // 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if (res.get(en) == null || "".equals(res.get(en))) {
                    // System.out.println("======为空的字段名===="+en);
                    res.remove(en);
                }
            }
        }
        System.out.println("支付宝回调request参数：：：：" + res.toString());
        return res;
    }

    /*
   *@Author Gavin
   *@Description 保存交易记录
   *@Date 2017/2/27 10:25
   *@Email gavinsjq@sina.com
   *@Params
   */
    private void saveTradingRecord(Map<String, String> map) {
        String serialNum = map.get("trade_no");
        String username = map.get("username");
        String orderType = map.get("orderType");
        String orderNo = map.get("out_trade_no");
        String dealSum = map.get("total_fee");
        String detail_data = map.get("detail_data");
        TransactionHistory old = transactionService.findBySerialNum(serialNum);
        if (old == null) {
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setSerialNum(serialNum);
            transactionHistory.setUserName(username);
            transactionHistory.setOrderNo(orderNo);
            transactionHistory.setPayWay(Payment.Alipay);
            transactionHistory.setDealSum(Double.parseDouble(dealSum));
            transactionHistory.setType(1);
            transactionService.save(transactionHistory);
            //回调订单接口，通知支付成功
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("orderNo",orderNo);
            paramsMap.put("paymentType",Payment.Alipay);
            if(orderType.equals("order")){
                Notify.toOrderService(paramsMap,Payment.Alipay.name());
            }else {
                Notify.toUserService(paramsMap);
            }
        }
    }

}
