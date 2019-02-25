/**
 * Licensed Property to China UnionPay Co., Ltd.
 * <p>
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 * All Rights Reserved.
 * <p>
 * <p>
 * Modification History:
 * =============================================================================
 * Author         Date          Description
 * ------------ ---------- ---------------------------------------------------
 * xshu       2014-05-28       日志打印工具类
 * =============================================================================
 */
package com.yesmywine.pay.unionpay.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LogUtil {

    private final static Logger GATELOG = LoggerFactory.getLogger("ACP_SDK_LOG");
    private final static Logger GATELOG_ERROR = LoggerFactory.getLogger("SDK_ERR_LOG");
    private final static Logger GATELOG_MESSAGE = LoggerFactory.getLogger("SDK_MSG_LOG");
    private final static Logger INTERFACE_MESSAGE = LoggerFactory.getLogger("INTERFACE_LOG");

    final static String LOG_STRING_REQ_MSG_BEGIN = "============================== SDK REQ MSG BEGIN ==============================";
    final static String LOG_STRING_REQ_MSG_END = "==============================  SDK REQ MSG END  ==============================";
    final static String LOG_STRING_RSP_MSG_BEGIN = "============================== SDK RSP MSG BEGIN ==============================";
    final static String LOG_STRING_RSP_MSG_END = "==============================  SDK RSP MSG END  ==============================";
    final static String LOG_INTERFACE_MSG_BEGIN = "==============================  INTGERFACE BEGIN  ==============================";
    final static String LOG_INTERFACE_MSG_END = "==============================  INTGERFACE END  ==============================";

    /**
     * 记录普通日志
     *
     * @param cont
     */
    public static void writeLog(String cont) {
        GATELOG.info(cont);
    }

    /**
     * 记录ERORR日志
     *
     * @param cont
     */
    public static void writeErrorLog(String cont) {
        GATELOG_ERROR.error(cont);
    }

    /**
     * 记录ERROR日志
     *
     * @param cont
     * @param ex
     */
    public static void writeErrorLog(String cont, Throwable ex) {
        GATELOG_ERROR.error(cont, ex);
    }

    /**
     * 记录通信报文
     *
     * @param msg
     */
    public static void writePayMessage(String msg) {
        GATELOG_MESSAGE.info(msg);
    }

    /**
     * 打印支付请求报文
     *
     * @param reqParam
     */
    public static void printPayRequestLog(Map<String, String> reqParam) {
        writePayMessage(LOG_STRING_REQ_MSG_BEGIN);
        Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            writePayMessage("[" + en.getKey() + "] = [" + en.getValue() + "]");
        }
        writePayMessage(LOG_STRING_REQ_MSG_END);
    }

    /**
     * 记录支付通信报文
     *
     * @param msg
     */
    public static void writeInterfaceMessage(String msg) {
        INTERFACE_MESSAGE.info(msg);
    }

    /**
     * 打印接口请求报文
     *
     * @param reqParam
     */
    public static void printInterfaceRequestLog(Map<String, String> reqParam) {
        writeInterfaceMessage(LOG_INTERFACE_MSG_BEGIN);
        Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            writeInterfaceMessage(en.getKey() + "=" + en.getValue());
        }
        writeInterfaceMessage(LOG_INTERFACE_MSG_END);
    }

    /**
     * 打印响应报文.
     *
     * @param res
     */
    public static void printResponseLog(String res) {
        writePayMessage(LOG_STRING_RSP_MSG_BEGIN);
        writePayMessage(res);
        writePayMessage(LOG_STRING_RSP_MSG_END);
    }

    /**
     * debug方法
     *
     * @param cont
     */
    public static void debug(String cont) {
        if (GATELOG.isDebugEnabled()) {
            GATELOG.debug(cont);
        }
    }

    /**
     * 打印请求报文
     *
     * @param reqParam
     */
    public static void printRequestLog(Map<String, String> reqParam) {
        writeMessage(LOG_STRING_REQ_MSG_BEGIN);
        Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> en = it.next();
            writeMessage("[" + en.getKey() + "] = [" + en.getValue() + "]");
        }
        writeMessage(LOG_STRING_REQ_MSG_END);
    }

    /**
     * 记录通信报文
     *
     * @param msg
     */
    public static void writeMessage(String msg) {
        GATELOG_MESSAGE.info(msg);
    }
}
