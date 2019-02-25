package com.yesmywine.orders.webController;

import com.yesmywine.orders.dao.OrderDetailDao;
import com.yesmywine.orders.dao.OrdersDao;
import com.yesmywine.orders.entity.OrderDetail;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hz on 8/14/17.
 */
@RestController
@RequestMapping("/web/orders")
public class GoodsByBuyController {
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrderDetailDao orderDetailDao;

    @RequestMapping(value = "/justBought",method = RequestMethod.GET)
    public String getBuy() {//刚刚被购买
        List<Long> orderNumber = ordersDao.findOrder();
        Set<OrderDetail> detail=new HashSet<>();
        for(Long l:orderNumber){
            if(detail.size()==3){
                break;
            }
            List<OrderDetail> list=orderDetailDao.findByOrderNo(l);
            for(OrderDetail o:list){
                detail.add(o);
                if(detail.size()==3){
                    break;
                }
            }

        }
        String a = "";
        for(OrderDetail o:detail){
            String goodsId=o.getGoodsId().toString();
            a=a+goodsId+",";
        }
        return ValueUtil.toJson(HttpStatus.SC_OK,a);
    }
}
