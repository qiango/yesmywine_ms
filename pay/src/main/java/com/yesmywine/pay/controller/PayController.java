package com.yesmywine.pay.controller;

import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.bean.PaymentResult;
import com.yesmywine.pay.service.PayFactory;
import com.yesmywine.pay.service.PaymentBiz;
import com.yesmywine.pay.service.TransactionService;
import com.yesmywine.pay.dao.PaymentDao;
import com.yesmywine.pay.entity.TransactionHistory;
import com.yesmywine.pay.wechat.utils.QRCodeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.util.number.DoubleUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by WANG, RUIQING on 11/30/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PaymentDao paymentDao;
    @Autowired
    private PayFactory payFactory;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {
        return ValueUtil.toJson("payments", paymentDao.findAll());
    }

//	@RequestMapping(method = RequestMethod.POST)
//	public String create(){
//		PaymentEntity paymentEntity = new PaymentEntity();
//		paymentEntity.setPaymentCode(Payment.Alipay);
//		paymentEntity.setActive(Active.isActive);
//		paymentEntity.setDescription("test");
//		return ValueUtil.toJson("payments",paymentDao.save(paymentEntity));
//	}


    @RequestMapping(value = "/pc",method = RequestMethod.POST)
    public String pay(String orderNumber, Double amount, String payment,String type, String description, HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            List<TransactionHistory > transactionHistoryList = transactionService.findByOrderNoAndType(orderNumber,1);
            TransactionHistory transactionHistory = null;
            if(transactionHistoryList.size()>0){
                transactionHistory = transactionHistoryList.get(0);
            }
            ValueUtil.verify(type,"type");
            ValueUtil.verify(orderNumber,"orderNumber");
            ValueUtil.verify(amount,"amount");
            ValueUtil.verify(payment,"payment");
            ValueUtil.verifyExist(transactionHistory, "该交易已完成");
            String username = UserUtils.getUserName(request);

            Payment pay = Payment.getPayment(payment);
            PaymentBiz paymentBiz = payFactory.getInstance(pay);

            PaymentResult paymentResult = paymentBiz.pay(orderNumber, amount, type, description, username, request);

//            if (pay.equals(Payment.WeChat) && paymentResult.getCode().equals("SUCCESS")) {
//                String urlCode = paymentResult.getData();
//                try {
//                    QRCodeUtils.qrCodeEncode(urlCode, "QR");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                response.setContentType("text/html; charset=UTF-8");
//                response.setContentType("image/jpeg");
//                FileInputStream fis = null;
//                fis = new FileInputStream("QR" + ".png");
//                OutputStream os = null;
//                os = response.getOutputStream();
//                int count = 0;
//                byte[] buffer = new byte[1024 * 1024];
//                while ((count = fis.read(buffer)) != -1) {
//                    os.write(buffer, 0, count);
//                }
//                os.flush();
//                return ValueUtil.toJson(paymentResult);
//            }

            return ValueUtil.toJson(paymentResult);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/app", method = RequestMethod.POST)
    public String appPay(String orderNumber, Double amount, String payment,String type, String description, HttpServletRequest request, HttpServletResponse response) {

        try {
            ValueUtil.verify(type,"type");
            ValueUtil.verify(orderNumber,"orderNumber");
            ValueUtil.verify(amount,"amount");
            ValueUtil.verify(payment,"payment");
            List<TransactionHistory > transactionHistoryList = transactionService.findByOrderNoAndType(orderNumber,1);
            TransactionHistory transactionHistory = null;
            if(transactionHistoryList.size()>0){
                transactionHistory = transactionHistoryList.get(0);
            }
            ValueUtil.verifyExist(transactionHistory, "该交易已完成");
            Payment pay = Payment.getPayment(payment);
            PaymentBiz paymentBiz = payFactory.getInstance(pay);
            String username = UserUtils.getUserName(request);

            PaymentResult paymentResult = paymentBiz.appPay(orderNumber, amount, type, description, username, request);
//            if (pay.equals(Payment.WeChat) && paymentResult.getCode().equals("SUCCESS")) {
//                String urlCode = paymentResult.getData();
//                try {
//                    QRCodeUtils.qrCodeEncode(urlCode, "QR");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                response.setContentType("text/html; charset=UTF-8");
//                response.setContentType("image/jpeg");
//                FileInputStream fis = null;
//                fis = new FileInputStream("QR" + ".png");
//                OutputStream os = null;
//                os = response.getOutputStream();
//                int count = 0;
//                byte[] buffer = new byte[1024 * 1024];
//                while ((count = fis.read(buffer)) != -1) {
//                    os.write(buffer, 0, count);
//                }
//                os.flush();
//                return ValueUtil.toJson(paymentResult);
//            }

            return ValueUtil.toJson(paymentResult.getData());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public String refresh() {
        return ValueUtil.toJson(payFactory.refreshPayment());
    }

    @RequestMapping(value = "/refund/itf", method = RequestMethod.POST)
    public String refund(String orderNumber,Double refundAmount, String payment, String refundNumber, String title, String description) {
        refresh();
        Payment pay = Payment.getPayment(payment);
        try {
            ValueUtil.verify(orderNumber,"orderNumber");
            ValueUtil.verify(refundAmount,"refundAmount");
            ValueUtil.verify(payment,"payment");
            PaymentBiz paymentBiz = payFactory.getInstance(pay);
            TransactionHistory history = transactionService.findByOrderNoAndType(orderNumber,1).get(0);
            List<TransactionHistory> refundHistoryList = transactionService.findByOrderNoAndType(orderNumber,2);
            Double refundTotal = 0.0;
            for (TransactionHistory refundHistory:refundHistoryList){
                refundTotal = DoubleUtils.add(refundTotal,refundHistory.getRefundSum()==null?0.0:refundHistory.getRefundSum());
            }
            Double payAmount = history.getDealSum();
            Double balanceAmount = DoubleUtils.sub(payAmount,refundTotal);
            if(balanceAmount<refundAmount){
                ValueUtil.isError("退款金额大于可退金额，无法退款");
            }
            return ValueUtil.toJson(paymentBiz.refund(orderNumber,refundNumber, payAmount, refundAmount, title, description));
        } catch (YesmywineException e) {
            e.printStackTrace();
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String queryDetail(String orderId, String payment) {

        Payment pay = Payment.getPayment(payment);
        try {
            PaymentBiz paymentBiz = payFactory.getInstance(pay);
            return ValueUtil.toJson(paymentBiz.queryDetail(orderId));
        } catch (Exception e) {
            HttpBean httpBean = new HttpBean(ConstantDt.dicUrl + "/dic/" + e.getMessage());
            httpBean.run();
            return ValueUtil.toError(e.getMessage(), httpBean.getResponseContent());
        }
    }

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/8/18
    *@Param
    *@Description:获取支付状态
    */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public String result(String orderNo) {
        for(int i = 0;i<22;i++){
            List<TransactionHistory > transactionHistoryList = transactionService.findByOrderNoAndType(orderNo,1);
            TransactionHistory transactionHistory = null;
            if(transactionHistoryList.size()>0){
                transactionHistory = transactionHistoryList.get(0);
            }
            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(transactionHistory!=null){
                return ValueUtil.toJson("SUCCESS");
            }
        }

        try {
            ValueUtil.isError("支付可能失败，请查看订单状态");
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        return null;
    }
}
