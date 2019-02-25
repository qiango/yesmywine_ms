package com.yesmywine.orders.webController;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.orders.service.OrderReturnExchangeService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by by on 2017/7/5.
 */
@RestController
@RequestMapping("/member")
public class MemberOrderInfoContorller {
    @Autowired
    private OrderReturnExchangeService returnExchangeService;

    @RequestMapping(value = "/regurnOrders",method = RequestMethod.GET)
    public String regurnOrdersIndex(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer id, HttpServletRequest request){
        try {
            Integer userId = UserUtils.getUserId(request);
            if(userId==null){
                ValueUtil.isError("用户未登录,或已登录超时");
            }
            if (id != null) {
                return ValueUtil.toJson(returnExchangeService.updateLoad(id));
            }
            PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
            if (null != params.get("showFields")) {
                pageModel.setFields(params.remove("showFields").toString());
            }
            return ValueUtil.toJson(returnExchangeService.findAll(pageModel));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

}
