package com.yesmywine.pay.unionpay.entity;

/**
 * @author Louxueming
 * @version 2016-08-25
 *          银联支付参数
 */
public class UnionPay {

    //************************基本信息 必填 ********************//
    /**
     * 版本号
     * 固定填写5.0.0
     * 必填
     */
    private String version;

    /**
     * 编码方式
     * 可取值UTF-8或GBK
     * 若不填写，默认取值：UTF-8
     * 必填
     */
    private String encoding;

    /**
     * 证书ID
     * 填写签名私钥证书的Serial Number，该值可通过SDK获取
     * 必填
     */
    private String certId;

    /**
     * 签名方法
     * 01：表示采用RSA  固定填写：01
     * 必填
     */
    private String signMethod;

    /**
     * 签名
     * 填写对报文摘要的签名，可通过SDK生成签名
     * 必填
     */
    private String signature;

    /**
     * 交易类型  01：消费  固定填写：01
     * 填写对报文摘要的签名，可通过SDK生成签名
     * 必填
     */
    private String txnType;

    /**
     * 交易子类  01：消费  固定填写：01
     * 依据实际交易类型填写
     * 必填
     */
    private String txnSubType;

    /**
     * 产品类型
     * 000201：B2C网关支付
     * 必填
     */
    private String bizType;

    /**
     * 渠道类型
     * 05：语音 07：互联网 08：移动   固定填写：07
     * 必填
     */
    private String channelType;


    //************************基本信息 必填 ********************//

    //************************商户信息  ********************//

    /**
     * 接入类型
     * 0：商户直连接入1：收单机构接入
     * 必填
     */
    private String accessType;

    /**
     * 商户代码
     * 已被批准加入银联互联网系统的商户代码
     * 必填
     */
    private String merId;

    /**
     * 前台通知地址
     * 用于支付完成后跳回到客户网站，必须上送完整的互联网可访问地址，
     * 支持HTTP与HTTPS协议（如：https://xxx.xxx.xxx....），
     * 地址中不能包含#与~不支持换行符等不可见字符
     * 必填
     */
    private String frontUrl;

    /**
     * 后台通知地址
     * 用于接收后台通知报文，必须上送完整的互联网可访问地址，
     * 支持HTTP与HTTPS协议（如：https://xxx.xxx.xxx....），
     * 地址中不能包含#与~不支持换行符等不可见字符
     * 必填
     */
    private String backUrl;

    /**
     * 失败交易前台跳转地址
     * 前台消费交易若商户上送此字段，则在支付失败时，页面跳转至商户该URL（不带交易信息，仅跳转），支持HTTP与HTTPS协议，互联网可访问
     */
    private String frontFailUrl;

    //************************商户信息  ********************//

    //************************订单信息  ********************//

    /**
     * 商户订单号
     * 商户订单号，仅能用大小写字母与数字，不能用特殊字符
     * 必填
     */
    private String orderId;

    /**
     * 交易币种
     * 币种格式必须为3位代码，境内客户取值：156（人民币）   固定填写：156
     * 必填
     */
    private String currencyCode;

    /**
     * 交易金额
     * 单位为分，不能带小数点，样例：1元送100  例：1元填写100
     * 必填
     */
    private String txnAmt;

    /**
     * 订单发送时间
     * 必须使用当前北京时间（年年年年月月日日时时分分秒秒）24小时制，样例：20151123152540，北京时间
     * 必填
     */
    private String txnTime;

    /**
     * 订单接收超时时间
     * 单位为毫秒，交易发生时，该笔交易在银联全渠道系统中有效的最长时间。当距离交易发送时间超过该时间时，银联全渠道系统不再为该笔交易提供支付服务
     * 必填
     */
    private String orderTimeoutInterval;

    /**
     * 支付超时时间
     * 超过此时间后，除网银交易外，其他交易银联系统会拒绝受理，提示超时。
     * 跳转银行网银交易如果超时后交易成功，会自动退款，大约5个工作日金额返还到持卡人账户。 此时间建议取支付时的北京时间加15分钟
     */
    private String payTimeout;

    //************************订单信息  ********************//

    //************************响应信息  ********************//

