package com.yesmywine.pay.unionpay.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.pay.bean.Notify;
import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.service.TransactionService;
import com.yesmywine.pay.entity.TransactionHistory;
import com.yesmywine.pay.unionpay.entity.UnionPay;
import com.yesmywine.pay.unionpay.util.Utils;
import com.yesmywine.pay.unionpay.sdk.*;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * @author SJQ
 * @version 2016-08-24
 *          支付回调
 */
@RestController
@RequestMapping("/pay/back/unionpay")
public class BackUrlController {

    private static final Logger log = LoggerFactory
            .getLogger(BackUrlController.class);

    private static final java.lang.String TIP = "支付回调: ";

    private static final int String = 0;

    private static final int HashMap = 0;
    @Autowired
    private TransactionService transactionService;

    /**
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     * @author louxueming 2016-11-24
     * @version v1.0
     * 银联回调 前台回调  此代码中不做业务逻辑处理，所有业务逻辑处理均在银联回调后台处理
     */
    @RequestMapping("/frontUrl")
    protected void unionpayFrontUrl(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;
        out = resp.getWriter();
        //前台返回路径
        java.lang.String frontUrl = "http://localhost:8081/unionPay";
        //文献ID 通过回调地址所传
        java.lang.String orderId = req.getParameter("orderId");
        try {
            JSONObject json = new JSONObject();
            //返回参数
            Map<java.lang.String, java.lang.String> paramMap = new HashMap<java.lang.String, java.lang.String>();
            Map<java.lang.String, java.lang.String> respParam = getAllRequestParam(req);
            LogUtil.writeLog("BackUrlRcvResponse前台接收报文返回开始");

            java.lang.String encoding = req.getParameter(SDKConstants.param_encoding);

            // 打印请求报文
            LogUtil.printPayRequestLog(respParam);
            //将响应数据转换为实体
            UnionPay unionPay = Utils.toBean(UnionPay.class, respParam);

            String userId = respParam.get("userId");

            LogUtil.writeLog("返回报文中encoding=[" + encoding + "]");
            java.lang.String pageResult = "";
            //支付成功
            if (unionPay.getRespCode().equals("00")) {
                json.put("respMsg", "支付成功");
//                saveTradingRecord(unionPay, Integer.valueOf(userId));
            } else {
                json.put("respMsg", "支付失败");
            }

            java.lang.String html = AcpService.createAutoFormHtml(frontUrl, respParam, UnionPayBase.encoding_UTF8);
            LogUtil.writeLog(html);
            out.write(html);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //返回参数
            Map<java.lang.String, java.lang.String> respParam = new HashMap<java.lang.String, java.lang.String>();
            JSONObject json = new JSONObject();
            try {
                json.put("success", "0");
                json.put("respMsg", "支付失败");
                respParam.put("jsonStr", json.toString());
                java.lang.String html = AcpService.createAutoFormHtml(frontUrl, respParam, UnionPayBase.encoding_UTF8);
                LogUtil.writeLog(html);
                out.write(html);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }

    }



    /**
     * 银联回调后台回调
     *
     * @param req
     * @param resp
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping("/backUrl")
    protected void unionpayBackUrl(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setHeader("Content-type", "text/html;charset=UTF-8");
        JSONObject json = new JSONObject();
        //返回参数
        Map<java.lang.String, java.lang.String> paramMap = new HashMap<java.lang.String, java.lang.String>();
        PrintWriter out = null;
        out = resp.getWriter();
        java.lang.String integral = "-1";
        int txnAmt = 0;
        //前台返回路径
        java.lang.String frontUrl = "";
        //后台返回路径
        java.lang.String backUrl = "";
        //文献ID 通过回调地址所传
        java.lang.String contentId = req.getParameter("orderId");
        try {
            LogUtil.writeLog("BackUrlRcvResponse后台接收报文返回开始");
            java.lang.String encoding = req.getParameter(SDKConstants.param_encoding);
            LogUtil.writeLog("返回报文中encoding=[" + encoding + "]");
            java.lang.String pageResult = "";

            Map<java.lang.String, java.lang.String> respParam = getAllRequestParam(req);
            System.out.println(respParam.toString());

            // 打印请求报文
            LogUtil.printPayRequestLog(respParam);

            String username = req.getParameter("username");
            String orderType = req.getParameter("orderType");
            //将响应数据转换为实体
            UnionPay unionPay = Utils.toBean(UnionPay.class, respParam);
            txnAmt = Integer.parseInt(unionPay.getTxnAmt()) / 100;
            //支付成功
            if (unionPay.getRespCode().equals("00")) {
                json.put("success", "1");
                LogUtil.writeLog("保存交易历史记录");
                saveTradingRecord(unionPay, username,orderType);
            } else {
                json.put("success", "0");
                Map<java.lang.String, java.lang.String> sParaTemp = new HashMap<java.lang.String, java.lang.String>();
                sParaTemp.put("flag", "add");
                sParaTemp.put("integral", unionPay.getTxnAmt());
                //调用单点积分接口
                Map<java.lang.String, Object> retMap = HttpClient.getCode(sParaTemp);
                int code = Integer.parseInt(retMap.get("code") + "");
                //支付成功
                       /* if(code==201){
                            integral = retMap.get("integral")+"";
					    }else if(code==502||code==500||code==400){
					    	integral = retMap.get("integral")+"";
					    }*/
                integral = retMap.get("integral") + "";
                json.put("integral", integral);
            }

