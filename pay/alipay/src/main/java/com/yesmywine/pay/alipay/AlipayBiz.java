package com.yesmywine.pay.alipay;

import com.yesmywine.pay.alipay.util.AlipaySubmit;
import com.yesmywine.pay.alipay.util.UtilDate;
import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.bean.PaymentResult;
import com.yesmywine.pay.service.PayFactory;
import com.yesmywine.pay.service.PaymentBiz;
import com.yesmywine.pay.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/1/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Service
public class AlipayBiz extends PayFactory implements PaymentBiz {

    @Autowired
    private TransactionService transactionService;

    @Override
    public PaymentResult pay(String orderNumber, Double amount, String title, String description, String username, HttpServletRequest request) {
//支付类型

        try {
            String payment_type = "1";
            //必填，不能修改
            //需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/
            //商户订单号
            String out_trade_no = new String(orderNumber.getBytes("ISO-8859-1"), "UTF-8");
            //商户网站订单系统中唯一订单号，必填
            //订单名称
            String subject = alipaySetting.getBody();
            //必填
            //付款金额
            String total_fee = new String((amount + "").getBytes("ISO-8859-1"), "UTF-8");
            //必填
            //需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html
            //防钓鱼时间戳
            String anti_phishing_key = "";
            //若要使用请调用类文件submit中的query_timestamp函数
            //客户端的IP地址
            String exter_invoke_ip = "";
            //非局域网的外网IP地址，如：221.0.0.1


            //////////////////////////////////////////////////////////////////////////////////

            //把请求参数打包成数组
            Map<String, String> sParaTemp = new HashMap<String, String>();
            sParaTemp.put("service", "create_direct_pay_by_user");
            sParaTemp.put("partner", alipaySetting.getPartner());
            sParaTemp.put("seller_email", alipaySetting.getSellerEmail());
            sParaTemp.put("_input_charset", alipaySetting.getInput_charset());
            sParaTemp.put("payment_type", payment_type);

            String params = "?username=" + username +"&orderType="+title;
            sParaTemp.put("notify_url", alipaySetting.getNotifyUrl() + params);
            sParaTemp.put("return_url", alipaySetting.getReturnUrl());
            sParaTemp.put("body", alipaySetting.getBody());
            sParaTemp.put("out_trade_no", out_trade_no);
            sParaTemp.put("subject", subject);
            sParaTemp.put("total_fee", total_fee);
            sParaTemp.put("show_url", "test");
            sParaTemp.put("anti_phishing_key", anti_phishing_key);
            sParaTemp.put("exter_invoke_ip", exter_invoke_ip);

            //建立请求
            String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
            PaymentResult result = new PaymentResult();
            result.setCode("SUCCESS");
            result.setData(sHtmlText.replaceAll("\"", "'"));
            result.setPayment(Payment.Alipay);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //查询
    @Override
    public PaymentResult queryDetail(String orderNumber) {
        Map<String, String> sParaTemp = new HashMap<>();
        PaymentResult paymentResult = new PaymentResult();
        sParaTemp.put("service", "single_trade_query");
        sParaTemp.put("partner", alipaySetting.getPartner());
        sParaTemp.put("_input_charset", alipaySetting.getInput_charset());
        sParaTemp.put("out_trade_no", orderNumber);
        try {
            String sHtmlText = AlipaySubmit.buildRequest("", "", sParaTemp);
            String trade_no = sHtmlText.substring(sHtmlText.indexOf("<trade_no>") + 10, sHtmlText.indexOf("</trade_no>"));
            System.out.println("trade_no:" + trade_no);
            paymentResult.setData(trade_no);
            paymentResult.setPayment(Payment.Alipay);
            paymentResult.setCode("success");
            return paymentResult;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    //退款
    @Override
    public PaymentResult refund(String orderNumber, String refundNumber, Double payAmount, Double refundAmount, String title, String description) {
        PaymentResult number = this.queryDetail(orderNumber);
        String querynumber = number.getData();
        Map<String, String> sParaTemp = new HashMap<>();
        sParaTemp.put("service", "refund_fastpay_by_platform_nopwd");
        sParaTemp.put("partner", alipaySetting.getPartner());
        sParaTemp.put("_input_charset", alipaySetting.getInput_charset());
        String params = "?returnNo=" + refundNumber;
        sParaTemp.put("notify_url", alipaySetting.getRefundBackUrl()+params);
        sParaTemp.put("batch_no", UtilDate.getDate() + UtilDate.getThree());
        sParaTemp.put("refund_date", UtilDate.getDateFormatter());
        sParaTemp.put("batch_num", "1");
        sParaTemp.put("detail_data", querynumber + "^" + refundAmount + "^正常退款");
        try {
            String sHtmlText = AlipaySubmit.buildRequest("", "", sParaTemp);
            System.out.println("退款返回值==>" + sHtmlText);

        } catch (Exception e) {
            e.printStackTrace();
        }
        number.setCode("SUCCESS");
        number.setPayment(Payment.Alipay);
        return number;
    }


    @Override
    public PaymentResult appPay(String orderNumber, Double amount, String title, String description, String userId, HttpServletRequest request) {
        return null;
    }
}