    /**
     * 应答码
     * 00 成功
     * 01-09 因银联全渠道系统原因导致的错误
     * 10-29 有关商户端上送报文格式检查导致的错误
     * 30-59 有关商户/收单机构相关业务检查导致的错误
     * 60-89 有关持卡人或者发卡行（渠道）相关的问题导致的错误
     * 90-99 预留
     */
    private String respCode;
    /**
     * 应答信息
     */
    private String respMsg;

    /**
     * 支付方式
     * 根据商户配置返回
     * <p>
     * 默认不返回此域，如需要返此域，需要提交申请，视商户配置返回，可在消费类交易中返回以下中的一种： 0001：认证支付 0002：快捷支付 0004：储值卡支付
     * 0005：IC卡支付 0201：网银支付 1001：牡丹畅通卡支付 1002：中铁银通卡支付 0401：信用卡支付——暂定 0402：小额临时支付 0403：认证支付2.0
     * 0404：互联网订单手机支付 9000：其他无卡支付(如手机客户端支付)
     */
    private String payType;
    /**
     * 支付卡类型
     * 消费交易，视商户配置返回。该域取值为： 00：未知 01：借记账户 02：贷记账户 03：准贷记账户 04：借贷合一账户 05：预付费账户 06：半开放预付费账户
     */
    private String payCardType;

    /**
     * 账号
     * 银行卡号。请求时使用加密公钥对交易账号加密，并做Base64编码后上送；应答时如需返回，则使用签名私钥进行解密。
     * 前台交易可由银联页面采集，也可由商户上送并返显，如需锁定返显卡号，应通过保留域（reserved）上送卡号锁定标识。
     */
    private String accNo;

    /**
     * 查询流水号
     * 由银联返回，用于在后续类交易中唯一标识一笔交易
     */
    private String queryId;
    /**
     * 系统跟踪号
     * 收单机构对账时使用，该域由银联系统产生
     */
    private String traceNo;
    /**
     * 交易传输时间
     * （月月日日时时分分秒秒）24小时制收单机构对账时使用，该域由银联系统产生
     */
    private String traceTime;
    /**
     * 清算日期
     * （月月日日时时分分秒秒）24小时制收单机构对账时使用，该域由银联系统产生
     * 为银联和入网机构间的交易结算日期。一般前一日23点至当天23点为一个清算日。也就是23点前的交易，当天23点之后开始结算，23点之后的交易，
     * 要第二天23点之后才会结算。测试环境为测试需要，13:30左右日切，所以13:30到13:30为一个清算日，测试环境今天下午为今天的日期，今天上午为昨天的日期。
     */
    private String settleDate;
    /**
     * 清算币种
     * 境内返回156
     */
    private String settleCurrencyCode;

    /**
     * 清算金额
     * 取值同交易金额
     */
    private String settleAmt;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getTxnSubType() {
        return txnSubType;
    }

    public void setTxnSubType(String txnSubType) {
        this.txnSubType = txnSubType;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public String getFrontFailUrl() {
        return frontFailUrl;
    }

    public void setFrontFailUrl(String frontFailUrl) {
        this.frontFailUrl = frontFailUrl;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getOrderTimeoutInterval() {
        return orderTimeoutInterval;
    }

    public void setOrderTimeoutInterval(String orderTimeoutInterval) {
        this.orderTimeoutInterval = orderTimeoutInterval;
    }

    public String getPayTimeout() {
        return payTimeout;
    }

    public void setPayTimeout(String payTimeout) {
        this.payTimeout = payTimeout;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayCardType() {
        return payCardType;
    }

    public void setPayCardType(String payCardType) {
        this.payCardType = payCardType;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getTraceTime() {
        return traceTime;
    }

    public void setTraceTime(String traceTime) {
        this.traceTime = traceTime;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getSettleCurrencyCode() {
        return settleCurrencyCode;
    }

    public void setSettleCurrencyCode(String settleCurrencyCode) {
        this.settleCurrencyCode = settleCurrencyCode;
    }

    public String getSettleAmt() {
        return settleAmt;
    }

    public void setSettleAmt(String settleAmt) {
        this.settleAmt = settleAmt;
    }


}
