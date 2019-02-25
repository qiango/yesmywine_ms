package com.yesmywine.goods.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.dao.PreSkuStatisticsDao;
import com.yesmywine.goods.entity.PreSkuStatistics;
import com.yesmywine.goods.service.PreSkuStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by by on 2017/7/12.
 */
@Service
@Transactional
public class PreSkuStatisticsServiceImpl extends BaseServiceImpl<PreSkuStatistics,Integer> implements PreSkuStatisticsService{

    @Autowired
    private PreSkuStatisticsDao skuStatisticsDao;

    @Override
    public List<PreSkuStatistics> findByStockCountNotEqualPreCount() {
        return skuStatisticsDao.findByStockCountNotEqualsPreCount();
    }

    @Override
    public String updateSkuStockCount(String skuCode, Integer count) {
            Integer residueCount = 0;//剩余数量
            List<PreSkuStatistics> statisticsList = skuStatisticsDao.findSkuIsPresell(skuCode);
            for(PreSkuStatistics preSkuStatistics:statisticsList){
                Integer preCount = preSkuStatistics.getPreCount();
                Integer stockCount = preSkuStatistics.getStockCount();
                Integer balance = preCount - stockCount;
                count = count - balance;
                if(count >= 0){
                    preSkuStatistics.setStockCount(preSkuStatistics.getStockCount()+balance);
                }else{
                    preSkuStatistics.setStockCount(preSkuStatistics.getStockCount()-count);
                    break;
                }
            }
            skuStatisticsDao.save(statisticsList);
        return "SUCCESS";
    }

    @Override
    public List<PreSkuStatistics> findSkuIsPresell(String skuCode) {
        return skuStatisticsDao.findSkuIsPresell(skuCode);
    }
}
