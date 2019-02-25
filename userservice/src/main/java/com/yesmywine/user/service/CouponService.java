package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.Coupon;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;
import java.util.Map;

/**
 * Created by ${shuang} on 2017/4/13.
 */
public interface CouponService extends BaseService<Coupon,Integer> {

//    下架、查询、撤销
    String couponCreate(Map<String, String> params) throws YesmywineException;//生成
    String cancel(Integer couponId) throws YesmywineException;//下架
    String delete(Integer couponId ) throws YesmywineException;//删除
    List frontIndex(Integer pageNo,Integer pageSize) throws YesmywineException;//前台列表展示
    String audit(Integer couponId,Integer auditStatus,String remarks ) throws YesmywineException; //审核
    String filter(String couponIds,String username,String status)throws YesmywineException;

    void task()throws YesmywineException;
}
