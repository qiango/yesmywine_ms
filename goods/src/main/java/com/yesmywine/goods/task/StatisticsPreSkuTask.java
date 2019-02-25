package com.yesmywine.goods.task;

import com.yesmywine.goods.entity.Goods;
import com.yesmywine.goods.entity.GoodsSku;
import com.yesmywine.goods.entity.PreSkuStatistics;
import com.yesmywine.goods.service.GoodsService;
import com.yesmywine.goods.service.PreSkuStatisticsService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by by on 2017/7/12.
 * 预售结束后，统计sku预售数量，以于进货
 */
@RestController
@RequestMapping("/goods/statisticsPreSku/task")
public class StatisticsPreSkuTask {
    @Autowired
    private PreSkuStatisticsService statisticsService;
    @Autowired
    private GoodsService goodsService;


    @RequestMapping(method = RequestMethod.GET)
    public String excuted() {
        System.out.println("=============================每分钟计算预售结束商品销售的sku数量 开始================================");
        List<Goods> goodsList = goodsService.findByOperateAndPreStatusAndEndTimeGt(1, 0);
        Map<String, Object> skuMap = new HashMap<>();
        for (Goods goods : goodsList) {
            List<GoodsSku> skuList = goods.getGoodsSku();
            for (GoodsSku goodsSku : skuList) {
                String skuCode = goodsSku.getCode();
                Integer skuCount = goodsSku.getCount() * (goods.getBooknumber() - goods.getRemainBooknumber());
                if (!skuMap.containsKey("skuCode")) {
                    skuMap.put(skuCode, skuCount);
                } else {
                    Integer count = (Integer) skuMap.get(skuCode);
                    skuMap.put(skuCode, count + skuCount);
                }
            }
        }
        List<PreSkuStatistics> statisticsList = new ArrayList<>();
        skuMap.forEach((k, v) -> {
            PreSkuStatistics skuStatistics = new PreSkuStatistics(k, (Integer) v, 0);
            statisticsList.add(skuStatistics);
        });
        statisticsService.save(statisticsList);

        goodsList.forEach(goods -> {
            goods.setPreStatus(1);
        });
        goodsService.save(goodsList);
        System.out.println("=============================每分钟计算预售结束商品销售的sku数量 结束================================");

        return ValueUtil.toJson("每分钟计算预售结束商品销售的sku数量    " + DateUtil.getNowTime());
    }
}
