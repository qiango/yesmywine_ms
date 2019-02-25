package com.yesmywine.pay.unionpay.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author louxueming
 *         updateTime 2016-10-14
 *         支付接口相关配置
 */
public class Config {


    public static final String SSO_URL = "http://www.sphrss.net:8083/newPlatform/newUser/ssoUpdateIntegral.action";

    /**
     * token  支付平台评证  可增加
     *
     * @param token 唯一
     * @return
     */
    public static String queryToken(String token) {

        Map<String, String> tokenMap = new HashMap<String, String>();
        //知识库支付token
        tokenMap.put("402881e857a81db80157a82002ea0000", "ZSKTOKEN");
        //远程教育平台token
        tokenMap.put("402881e857a81db80157a87215d50002", "DFTOKEN");
        //信息咨询管理token
        tokenMap.put("402881e857b39bc40157b3ac148c0005", "XXZXTOKEN");

        return tokenMap.get(token);
    }

    public static String queryBackUrl(String token) {
        Map<String, String> tokenMap = new HashMap<String, String>();
        //知识库支付token
        //	tokenMap.put("402881e857a81db80157a82002ea0000", "http://zsk.sphrss.net/Knowledge/admin/back/");
        tokenMap.put("402881e857a81db80157a82002ea0000", "http://127.0.0.1:7003/Knowledge/pay/");
        //远程教育平台token
        tokenMap.put("402881e857a81db80157a87215d50002", "http://http://xxzx.sphrss.net/infoConsult/");
        //信息咨询管理token
        tokenMap.put("402881e857b39bc40157b3ac148c0005", "http://xxzx.sphrss.com//infoConsult/account/");

        return tokenMap.get(token);
    }


}
