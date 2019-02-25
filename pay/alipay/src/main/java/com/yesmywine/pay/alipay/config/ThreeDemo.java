package com.yesmywine.pay.alipay.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hz on 7/17/17.
 */
public class ThreeDemo {

    public void get(HttpServletRequest request) throws Exception {


         //获取支付宝GET过来反馈信息
 Map<String,String> params = new HashMap<String,String>();

        Map requestParams = request.getParameterMap();

        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();

            String[] values = (String[]) requestParams.get(name);

            String valueStr = "";

            for (int i = 0; i < values.length; i++) {

                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        :valueStr + values[i] + ",";

            }

            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化

            try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            params.put(name, valueStr);

        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
//支付宝用户号

        String app_id = new String("".getBytes("ISO-8859-1"),"UTF-8");


//获取用户信息授权
String auth_user = new String(request.getParameter("scope").getBytes("ISO-8859-1"),"UTF-8");

//获的第三方登录用户授权auth_code

        String auth_code = new String(request.getParameter("auth_code").getBytes("ISO-8859-1"),"UTF-8");

        String privateKey = "私钥";

        String publicKey = "支付宝公钥";

        //使用auth_code换取接口access_token及用户userId

AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","应用APPID",privateKey,"json","UTF-8",publicKey,"RSA2");//正常环境下的网关
//AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do","沙箱环境先的应用APPID",privateKey,"json","UTF-8",publicKey,"RSA2");//沙箱下的网关
//获取用户信息授权
AlipaySystemOauthTokenRequest requestLogin2 = new AlipaySystemOauthTokenRequest();
requestLogin2.setCode(auth_code);
requestLogin2.setGrantType("authorization_code");
        AlipaySystemOauthTokenResponse oauthTokenResponse = null;
        try {
            oauthTokenResponse = alipayClient.execute(requestLogin2);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
//    out.write("<br/>AccessToken:"+oauthTokenResponse.getAccessToken() + "\n");
//调用接口获取用户信息
        AlipayClient alipayClientUser = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", "2016073100131450", privateKey, "json", "UTF-8", publicKey, "RSA2");
        AlipayUserInfoShareRequest requestUser = new AlipayUserInfoShareRequest();
        try {
        AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(requestUser, oauthTokenResponse.getAccessToken());
        //out.write("<br/>UserId:" + userinfoShareResponse.getUserId() + "\n");//用户支付宝ID
        //out.write("UserType:" + userinfoShareResponse.getUserType() + "\n");//用户类型
        //out.write("UserStatus:" + userinfoShareResponse.getUserStatus() + "\n");//用户账户动态
        //out.write("Email:" + userinfoShareResponse.getEmail() + "\n");//用户Email地址
        //out.write("IsCertified:" + userinfoShareResponse.getIsCertified() + "\n");//用户是否进行身份认证
        //out.write("IsStudentCertified:" + userinfoShareResponse.getIsStudentCertified() + "\n");//用户是否进行学生认证
        } catch (AlipayApiException e) {
        //处理异常
        e.printStackTrace();
        }
    }
}
