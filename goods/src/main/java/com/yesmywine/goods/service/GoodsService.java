package com.yesmywine.goods.service;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entity.Goods;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;
import java.util.Map;

/**
 * Created by hz on 2/10/17.
 */
public interface GoodsService extends BaseService<Goods, Integer> {

    String addGoods(Map<String, String> param) throws YesmywineException;//新增福袋

//    com.alibaba.fastjson.JSONObject updateLoad(Integer goodsId) throws YesmywineException;//加载显示商品

    String delete(Integer goodsId) throws YesmywineException;//删除福袋

    String synchronous(Map<String,Object> map)throws YesmywineException;

    String synchronousProp(Map<String,Object> map)throws YesmywineException;

    String standDown(Map<String,String> map,String userName)throws YesmywineException;//申请上下架

    String checkStandDown(Map<String,String> map,String userName) throws Exception;//审核上下架

    String updateGoods(Map<String,String> map,String userName)throws YesmywineException;//编辑商品信息去审核

    String checkGoods(Map<String,String> map,String userName)throws YesmywineException;//审核商品信息

    Object  details(Integer goodsId) throws YesmywineException;//商品详情

    Object  combination(Integer goodsId, Integer size) throws YesmywineException;//商品组合购买

    Object  parameter(Integer goodsId)  throws YesmywineException;//商品参数

    String updateComment()throws YesmywineException;

    Object getSkuId(Integer goodsId) throws YesmywineException;

    Object cancelSkuId(Integer goodsId,String jsonArray)throws YesmywineException;

    String setBookGoods(Integer []goodsId,Integer []count,String[] price,String saleModel,String startTime,String endTime)throws YesmywineException;

    String synchronousGoods(Map<String,String> map)throws YesmywineException;

    List<Goods> findByOperateAndPreStatusAndEndTimeGt(Integer saleModel, Integer preStatus);

    com.alibaba.fastjson.JSONObject showOne(Integer goodsId)throws YesmywineException;

    com.alibaba.fastjson.JSONObject showMirrorOne(Integer goodsMirrorId)throws YesmywineException;

    Object categoryGoods(Integer cagetoryId, Integer pageNo, Integer pageSize);

    PageModel findByGoods(Integer pageNo, Integer pageSize,String goodsName_l)throws YesmywineException;

    String updateSales(String jsonString)throws YesmywineException;
}
