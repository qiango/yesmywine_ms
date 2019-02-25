package com.yesmywine.pay.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by SJQ on 2017/2/17.
 */
public class PayCommonUtil {
    public static Logger log = LoggerFactory.getLogger(PayCommonUtil.class);

    /**
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     *
     * @return boolean
     */
    public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }

        sb.append("key=" + API_KEY);

        //算出摘要
        String mysign = MD5.MD5Encode(sb.toString()).toLowerCase();
        System.out.println(packageParams.get("sign"));
        String tenpaySign = ((String) packageParams.get("sign")).toLowerCase();

        //System.out.println(tenpaySign + "    " + mysign);
        return tenpaySign.equals(mysign);
    }

    /**
     * @param characterEncoding 编码格式
     * @return
     * @author
     * @date 2016-4-22
     * @Description：sign签名
     * @params parameters
     * 请求参数
     */
    public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = "";
            try {
                v = (String) entry.getValue();
            } catch (Exception e) {
                // TODO: handle exception
                v = entry.getValue() + "";
            }

            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);

        System.out.println("ASCII字典序排序::::" + sb.toString());
        String sign = MD5.MD5Encode(sb.toString()).toUpperCase();
        return sign;
    }

    /**
     * @param parameters 请求参数
     * @return
     * @author
     * @date 2016-4-22
     * @Description：将请求参数转换为xml格式的string
     */
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = "";
            try {
                v = (String) entry.getValue();
            } catch (Exception e) {
                // TODO: handle exception
                v = entry.getValue() + "";
            }

            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 取出一个指定长度大小的随机正整数.
     *
     * @param length int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * 获取当前时间 yyyyMMddHHmmss
     *
     * @return String
     */
    public static String getCurrTime() {
        Date now = new Date();
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = outFormat.format(now);
        return s;
    }


    public static JSONObject httpsRequestToJsonObject(String requestUrl,
                                                      String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            StringBuffer buffer = httpsRequest(requestUrl, requestMethod,
                    outputStr);
            jsonObject = JSON.parseObject(buffer.toString());

        } catch (ConnectException ce) {
            log.error("连接超时：" + ce.getMessage());
        } catch (Exception e) {
            log.error("https请求异常：" + e.getMessage());
        }
        return jsonObject;
    }

    private static StringBuffer httpsRequest(String requestUrl,
                                             String requestMethod, String output)
            throws NoSuchAlgorithmException, NoSuchProviderException,
            KeyManagementException, MalformedURLException, IOException,
            ProtocolException, UnsupportedEncodingException {
        URL url = new URL(requestUrl);
        HttpsURLConnection connection = (HttpsURLConnection) url
                .openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod(requestMethod);
        if (null != output) {
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(output.getBytes("UTF-8"));
            outputStream.close();
        }
        // 从输入流读取返回内容
        InputStream inputStream = connection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream, "utf-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str = null;
        StringBuffer buffer = new StringBuffer();
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
        inputStream = null;
        connection.disconnect();
        return buffer;
    }


    public static void main(String[] args) {
        String test = "appid=wx29983132bd04dca9&body=order pay&mch_id=1436620702&nonce_str=1537078166&notify_url=http://88.88.88.211:8095/backUrl/wechatPay&out_trade_no=20170220153650&product_id=20170220153650&spbill_create_ip=123.12.12.123&time_expire=20170220153707&time_start=20170220153707&total_fee=1&trade_type=NATIVE&key=1436620702";
        System.out.println(MD5.MD5Encode(test));

    }
}
