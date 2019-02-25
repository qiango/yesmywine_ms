package com.yesmywine.orders.service.impl;

import com.yesmywine.db.base.biz.RedisCache;
import com.yesmywine.orders.service.FIFOService;
import com.yesmywine.orders.service.SecKillService;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;


/**
 * Created by light on 2017/3/8.
 */
@Service
public class SecKillServiceImpl implements SecKillService {
    @Override
    public Boolean seckill(String skuId, FIFOService<String> fifo, Integer count, ServletContext servletContext) {
        boolean flag = fifo.add(count.toString());
        if(!flag){
            return flag;
        }
        RedisCache.set(skuId, Integer.parseInt(RedisCache.get(skuId))- count);
        return flag;
    }
}
