package com.yesmywine.orders.service;

        import com.yesmywine.base.record.biz.BaseService;
        import com.yesmywine.orders.entity.OrderReturnExchange;
        import com.yesmywine.util.error.YesmywineException;
        import org.springframework.web.bind.annotation.RequestParam;

        import javax.servlet.http.HttpServletRequest;
        import java.util.Map;

/**
 * Created by wangdiandian on 2017/4/12.
 */
public interface OrderReturnExchangeService extends BaseService<OrderReturnExchange, Integer> {

    String creatOrderReturnExchange(@RequestParam Map<String, String> param,Integer userId)throws YesmywineException;//新增退换货单

    OrderReturnExchange updateLoad(Integer id) throws YesmywineException;//加载退换货单

    String auditOrders(String returnNo, Integer type, Boolean isQualityProblem, String rejectReason) throws YesmywineException; //退换货审核

    String refundCancel(Integer refundId)throws YesmywineException;

    Map<String, Object> application(Long orderNo,Integer goodsId,Integer userId) throws YesmywineException;

//    String review(Integer id,Integer reviewStatus) throws YesmywineException;
}
