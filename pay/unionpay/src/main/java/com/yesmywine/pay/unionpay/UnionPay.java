package com.yesmywine.pay.unionpay;

import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.bean.PaymentResult;
import com.yesmywine.pay.service.PaymentBiz;
import com.yesmywine.pay.service.TransactionService;
import com.yesmywine.pay.entity.TransactionHistory;
import com.yesmywine.pay.unionpay.controller.DemoBase;
import com.yesmywine.pay.unionpay.sdk.AcpService;
import com.yesmywine.pay.unionpay.sdk.LogUtil;
import com.yesmywine.pay.unionpay.sdk.SDKConfig;
import com.yesmywine.pay.unionpay.sdk.UnionPayBase;
import com.yesmywine.pay.unionpay.util.Config;
import com.yesmywine.pay.service.PayFactory;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SJQ
 * @version 2017-02-15
 */
@Service
public class UnionPay extends DemoBase implements PaymentBiz {

    private static final String TIP = "在线支付[PayInterface]: ";

    @Autowired
    private TransactionService transactionService;

    static {
        SDKConfig.getConfig().loadPropertiesFromSrc();
    }

    /*
    *@Author Gavin
    *@Description 银联支付
    *@Date 2017/2/27 14:22
    *@Email gavinsjq@sina.com
    *@Params
    */
    public PaymentResult pay(String orderNumber, Double amount, String title, String description, String username, HttpServletRequest request) {
//        UnionPaySetting unionPaySetting = getUnionPaySetting();
//        unionPaySetting
        try {
            Map<String, String> requestData = new HashMap<String, String>();
            /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
            requestData.put("version", DemoBase.version);              //版本号，全渠道默认值
            requestData.put("encoding", DemoBase.encoding_UTF8);              //字符集编码，可以使用UTF-8,GBK两种方式
            requestData.put("signMethod", "01");                          //签名方法，只支持 01：RSA方式证书加密
            requestData.put("txnType", "01");                          //交易类型 ，01：消费
            requestData.put("txnSubType", "01");                          //交易子类型， 01：自助消费
            requestData.put("bizType", "000201");                      //业务类型，B2C网关支付，手机wap支付
            requestData.put("channelType", "07");                      //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机

            /***商户接入参数***/
            requestData.put("merId", PayFactory.unionPaySetting.getMerId());         //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
            requestData.put("accessType", "0");                          // 接入类型，商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
            requestData.put("orderId", orderNumber);             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
            requestData.put("txnTime", DemoBase.getCurrentTime());        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
            requestData.put("currencyCode", "156");                      //交易币种（境内商户一般是156 人民币）
            requestData.put("txnAmt", String.valueOf((long) (amount * 100)));                              //交易金额，单位分，不要带小数点

            // 前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
            // 如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
            // 异步通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
            // 将所支付的文献Id传给回调，用于保存支付记录 前台返回页面
            String param = "?username=" + username+"&orderType="+title;
//            String param = "";
            requestData.put("frontUrl", PayFactory.unionPaySetting.getFrontUrl() + param);


            // 后台通知地址（需设置为【外网】能访问 http
            // https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
            // 后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
            // 注意:1.需设置为外网能访问，否则收不到通知 2.http https均可 3.收单后台通知后需要10秒内返回http200或302状态码
            // 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
            // 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d
            // 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
            requestData.put("backUrl", PayFactory.unionPaySetting.getPayBackUrl() + param);


            /** 请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面 **/
            Map<String, String> submitFromData = AcpService.sign(requestData, UnionPayBase.encoding_UTF8); // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。

            String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
            String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData, UnionPayBase.encoding_UTF8);
            html = html.replaceAll("\"", "'");
            LogUtil.writeLog("打印请求HTML，此为请求报文，为联调排查问题的依据：" + html);

            PaymentResult paymentResult = new PaymentResult();
            paymentResult.setCode("SUCCESS");
            paymentResult.setPayment(Payment.UnionPay);
            paymentResult.setData(html);
            return paymentResult;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public PaymentResult queryDetail(String orderId) {
        Map<String, String> data = new HashMap<String, String>();
        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", DemoBase.version);                 //版本号
        data.put("encoding", DemoBase.encoding_UTF8);               //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", "01");                          //签名方法 目前只支持01-RSA方式证书加密
        data.put("txnType", "00");                             //交易类型 00-默认
        data.put("txnSubType", "00");                          //交易子类型  默认00
        data.put("bizType", "000202");                         //业务类型

        /***商户接入参数***/
        data.put("merId", PayFactory.unionPaySetting.getMerId());           //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                           //接入类型，商户接入固定填0，不需修改

        /***要调通交易以下字段必须修改***/
        data.put("orderId", orderId);                 //****商户订单号，每次发交易测试需修改为被查询的交易的订单号
        data.put("txnTime", DemoBase.getCurrentTime());                 //****订单发送时间，每次发交易测试需修改为被查询的交易的订单发送时间

        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/

        Map<String, String> reqData = AcpService.sign(data, DemoBase.encoding_UTF8);            //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getSingleQueryUrl();                                //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.singleQueryUrl
        Map<String, String> rspData = AcpService.post(reqData, url, DemoBase.encoding_UTF8);  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        if (!rspData.isEmpty()) {
            if (AcpService.validate(rspData, DemoBase.encoding_UTF8)) {
                LogUtil.writeLog("验证签名成功");
                if (("00").equals(rspData.get("respCode"))) {//如果查询交易成功
                    String origRespCode = rspData.get("origRespCode");
                    if (("00").equals(origRespCode)) {
                        //交易成功，更新商户订单状态
                        //TODO
                    } else if (("03").equals(origRespCode) ||
                            ("04").equals(origRespCode) ||
                            ("05").equals(origRespCode)) {
                        //订单处理中或交易状态未明，需稍后发起交易状态查询交易 【如果最终尚未确定交易是否成功请以对账文件为准】
                        //TODO
                    } else {
                        //其他应答码为交易失败
                        //TODO
                    }
                } else if (("34").equals(rspData.get("respCode"))) {
                    //订单不存在，可认为交易状态未明，需要稍后发起交易状态查询，或依据对账结果为准

                } else {//查询交易本身失败，如应答码10/11检查查询报文是否正确
                    //TODO
                }
            } else {
                LogUtil.writeErrorLog("验证签名失败");
                //TODO 检查验证签名失败的原因
            }
        } else {
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
        }

        String reqMessage = DemoBase.genHtmlResult(reqData);
        String rspMessage = DemoBase.genHtmlResult(rspData);
        PaymentResult paymentResult = new PaymentResult();
        paymentResult.setCode("SUCCESS");
        paymentResult.setPayment(Payment.UnionPay);
        paymentResult.setData("交易状态查询交易</br>请求报文:<br/>" + reqMessage + "<br/>" + "应答报文:</br>" + rspMessage + "");
        return paymentResult;
    }

    @RequestMapping(value = "/refund", method = RequestMethod.GET)
    public PaymentResult refund(String orderNumber, String refundNumber, Double payAmount, Double refundAmount, String title, String description) {
        TransactionHistory transactionHistory = transactionService.findByOrderIdAndPayWay(orderNumber, "UnionPay");
        String origQryId = transactionHistory.getSerialNum();

        Map<String, String> data = new HashMap<String, String>();
        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", DemoBase.version);               //版本号
        data.put("encoding", DemoBase.encoding_UTF8);             //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", "01");                        //签名方法 目前只支持01-RSA方式证书加密
        data.put("txnType", "04");                           //交易类型 04-退货
        data.put("txnSubType", "00");                        //交易子类型  默认00
        data.put("bizType", "000202");                       //业务类型
        data.put("channelType", "07");                       //渠道类型，07-PC，08-手机

        /***商户接入参数***/
        data.put("merId", PayFactory.unionPaySetting.getMerId());                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("backUrl", PayFactory.unionPaySetting.getRefundBackUrl());               //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 退货交易 商户通知,其他说明同消费交易的后台通知
        data.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改
        data.put("orderId", orderNumber);          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
        data.put("txnTime", DemoBase.getCurrentTime());      //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        data.put("currencyCode", "156");                     //交易币种（境内商户一般是156 人民币）
        data.put("txnAmt", String.valueOf((long) (refundAmount * 100)));                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额
        //data.put("reqReserved", "透传信息");                    //请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节

        /***要调通交易以下字段必须修改***/
        data.put("origQryId", origQryId);      //****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取

        /**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/

        Map<String, String> reqData = AcpService.sign(data, DemoBase.encoding_UTF8);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String url = SDKConfig.getConfig().getBackRequestUrl();                                //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> rspData = AcpService.post(reqData, url, DemoBase.encoding_UTF8);//这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        if (!rspData.isEmpty()) {
            if (AcpService.validate(rspData, DemoBase.encoding_UTF8)) {
                LogUtil.writeLog("验证签名成功");
                String respCode = rspData.get("respCode");
                if (("00").equals(respCode)) {
                    //交易已受理(不代表交易已成功），等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
                    //TODO
                    LogUtil.writeLog("退款请求成功,记录退款信息");
                    //修改交易状态
                    updateTradingRecord(rspData,refundNumber);
                } else if (("03").equals(respCode) ||
                        ("04").equals(respCode) ||
                        ("05").equals(respCode)) {
                    //后续需发起交易状态查询交易确定交易状态
                    //TODO
                    System.out.println("退款请求失败");
                } else {
                    //其他应答码为失败请排查原因
                    //TODO
                    System.out.println("退款请求失败");
                }
            } else {
                LogUtil.writeErrorLog("验证签名失败");
                //TODO 检查验证签名失败的原因
            }
        } else {
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
        }
        String reqMessage = DemoBase.genHtmlResult(reqData);
        String rspMessage = DemoBase.genHtmlResult(rspData);
        try {
            PaymentResult paymentResult = new PaymentResult();
            paymentResult.setCode("SUCCESS");
            paymentResult.setPayment(Payment.UnionPay);
            paymentResult.setData("请求报文:<br/>" + reqMessage + "<br/>" + "应答报文:</br>" + rspMessage);
            return paymentResult;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public PaymentResult appPay(String orderNumber, Double amount, String title, String description, String username, HttpServletRequest request) throws YesmywineException {
        Map<String, String> data = new HashMap<String, String>();
        /***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
        data.put("version", DemoBase.version);               //版本号
        data.put("encoding", DemoBase.encoding_UTF8);             //字符集编码 可以使用UTF-8,GBK两种方式
        data.put("signMethod", "01");                        //签名方法
        data.put("txnType", "01");              		 	//交易类型 01:消费
        data.put("txnSubType", "01");           		 	//交易子类 01：消费
        data.put("bizType", "000201");                       //业务类型
        data.put("channelType", "08");                       //渠道类型，07-PC，08-手机

        /***商户接入参数***/
        data.put("merId", PayFactory.unionPaySetting.getMerId());                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
        data.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改
        data.put("orderId", orderNumber);          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费
        data.put("txnTime", DemoBase.getCurrentTime());      //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        data.put("currencyCode", "156");                     //交易币种（境内商户一般是156 人民币）
        data.put("accType", "01");					 	//账号类型 01：银行卡02：存折03：IC卡帐号类型(卡介质)
        data.put("txnAmt", String.valueOf((long) (amount * 100)));                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额
        //data.put("reqReserved", "透传信息");                    //请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节
        String param = "?username=" + username+"&orderType="+title;
        //后台通知地址（需设置为外网能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，【支付失败的交易银联不会发送后台通知】
        //后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
        //注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码
        //    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200或302，那么银联会间隔一段时间再次发送。总共发送5次，银联后续间隔1、2、4、5 分钟后会再次通知。
        //    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败

        data.put("backUrl", PayFactory.unionPaySetting.getPayBackUrl() + param);

        /**对请求参数进行签名并发送http post请求，接收同步应答报文**/
        Map<String, String> reqData = AcpService.sign(data,UnionPayBase.encoding_UTF8);			 //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl();								 //交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
        Map<String, String> rspData = AcpService.post(reqData,requestAppUrl,UnionPayBase.encoding_UTF8);  //发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

        /**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
        //应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
        if(!rspData.isEmpty()){
            if(AcpService.validate(rspData, UnionPayBase.encoding_UTF8)){
                LogUtil.writeLog("验证签名成功");
                String respCode = rspData.get("respCode") ;
                if(("00").equals(respCode)){
                    //成功,获取tn号
                    String tn = rspData.get("tn");
                    PaymentResult paymentResult = new PaymentResult();
                    paymentResult.setCode("SUCCESS");
                    paymentResult.setPayment(Payment.UnionPay);
                    paymentResult.setData(tn);
                    return paymentResult;
                    //TODO
                }else{
                    //其他应答码为失败请排查原因或做失败处理

                    //TODO
                }
            }else{
                LogUtil.writeErrorLog("验证签名失败");
                //TODO 检查验证签名失败的原因
            }
        }else{
            //未返回正确的http状态
            LogUtil.writeErrorLog("未获取到返回报文或返回http状态码非200");
        }
        String reqMessage = DemoBase.genHtmlResult(reqData);
        String rspMessage = DemoBase.genHtmlResult(rspData);
        ValueUtil.isError("支付失败，请重新支付或更换支付方式");
        return null;
    }

    public PaymentResult androidPay(String orderNumber,Double amount,Integer userId){

        return null;
    }

    public PaymentResult iosPay(){

        return null;
    }

    /**
     * 银联支付判断传入参数是否正确
     *
     * @return
     * @params request
     */
    public static String integralpayMap(Map<String, String> respParam) {
        String token = respParam.get("token");
        if (StringUtils.isEmpty(token)) {
            return TIP + "token值不能为空";
        } else if (StringUtils.isEmpty(Config.queryToken(token))) {
            return TIP + "token值不正确";
        }
        String txnAmt = respParam.get("txnAmt");
        if (StringUtils.isEmpty(txnAmt)) {
            return TIP + "txnAmt值不能为空";
        } else {

        }
        String contentId = respParam.get("contentId");
        if (StringUtils.isEmpty(contentId)) {
            return TIP + "在线支付时contentId不能为空";
        }
        String userName = respParam.get("userName");
        if (StringUtils.isEmpty(userName)) {
            return TIP + "userName值不能为空";
        }
        String frontUrl = respParam.get("frontUrl");
        if (StringUtils.isEmpty(frontUrl)) {
            return TIP + "frontUrl值不能为空";
        }
        String encoding = respParam.get("encoding");
        if (StringUtils.isEmpty(encoding)) {
            return TIP + "encoding值不能为空";
        }
        return "";
    }


    /**
     * 银联支付判断传入参数是否正确
     *
     * @return
     * @params request
     */
    public static String unionpayMap(Map<String, String> respParam) {
        String token = respParam.get("token");
        if (StringUtils.isEmpty(token)) {
            return TIP + "token值不能为空";
        } else if (StringUtils.isEmpty(Config.queryToken(token))) {
            return TIP + "token值不正确";
        }
        String txnAmt = respParam.get("txnAmt");
        if (StringUtils.isEmpty(txnAmt)) {
            return TIP + "txnAmt值不能为空";
        } else {

        }
        String action = respParam.get("action");
        if (StringUtils.isEmpty(action)) {
            return TIP + "action值不能为空";
        } else if (!action.equals("1") && !action.equals("2")) {
            return TIP + "action值错误";
        }
        String contentId = respParam.get("contentId");
        if (action.equals("2") && StringUtils.isEmpty(contentId)) {
            return TIP + "在线支付时contentId不能为空";
        }
        String userName = respParam.get("userName");
        if (StringUtils.isEmpty(userName)) {
            return TIP + "userName值不能为空";
        }
        String frontUrl = respParam.get("frontUrl");
        if (StringUtils.isEmpty(frontUrl)) {
            return TIP + "frontUrl值不能为空";
        }
        String backUrl = respParam.get("backUrl");
        if (StringUtils.isEmpty(backUrl)) {
            return TIP + "backUrl值不能为空";
        }
        String encoding = respParam.get("encoding");
        if (StringUtils.isEmpty(encoding)) {
            return TIP + "encoding值不能为空";
        }
        return "";
    }


    /**
     * 判断传入参数是否正确
     *
     * @return
     * @params request
     */
    public static String payMap(Map<String, String> respParam) {
        String token = respParam.get("token");
        if (StringUtils.isEmpty(token)) {
            return TIP + "token值不能为空";
        } else if (StringUtils.isEmpty(Config.queryToken(token))) {
            return TIP + "token值不正确";
        }
        String txnAmt = respParam.get("txnAmt");
        if (StringUtils.isEmpty(txnAmt)) {
            return TIP + "txnAmt值不能为空";
        } else {

        }
        String payType = respParam.get("payType");
        if (StringUtils.isEmpty(payType)) {
            return TIP + "payType值不能为空";
        } else if (!payType.equals("unionPay") && !payType.equals("aliPay") && !payType.equals("jf")) {
            return TIP + "不存在此支付方式";
        }
        String action = respParam.get("action");
        if (StringUtils.isEmpty(action)) {
            return TIP + "action值不能为空";
        } else if (!action.equals("1") && !action.equals("2") && !action.equals("3")) {
            return TIP + "action值错误";
        }
        String userName = respParam.get("userName");
        if (StringUtils.isEmpty(userName)) {
            return TIP + "userName值不能为空";
        }
        String frontUrl = respParam.get("frontUrl");
        if (StringUtils.isEmpty(frontUrl)) {
            return TIP + "frontUrl值不能为空";
        }
        String backUrl = respParam.get("backUrl");
        if (StringUtils.isEmpty(backUrl)) {
            return TIP + "backUrl值不能为空";
        }
        String encoding = respParam.get("encoding");
        if (StringUtils.isEmpty(encoding)) {
            return TIP + "encoding值不能为空";
        }
        return "";
    }

    private void updateTradingRecord(Map<String, String> map,String returnNo) {
        String queryId = map.get("queryId");//退款单号
        String origQryId = map.get("origQryId");//流水号
        String txnAmt = map.get("txnAmt");//退款金额
        TransactionHistory transactionHistory = transactionService.findBySerialNum(origQryId);
        TransactionHistory oldHistory = transactionService.findByReturnNo(returnNo);
        if(oldHistory==null) {
            TransactionHistory returnHistory = new TransactionHistory();
            returnHistory.setRefundSum(Double.valueOf(txnAmt));
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
