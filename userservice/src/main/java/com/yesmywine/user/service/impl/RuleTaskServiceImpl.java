package com.yesmywine.user.service.impl;

import com.yesmywine.user.service.RuleTaskService;
import com.yesmywine.user.service.UserLevelService;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Created by ${shuang} on 2017/2/13.
 */
@Service
public class RuleTaskServiceImpl implements RuleTaskService {
    @Autowired
    private UserLevelService userLevelService;

    @Scheduled(cron = "0 00 00 ? * *" )//每天凌晨触发点触发
    public void doSomethingWith() {

//        等级自动处理任务
        System.out.println("定时任务开始......");
        long begin = System.currentTimeMillis();
        try {
           userLevelService.voluntarily();

        } catch (YesmywineException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("定时任务结束，共耗时：[" + (end - begin) / 1000 + "]秒");
    }
}
