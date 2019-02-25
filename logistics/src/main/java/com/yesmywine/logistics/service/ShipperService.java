package com.yesmywine.logistics.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.logistics.entity.Shippers;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by wangdiandian on 2017/4/6.
 */
public interface ShipperService extends BaseService<Shippers,Integer> {
    String saveShipper(String jsonData) throws YesmywineException;//新增和修改保存承运商

    Shippers updateLoad(Integer id) throws YesmywineException;//加载承运商

    String delete(String jsonData) throws YesmywineException;//删除承运商

    Shippers shippersName(String shipperCode) throws YesmywineException;//根据承运商code显示承运商


}
