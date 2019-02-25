package com.yesmywine.orders.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.orders.service.OrderReturnExchangeService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/4/12.
 */
@RestController
@RequestMapping("/orders/orderReturn")
public class OrderReturnExchangeController {
    @Autowired
    private OrderReturnExchangeService orderReturnExchangeService;


    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer id) throws YesmywineException {
        MapUtil.cleanNull(params);

        if (id != null) {
            return ValueUtil.toJson(HttpStatus.SC_OK, orderReturnExchangeService.updateLoad(id));
        }
            if (ValueUtil.notEmpity(params.get("type"))) {
                String supplierType = params.get("type").toString();
                if (supplierType.equals("all")) {
                    params.remove(params.remove("type").toString());
                }
            }
            if (ValueUtil.notEmpity(params.get("channel"))) {
                String supplierType = params.get("channel").toString();
                if (supplierType.equals("all")) {
                    params.remove(params.remove("channel").toString());
                }
            }

            if (ValueUtil.notEmpity(params.get("rebackType"))) {
                String supplierType = params.get("rebackType").toString();
                if (supplierType.equals("all")) {
                    params.remove(params.remove("rebackType").toString());
                }

        }

            if (ValueUtil.notEmpity(params.get("status"))) {
                String supplierType = params.get("status").toString();
                if (supplierType.equals("all")) {
                    params.remove(params.remove("status").toString());
                }
        }

            if (null != params.get("all") && params.get("all").toString().equals("true")) {
                return ValueUtil.toJson(orderReturnExchangeService.findAll());
            } else if (null != params.get("all")) {
                params.remove(params.remove("all").toString());
            }
            PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
            if (null != params.get("showFields")) {
                pageModel.setFields(params.remove("showFields").toString());
            }
            if (pageNo != null) params.remove(params.remove("pageNo").toString());
            if (pageSize != null) params.remove(params.remove("pageSize").toString());
            pageModel.addCondition(params);
            pageModel = orderReturnExchangeService.findAll(pageModel);
            return ValueUtil.toJson(HttpStatus.SC_OK, pageModel);

        }
    @RequestMapping(value = "/auditOrders",method = RequestMethod.PUT)
    public String auditRefundOrders(String returnNo, Integer type ,String rejectReason, Boolean isQualityProblem,HttpServletRequest request) {//订单退换货审核
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, orderReturnExchangeService.auditOrders(returnNo,type, isQualityProblem,rejectReason));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
}