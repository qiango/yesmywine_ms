package com.yesmywine.goods.controller;

import com.yesmywine.goods.entity.PreSkuStatistics;
import com.yesmywine.goods.service.PreSkuStatisticsService;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by SJQ on 2017/7/13.
 */
@RestController
@RequestMapping("/goods/itf/presell/sku")
public class PreSkuController {

    @Autowired
    private PreSkuStatisticsService statisticsService;

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/7/13
    *@Param
    *@Description:获取预售商品sku
    */
    @RequestMapping(method = RequestMethod.GET)
    public String getSkuList(String skuCode){
        List<PreSkuStatistics> statisticsList = statisticsService.findSkuIsPresell(skuCode);
        Integer count = 0;
        for(PreSkuStatistics preSkuStatistics:statisticsList){
            Integer preCount = preSkuStatistics.getPreCount();
            Integer stockCount = preSkuStatistics.getStockCount();
            Integer balance = preCount - stockCount;
            count += balance;
        }
        return ValueUtil.toJson(count);
    }

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/7/13
    *@Param
    *@Description:更改预售商品sku进货数量
    */
    @RequestMapping(method = RequestMethod.POST)
    public String updateSkuStockCount(String skuCode,Integer count){
        String statisticsList = statisticsService.updateSkuStockCount(skuCode,count);
        return ValueUtil.toJson(HttpStatus.SC_CREATED,statisticsList);
    }
}
