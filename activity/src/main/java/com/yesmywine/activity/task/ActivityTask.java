package com.yesmywine.activity.task;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.activity.entity.Activity;
import com.yesmywine.activity.entity.RegulationGoods;
import com.yesmywine.activity.service.ActivityService;
import com.yesmywine.activity.service.RegulationGoodsService;
import com.yesmywine.activity.service.RegulationService;
import com.yesmywine.activity.service.UseService;
import com.yesmywine.activity.thread.ChangeGoosInfoThread;
import com.yesmywine.activity.thread.RestoreInventoryThread;
import com.yesmywine.db.base.ehcache.CacheStatement;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.date.DateUtil;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by SJQ on 2017/6/13.
 */
@RestController
@RequestMapping("/activity/task")
public class ActivityTask {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UseService useService;
    @Autowired
    private RegulationService regulationService;
    @Autowired
    private RegulationGoodsService regulationGoodsService;

    @Autowired
    private CacheManager cacheManager;

    @RequestMapping(method = RequestMethod.GET)
    public String excuted() {
//        if (true) {
            System.out.println("=============================每分钟判断活动状态定时任务 开始================================");
//            return ValueUtil.toJson("SUCCESS");
//        }
        //判断需要启动的活动
        List<Activity> startActivityList = activityService.findByIsDeleteAndStatusAndAuditStatus(DeleteEnum.NOT_DELETE, ActivityStatus.notCurrent,1);
        Date now = new Date();
        for(Activity activity:startActivityList){
            Date startTime = activity.getStartTime();
            if(now.getTime()>startTime.getTime()){
                System.out.println("=============================启动活动:"+activity.getId()+"_"+activity.getName()+"================================");
                activity.setStatus(ActivityStatus.current);
                activityService.startActivity(activity);
                if(activity.getType().equals("nullT-rushPurA")){
                    changeGoodsPrice(activity);
                    List<RegulationGoods> goodsList = regulationGoodsService.findByActivityId(activity.getId());
                    String goodsIds = "";
                    String priceArr = "";
                    String countArr = "";
                    for(RegulationGoods goods:goodsList){
                        String goodsId = String.valueOf(goods.getTargetId());
                        String price = String.valueOf(goods.getActivityPrice());
                        String count = String.valueOf(goods.getAllLimitCount());
                        goodsIds +=goodsId +",";
                        priceArr +=price +",";
                        countArr += count +",";
                    }
                    goodsIds = goodsIds.substring(0,goodsIds.length()-1);
                    priceArr = priceArr.substring(0,priceArr.length()-1);
                    countArr = countArr.substring(0,countArr.length()-1);
                    ChangeGoosInfoThread runnable = new ChangeGoosInfoThread(goodsIds,priceArr,countArr,"2",activity);
                    Thread thread = new Thread(runnable);
                    thread.start();
                }

                cacheManager.clearAllStartingWith(CacheStatement.ACTIVITY_VALUE);//清除活动所有缓存
                System.out.println("=============================启动成功:"+activity.getId()+"_"+activity.getName()+"================================");
            }
        }

        //判断结束的活动
        List<Activity> endActivityList = activityService.findByIsDeleteAndStatusAndAuditStatus(DeleteEnum.NOT_DELETE, ActivityStatus.current,1);
        for(Activity activity:endActivityList){
            Date endTime = activity.getEndTime();
            if(now.getTime()>endTime.getTime()){
                System.out.println("=============================停止活动:"+activity.getId()+"_"+activity.getName()+"================================");
                //结束活动
                activity.setStatus(ActivityStatus.overdue);
                activityService.endActivity(activity);
                if(activity.getType().equals("nullT-rushPurA")){//如果为抢购，需要还原商品原价，并释放剩余冻结库存
                    List<RegulationGoods> goodsList = regulationGoodsService.findByActivityId(activity.getId());
                    String goodsIds = "";
                    String priceArr = "";
                    String countArr = "";
                    for(RegulationGoods goods:goodsList){
                        String goodsId = String.valueOf(goods.getTargetId());
                        String price = String.valueOf(goods.getOriginPrice());
                        String count = String.valueOf(goods.getAllLimitCount());
                        goodsIds +=goodsId +",";
                        priceArr +=price +",";
                        countArr += count +",";
                    }
                    goodsIds = goodsIds.substring(0,goodsIds.length()-1);
                    priceArr = priceArr.substring(0,priceArr.length()-1);
                    countArr = countArr.substring(0,countArr.length()-1);
                    //还原冻结库存  并通知cms删除活动商品
                    RestoreInventoryThread restoreThread = new RestoreInventoryThread(goodsIds,activity);
                    Thread restore_Thread = new Thread(restoreThread);
                    restore_Thread.start();

                    ChangeGoosInfoThread runnable = new ChangeGoosInfoThread(goodsIds,priceArr,countArr,"0",activity);
                    Thread thread = new Thread(runnable);
                    thread.start();

                }
                cacheManager.clearAllStartingWith(CacheStatement.ACTIVITY_VALUE);//清除活动所有缓存
                System.out.println("=============================停止成功:"+activity.getId()+"_"+activity.getName()+"================================");
            }
        }
        System.out.println("=============================每分钟判断活动状态定时任务 结束================================");
        return ValueUtil.toJson("每分钟判断活动状态定时任务    "+ DateUtil.getNowTime());

    }

    private void changeGoodsPrice(Activity activity) {
    }


}
