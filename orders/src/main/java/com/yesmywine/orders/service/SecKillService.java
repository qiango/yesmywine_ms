package com.yesmywine.orders.service;


import javax.servlet.ServletContext;

/**
 * Created by light on 2017/3/8.
 */
public interface SecKillService {
    Boolean seckill(String skuId, FIFOService<String> fifo, Integer count, ServletContext servletContext);
}
