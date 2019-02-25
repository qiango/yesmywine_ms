package com.yesmywine.pay.wechat;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.bean.PaymentResult;
import com.yesmywine.pay.service.PayFactory;
import com.yesmywine.pay.service.PaymentBiz;
import com.yesmywine.pay.service.TransactionService;
import com.yesmywine.pay.entity.TransactionHistory;
import com.yesmywine.pay.wechat.utils.HttpRequest;
import com.yesmywine.pay.wechat.utils.PayCommonUtil;
import com.yesmywine.pay.wechat.utils.WechatUtils;
import com.yesmywine.pay.wechat.utils.XmlUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.date.DateUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by WANG, RUIQING on 12/1/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Service
public class WeChatPay extends PayFactory implements PaymentBiz {
    private static final Logger log = LoggerFactory.getLogger(WeChatPay.class);

    @Autowired
    private TransactionService transactionService;

    @Override
    public PaymentResult queryDetail(String orderNumber) {
        String APPID = weChatPaySetting.getAppId();//AppID(应用ID)
        String MCHID = weChatPaySetting.getMchId();
        String KEY = weChatPaySetting.getApiKey();  //API密钥

        try {
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            //商户信息
            packageParams.put("appid", APPID);      //公众账号ID
            packageParams.put("mch_id", MCHID);    //商户号
            packageParams.put("nonce_str", WechatUtils.getNonce_str());  //随机字符串
            //通知地址  异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
            packageParams.put("out_trade_no", orderNumber);        //商户订单号
            //生成签名字符串
            String sign = PayCommonUtil.createSign("UTF-8", packageParams, KEY);
            packageParams.put("sign", sign);    //签名
            String requestXML = PayCommonUtil.getRequestXml(packageParams);
            log.info("支付请求：" + requestXML);
            long startTime = System.currentTimeMillis();

            String resXml = HttpRequest.post(
                    "https://api.mch.weixin.qq.com/pay/orderquery",
                    requestXML);
            log.info("请求结果：" + resXml);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    /*
    *@Author Gavin
    *@Description   PC支付
    *@Date 2017/2/21 17:39
    *@Email gavinsjq@sina.com
    *@Params
    */
    public PaymentResult pay(String orderNumber, Double amount, String title, String description, String username, HttpServletRequest request) {
        String APPID = weChatPaySetting.getAppId();//AppID(应用ID)
        String MCHID = weChatPaySetting.getMchId();
        String KEY = weChatPaySetting.getApiKey();  //API密钥
        String BODY = weChatPaySetting.getBody();
        String notify_url = weChatPaySetting.getNotifyUrl();   //回调地址。测试回调必须保证外网能访问到此地址

        JSONObject retJson = new JSONObject();
        try {
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            //商户信息
            packageParams.put("appid", APPID);      //公众账号ID
            packageParams.put("mch_id", MCHID);     //商户号
            packageParams.put("nonce_str", WechatUtils.getNonce_str());      //随机字符串
            packageParams.put("body", BODY);        //商品描述  eg:腾讯充值中心-QQ会员充值
            packageParams.put("trade_type", "NATIVE");      //交易类型  JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
            packageParams.put("spbill_create_ip", WechatUtils.getIpAddr(request));     //ip地址

            //支付信息
            packageParams.put("notify_url", notify_url);        //通知地址  异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
            packageParams.put("out_trade_no", orderNumber);        //商户订单号
            packageParams.put("total_fee", String.valueOf((long) (amount * 100)));        //标价金额  订单总金额，单位为分，详见支付金额
            //MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
            packageParams.put("product_id", orderNumber);        //商品ID trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
            Calendar nowTime = Calendar.getInstance();
            packageParams.put("time_start", DateUtil.toString(      //交易起始时间  订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。
                    nowTime.getTime(), "yyyyMMddHHmmss"));
//            packageParams.put("time_expire", DateUtil.toString(     //交易结束时间  订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
//                    nowTime.getTime(), "yyyyMMddHHmmss"));
            packageParams.put("attach",username+"_"+title);

            //生成签名字符串
            String sign = PayCommonUtil.createSign("UTF-8", packageParams, KEY);
            packageParams.put("sign", sign);    //签名

            String requestXML = PayCommonUtil.getRequestXml(packageParams);
            log.info("支付请求：" + requestXML);
            long startTime = System.currentTimeMillis();

            String resXml = HttpRequest.post(
                    "https://api.mch.weixin.qq.com/pay/unifiedorder",
                    new String(requestXML.getBytes("UTF-8"), "ISO-8859-1"));
//                    requestXML);
            log.info("请求结果：" + resXml);

            long endTime = System.currentTimeMillis();

            Integer execute_time = (int) ((endTime - startTime) / 1000);
            SortedMap resultMap = XmlUtils.parseXmlStr(resXml);
            Boolean validateCallback = PayCommonUtil.isTenpaySign("utf-8", resultMap, KEY);
            log.info("验证返回签名结果：" + validateCallback);
            if (!validateCallback) {
                ValueUtil.isError("验证返回签名结果失败");
            }

            if (resultMap.get("result_code").toString().equals("SUCCESS") && resultMap.get("result_code").toString().equals("SUCCESS")) {

                resultMap.putAll(packageParams);
                resultMap.put("placeOrderTime", nowTime.getTime());
                resultMap.put("applicant", username);

                String urlCode = (String) resultMap.get("code_url"); //微信二维码短链接
                PaymentResult result = new PaymentResult();
                result.setCode("SUCCESS");
                result.setPayment(Payment.WeChat);
                result.setData(urlCode);
                result.setOrderNo(orderNumber);
                return result;
            } else {
                String msg = resultMap.get("err_code_des").toString();
                ValueUtil.isError(msg);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

//    @Override
//    public PaymentResult refund(String orderNumber,Double payAmount,Double refundAmount, String title, String description) {
//        return null;
//    }


    /*
    *@Author Gavin
    *@Description 退款
    *@Date 2017/2/21 17:39
    *@Email gavinsjq@sina.com
    *@Params
    */
//    @Override
//    public PaymentResult refund(String orderNumber,String refundNumber, Double refundAmount, String title, String description) {
    public PaymentResult refund(String orderNumber, String refundNumber, Double payAmount, Double refundAmount, String title, String description) {
        String APPID = weChatPaySetting.getAppId();//AppID(应用ID)
        String MCHID = weChatPaySetting.getMchId();
        String KEY = weChatPaySetting.getApiKey();  //API密钥
        try {
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
//            商户信息（固定）
            packageParams.put("appid", APPID);      //公众账号ID
            packageParams.put("mch_id", MCHID);     //商户号
            packageParams.put("nonce_str", WechatUtils.getNonce_str());      //随机字符串

            //需要传递的参数
            packageParams.put("out_trade_no", orderNumber);        //商户订单号
            packageParams.put("out_refund_no", "TK" + orderNumber);     //商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
            packageParams.put("total_fee", String.valueOf((long) (refundAmount * 100)));        //标价金额  订单总金额，单位为分，详见支付金额
            packageParams.put("refund_fee", String.valueOf((long) (refundAmount * 100)));        //退款金额
            packageParams.put("op_user_id", MCHID);//操作人员,默认为商户账号

            //生成签名字符串
            String sign = PayCommonUtil.createSign("UTF-8", packageParams, KEY);
            packageParams.put("sign", sign);    //签名

            String requestXML = PayCommonUtil.getRequestXml(packageParams);

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File("/opt/apiclient_cert.p12"));//放退款证书的路径
            try {
                keyStore.load(instream, MCHID.toCharArray());
            } finally {
                instream.close();
            }

            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, MCHID.toCharArray()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            log.info("退款请求：" + requestXML);
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            try {

                HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");//退款接口

                log.info("executing request" + httpPost.getRequestLine());
                StringEntity reqEntity = new StringEntity(requestXML);
                // 设置类型
                reqEntity.setContentType("application/x-www-form-urlencoded");
                httpPost.setEntity(reqEntity);
                CloseableHttpResponse chr = httpclient.execute(httpPost);
                try {
                    HttpEntity entity = chr.getEntity();

                    System.out.println("----------------------------------------");
                    System.out.println(chr.getStatusLine());
                    StringBuffer rspXml = new StringBuffer();
                    if (entity != null) {
                        System.out.println("Response content length: " + entity.getContentLength());
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                        String text;
                        while ((text = bufferedReader.readLine()) != null) {
                            rspXml.append(text);
                        }

                    }
                    log.info("退款响应：" + rspXml.toString());
                    SortedMap map = XmlUtils.parseXmlStr(rspXml.toString());
                    EntityUtils.consume(entity);
                    PaymentResult result = new PaymentResult();
                    result.setPayment(Payment.WeChat);
                    if (map.get("result_code").equals("SUCCESS")) {
                        //如果成功，修改交易状态
                        updateTradingRecord(packageParams,refundNumber);
                        result.setCode("SUCCESS");
                        return result;
                    } else {
                        result.setCode("FAIL");
                        result.setData(map.get("err_code_des").toString());
                        return result;
                    }

                } finally {
                    chr.close();
                }
            } finally {
                httpclient.close();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return null;
    }

    /*
    *@Author Gavin
    *@Description  APP支付
    *@Date 2017/6/19 16:58
    *@Email gavinsjq@sina.com
    *@Params
    */
    @Override
    public PaymentResult appPay(String orderNumber, Double amount, String title, String description, String username, HttpServletRequest request) {
        String APPID = weChatPaySetting.getAppId();//AppID(应用ID)
        String MCHID = weChatPaySetting.getMchId();
        String KEY = weChatPaySetting.getApiKey();  //API密钥
        String BODY = weChatPaySetting.getBody();
        String notify_url = weChatPaySetting.getNotifyUrl();   //回调地址。测试回调必须保证外网能访问到此地址

        JSONObject retJson = new JSONObject();
        try {
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            //商户信息
            packageParams.put("appid", APPID);      //公众账号ID
            packageParams.put("mch_id", MCHID);     //商户号
            packageParams.put("nonce_str", WechatUtils.getNonce_str());      //随机字符串
            packageParams.put("body", BODY);        //商品描述  eg:腾讯充值中心-QQ会员充值
            packageParams.put("trade_type", "APP");      //交易类型  JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
            packageParams.put("spbill_create_ip", WechatUtils.getIpAddr(request));     //ip地址

            //支付信息
            packageParams.put("notify_url", notify_url);        //通知地址  异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
            packageParams.put("out_trade_no", orderNumber);        //商户订单号
            packageParams.put("total_fee", String.valueOf((long) (amount * 100)));        //标价金额  订单总金额，单位为分，详见支付金额
            //MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
            packageParams.put("product_id", orderNumber);        //商品ID trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
            Calendar nowTime = Calendar.getInstance();
            packageParams.put("time_start", DateUtil.toString(      //交易起始时间  订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。
                    nowTime.getTime(), "yyyyMMddHHmmss"));
//            packageParams.put("time_expire", DateUtil.toString(     //交易结束时间  订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
//                    nowTime.getTime(), "yyyyMMddHHmmss"));
            packageParams.put("attach",username+"_"+title);

            //生成签名字符串
            String sign = PayCommonUtil.createSign("UTF-8", packageParams, KEY);
            packageParams.put("sign", sign);    //签名

            String requestXML = PayCommonUtil.getRequestXml(packageParams);
            log.info("支付请求：" + requestXML);
            long startTime = System.currentTimeMillis();

            String resXml = HttpRequest.post(
                    "https://api.mch.weixin.qq.com/pay/unifiedorder",
                    new String(requestXML.getBytes("UTF-8"), "ISO-8859-1"));
//                    requestXML);
            log.info("请求结果：" + resXml);

            long endTime = System.currentTimeMillis();

            Integer execute_time = (int) ((endTime - startTime) / 1000);
            SortedMap resultMap = XmlUtils.parseXmlStr(resXml);
            Boolean validateCallback = PayCommonUtil.isTenpaySign("utf-8", resultMap, KEY);
            log.info("验证返回签名结果：" + validateCallback);
            if (!validateCallback) {
                PaymentResult result = new PaymentResult();
                result.setCode("FAIL");
                result.setPayment(Payment.WeChat);
                return result;
            }

            if (resultMap.get("result_code").toString().equals("SUCCESS") && resultMap.get("result_code").toString().equals("SUCCESS")) {

                resultMap.putAll(packageParams);
                resultMap.put("placeOrderTime", nowTime.getTime());
                resultMap.put("applicant", username);

                String urlCode = (String) resultMap.get("code_url"); //微信二维码短链接
                PaymentResult result = new PaymentResult();
                result.setCode("SUCCESS");
                result.setPayment(Payment.WeChat);
                result.setData(urlCode);
                return result;
            } else {
                String msg = resultMap.get("err_code_des").toString();
                PaymentResult result = new PaymentResult();
                result.setCode("FAIL");
                result.setPayment(Payment.WeChat);
                result.setData(msg);
                return result;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    private void updateTradingRecord(SortedMap map,String returnNo) {
        String SerialNum = (String) map.get("transaction_id");//订单号
        String refundSum = (String) map.get("refund_fee");//退款金额
        TransactionHistory transactionHistory = transactionService.findBySerialNum(SerialNum);
        TransactionHistory oldHistory = transactionService.findByReturnNo(returnNo);
        if(oldHistory==null) {
            TransactionHistory returnHistory = new TransactionHistory();
            returnHistory.setRefundSum(Double.valueOf(refundSum));
            returnHistory.setRefundTime(new Date());
            returnHistory.setReturnNo(returnNo);
            returnHistory.setOrderNo(transactionHistory.getOrderNo());
            returnHistory.setSerialNum(transactionHistory.getSerialNum());
            returnHistory.setPayWay(transactionHistory.getPayWay());
            returnHistory.setType(2);
            transactionService.save(returnHistory);
        }
    }
}