            LogUtil.writeInterfaceMessage(json.toString());
            return;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                json.put("success", "0");
                json.put("respMsg", "支付异常");
                paramMap.put("jsonStr", json.toString());
                java.lang.String html = AcpService.createAutoFormHtml(backUrl, paramMap, UnionPayBase.encoding_UTF8);
                LogUtil.writeLog(html);
                out.write(html);
                return;
            } catch (JSONException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

    }

    /*
  *@Author Gavin
  *@Description 保存交易记录
  *@Date 2017/2/27 10:25
  *@Email gavinsjq@sina.com
  *@Params
  */
    private void saveTradingRecord(UnionPay unionPay, String username, String orderType) {
        String serialNum = unionPay.getQueryId();
        String orderNo = unionPay.getOrderId();
        String dealSum = unionPay.getTxnAmt();
        TransactionHistory old = transactionService.findBySerialNum(serialNum);
        if (old == null) {
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setSerialNum(serialNum);
            transactionHistory.setOrderNo(orderNo);
            transactionHistory.setPayWay(Payment.UnionPay);
            transactionHistory.setDealSum(Double.parseDouble(dealSum) / 100);
            transactionHistory.setUserName(username);
            transactionHistory.setType(1);
            transactionService.save(transactionHistory);
            //回调订单接口，通知支付成功
//            Map<String,Object> paramsMap = new HashMap<String,Object>();
//            paramsMap.put("orderNo",orderNo);
//            paramsMap.put("paymentType",Payment.UnionPay);
//            String resultCode = SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/web/finish/paymentSuccess", com.yesmywine.httpclient.bean.RequestMethod.post,paramsMap,null);
//            System.out.println("银联支付成功，调用订单，返回结果==》"+resultCode);
            Map<String,Object> paramsMap = new HashMap<String,Object>();
            paramsMap.put("orderNo",orderNo);
            paramsMap.put("paymentType",Payment.UnionPay);
            if(orderType.equals("order")){
                Notify.toOrderService(paramsMap,Payment.UnionPay.name());
            }else {
                Notify.toUserService(paramsMap);
            }
        }
    }

    /**
     * 获取请求参数中所有的信息
     *
     * @param request
     * @return
     */
    public static Map<java.lang.String, java.lang.String> getAllRequestParam(final HttpServletRequest request) {
        Map<java.lang.String, java.lang.String> res = new HashMap<java.lang.String, java.lang.String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                java.lang.String en = (java.lang.String) temp.nextElement();
                java.lang.String value = request.getParameter(en);
                res.put(en, value);
                //在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                //System.out.println("ServletUtil类247行  temp数据的键=="+en+"     值==="+value);
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }

	/*public static void backUrl(final String url,final Map<String, String> paramMap,HttpServletRequest request,final HttpServletResponse response){

				  response.setHeader("Content-type", "text/html;charset=UTF-8");
				  PrintWriter out = null;
				  try {
					    out = response.getWriter();
						String html = AcpService.createAutoFormHtml("http://127.0.0.1:8080/PayWebService/pay/PayInterface.do", paramMap,UnionPayBase.encoding_UTF8);
						System.out.println(html);
						out.write(html);
				} catch (IOException e) {
					e.printStackTrace();
				} 
				
			}
	
	}*/

}
