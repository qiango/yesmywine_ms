package com.yesmywine.user.service;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.StoreWine;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by ${shuang} on 2017/8/10.
 */
public interface StoreWineService extends BaseService<StoreWine, Integer> {

    String storage(Integer userId, String json) throws YesmywineException;//存酒

    String show(Integer userId, String json);//提酒价格

    String extract(String json, String extractorderNumber, Integer userId) throws YesmywineException;//提酒;

    String comfirm(String json, String extractorderNumber, Integer userId) throws YesmywineException;//支付确认

    String cancel(String json, String extractorderNumber, Integer userId) throws YesmywineException;//订单取消

    PageModel page(PageModel pageModel) throws YesmywineException;

    String returns(String json, String returnOrderNumber, Integer userId) throws YesmywineException; //退货

    String returnComfirm(String json, String returnOrderNumber, Integer userId) throws YesmywineException;//退货确认

    String returnShow(Integer userId, String json);//退货价格展示

    String returnComfirmcancel(String json, String returnOrderNumber, Integer userId) throws YesmywineException;//退货审核不通过
}
