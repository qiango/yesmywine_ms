package com.yesmywine.orders.webController;

import com.yesmywine.jwt.UserUtils;
import com.yesmywine.orders.service.OrderRefreshService;
import com.yesmywine.orders.service.OrderService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wangdiandian on 2017/7/14.
 */
@RestController
@RequestMapping("/member/orders")
public class MemberOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRefreshService orderRefreshService;

    @RequestMapping(value = "viewLogistics",method = RequestMethod.GET)
    public String viewLogistics(Long orderNo, HttpServletRequest request) {//查看物流
        try {
            Integer userId = UserUtils.getUserId(request);
            if (ValueUtil.isEmpity(userId)) {
                ValueUtil.isError("未登录");
            }
            return ValueUtil.toJson(HttpStatus.SC_OK, orderService.viewLogistics(orderNo, userId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }

    @RequestMapping(value = "/orders/goodsNamePage", method = RequestMethod.GET)
    public String goodsNamePage(String goodsName,Integer pageNo,Integer pageSize,HttpServletRequest request) {
        try {
            Integer userId = UserUtils.getUserId(request);
            if (ValueUtil.isEmpity(userId)) {
                ValueUtil.isError("未登录");
            }
            return ValueUtil.toJson(HttpStatus.SC_OK, orderService.findgOrdersPage(goodsName, pageNo, pageSize, userId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

//    @RequestMapping(value = "giftCard",method = RequestMethod.GET)
//    public String giftCard(Long cardNumber,String password,HttpServletRequest request) {
//        try {
//            String userInfo = UserUtils.getUserInfo(request).toJSONString();
//            if(ValueUtil.isEmpity(userInfo)){
//                return "未登录";
//            }
//            return ValueUtil.toJson(HttpStatus.SC_OK, orderRefreshService.giftCard(cardNumber, password,userInfo));
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(), e.getMessage());
//        }

//    }


    @RequestMapping(value = "/giftCard/detaile",method = RequestMethod.POST)
    public String giftCardDetaile(Long orderNo,Integer goodsId,HttpServletRequest request) {
        try {
            Integer userId = UserUtils.getUserId(request);
            if (ValueUtil.isEmpity(userId)) {
                ValueUtil.isError("未登录");
            }
            return  orderService.giftCardDetaile(orderNo, goodsId);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }
}