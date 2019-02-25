package com.yesmywine.orders.controller;

import com.yesmywine.orders.service.FIFOService;
import com.yesmywine.orders.service.impl.FIFOImpl;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * 初始化秒杀队列
 * Created by light on 2017/3/8.
 */
@RestController
@RequestMapping("/init")
public class InitController {

    //开始（创建队列）
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public String startSeckill(HttpServletRequest request, String skuId, Integer count){
            try {
                ServletContext servletContext = request.getSession().getServletContext();
            FIFOService<String> fifo = new FIFOImpl<>(count);
            servletContext.setAttribute(skuId, fifo);
        }catch (Exception e){
            return ValueUtil.toJson("失败");
        }
        return ValueUtil.toJson("success");
    }

    //结束（删除队列）
    @RequestMapping(value = "/end", method = RequestMethod.GET)
    public String endSeckill(HttpServletRequest request, String skuId){
        try {
            ServletContext servletContext = request.getSession().getServletContext();
            servletContext.removeAttribute(skuId);
        }catch (Exception e){
            return ValueUtil.toJson("失败");
        }
        return ValueUtil.toJson("success");
    }
}
