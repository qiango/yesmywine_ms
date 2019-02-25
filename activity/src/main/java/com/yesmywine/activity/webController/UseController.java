package com.yesmywine.activity.webController;

import com.yesmywine.activity.service.UseService;
import com.yesmywine.db.base.ehcache.CacheStatement;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by SJQ on 2017/5/10.
 */
@RestController
@RequestMapping("/web/activity")
public class UseController {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private UseService useService;

    /*
    *@Author Gavin
    *@Description 购物车调用并计算费用
    *@Date 2017/6/15 9:49
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/cart", method = RequestMethod.POST)
    public String use(String jsonData,String username) {
        try {
            return ValueUtil.toJson(useService.runCart(jsonData,username));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String use(Double totalPrice) {
        try {
            return ValueUtil.toJson(useService.runOrder(totalPrice));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }
    /*
    *@Author Gavin
    *@Description  查看单个商品活动信息
    *@Date 2017/6/15 9:48
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public String single(@RequestParam Map<String, String> params) {
        try {
            return ValueUtil.toJson(useService.runSingle(params));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }

    /*
    *@Author Gavin
    *@Description 根据活动Id查看活动中的所有商品
    *@Date 2017/6/15 9:49
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/activityGoods", method = RequestMethod.GET)
    public String single(Integer activityId,Integer pageNo,Integer pageSize) {
        return ValueUtil.toJson(useService.getActivityGoods(activityId, pageNo, pageSize));
    }

    /*
    *@Author Gavin
    *@Description 查看活动剩余时间
    *@Date 2017/6/15 9:49
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping( method = RequestMethod.GET)
    public String activityInfo(Integer activityId, Integer pageNo, Integer pageSize,String test) {
        return ValueUtil.toJson(useService.getActivityInfo(activityId,  pageNo,  pageSize));
    }

    /*
    *@Author Gavin
    *@Description 清除所有缓存
    *@Date 2017/6/15 9:49
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping( value = "/cleanCache",method = RequestMethod.GET)
    public String cleanCache() {
        cacheManager.clearAllStartingWith(CacheStatement.ACTIVITY_VALUE);
        return ValueUtil.toJson("");//清除活动所有缓存);
    }
}
