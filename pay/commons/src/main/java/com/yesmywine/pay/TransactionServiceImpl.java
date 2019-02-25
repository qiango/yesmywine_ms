package com.yesmywine.pay;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.service.TransactionService;
import com.yesmywine.pay.dao.TransactionDao;
import com.yesmywine.pay.entity.TransactionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SJQ on 2017/2/27.
 */
@Service
public class TransactionServiceImpl extends BaseServiceImpl<TransactionHistory, Integer> implements TransactionService {
    @Autowired
    private TransactionDao transactionDao;

    @Override
    public List<TransactionHistory> findByOrderNoAndType(String orderNo, Integer type) {
        return transactionDao.findByOrderNoAndType(orderNo,type);
    }

    @Override
    public TransactionHistory findByOrderIdAndPayWay(String orderNumber, String payWay) {
        return transactionDao.findByOrderNoAndPayWay(orderNumber, Payment.getPayment(payWay));
    }

    @Override
    public TransactionHistory findBySerialNum(String serialNum) {
        return transactionDao.findBySerialNum(serialNum);
    }

    @Override
    public TransactionHistory findByReturnNo(String returnNo) {
        return  transactionDao.findByReturnNo(returnNo);
    }
}
