package com.yesmywine.pay.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.entity.TransactionHistory;
import com.yesmywine.pay.service.TransactionService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by by on 2017/6/30.
 */
@RestController
@RequestMapping("/pay/record")
public class PayHistoryRecord {
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer id){
        try {
            MapUtil.cleanNull(params);
            if (id != null) {
                TransactionHistory history = transactionService.findOne(id);
                ValueUtil.verifyNotExist(history,"该记录不存在！");
                return ValueUtil.toJson(history);
            }
            if (null != params.get("all") && params.get("all").toString().equals("true")) {
                return ValueUtil.toJson(transactionService.findAll());
            } else if (null != params.get("all")) {
                params.remove(params.remove("all").toString());
            }

            if (null != params.get("payWay") && !params.get("payWay").equals("")) {
               params.put("payWay", Payment.getPayment(params.get("payWay").toString()));
            }

            PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
            if (null != params.get("showFields")) {
                pageModel.setFields(params.remove("showFields").toString());
            }
            if (pageNo != null) params.remove(params.remove("pageNo").toString());
            if (pageSize != null) params.remove(params.remove("pageSize").toString());
            pageModel.addCondition(params);
            pageModel = transactionService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
}
