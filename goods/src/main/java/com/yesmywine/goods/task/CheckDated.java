//package com.hzbuvi.goods.task;
//
//import com.hzbuvi.goods.entity.GiftCardOrder;
//import com.hzbuvi.goods.service.GiftCardOrderService;
//import GiftCardService;
//import DateUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by SJQ on 2016/12/26.
// *
// * @Description:定时清除过期礼品卡
// */
////@Component
//public class CheckDated {
//
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void excuted() {
//
//
//        Date nowTime = new Date();
//        String now = DateUtil.toString(nowTime, "yyyy-MM-dd");
//        List<GiftCardOrder> co = giftCardOrderService.findDated(now);
//
//
//    }
//
//    @Autowired
//    private GiftCardOrderService giftCardOrderService;
//}
