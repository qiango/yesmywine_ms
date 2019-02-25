package com.yesmywine.ware.controller;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdicons.json.mapper.MapperException;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.db.base.biz.RedisCache;
import com.yesmywine.util.basic.JSONUtil;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.Constants;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.ware.entity.ChannelsInventory;
import com.yesmywine.ware.service.ChannelsInventoryService;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by SJQ on 2007/1/4.
 *
 * @Description: 渠道库存管理
 */
@RestController
@RequestMapping("/inventory/channelInventory")
public class ChannelsInventoryController {
    private static final Logger log = LoggerFactory
            .getLogger(ChannelsInventoryController.class);


    @Autowired
    private ChannelsInventoryService channelsInventoryService;

    /*
    *@Author Gavin
    *@Description 渠道库存查询（在channelInventory表中,PASS层后台使用）
    *@Date 2007/3/16 14:59
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,Integer skuId) {
//        if (params.get("channelId") != null && Utils.isNum(params.get("channelId").toString())) {
//            Integer channelId = Integer.valueOf(params.get("channelId").toString());
//            Channels channels = new Channels();
//            channels.setId(channelId);
//            params.remove(params.remove("channelId").toString());
//            params.put("channel", channels);
//        }
        MapUtil.cleanNull(params);
        if(skuId!=null){
            return ValueUtil.toJson(channelsInventoryService.findBySkuId(skuId));
        }

        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = channelsInventoryService.findAll(pageModel);
        return ValueUtil.toJson(pageModel);
    }

    /*
   *@Author SJQ
   *@Description 同步库存信息
   *@CreateTime
   *@Params
   */
    @RequestMapping(value = "/syn" , method = RequestMethod.POST)
    public String  synchronizetion(String jsonData){
        JSONObject jsonObject = JSON.parseObject(jsonData);
        String status = jsonObject.getString("msg");
        if(status.equals("stock")){
            stock(jsonData);
        }else if(status.equals("sub")){
            sub(jsonData);
        }else if(status.equals("releaseFreeze")){
            PAASReleaseFreeze(jsonData);
        }else if(status.equals("addEnRoute")){
            addEnRoute(jsonData);
        }else if(status.equals("subEnRoute")){
            subEnRoute(jsonData);
        }else if(status.equals("subToCommon")){
            subToCommon(jsonData);
        }else if(status.equals("freeze")){
//            freeze(jsonData);
        }else if(status.equals("addFreeze")){//预售sku直接增加冻结库存
            addFreeze(jsonData);
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED,"SUCCESS");
    }

    private String addFreeze(String jsonData) {
        try {
            String result = channelsInventoryService.addFreeze(jsonData);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,result);
        }catch (YesmywineException e){
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    private String subToCommon(String jsonData) {
        try {
            String result = channelsInventoryService.subToCommon(jsonData);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,result);
        }catch (YesmywineException e){
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    /*
    *@Author Gavin
    *@Description 同步paas入库
    *@Date 2017/4/12 17:16
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/stock",method = RequestMethod.POST)
    public String stock(String jsonData){
        try {
            String result = channelsInventoryService.stock(jsonData);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,result);
        }catch (YesmywineException e){
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 同步paas出库
    *@Date 2017/4/12 17:16
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/sub",method = RequestMethod.POST)
    public String sub(String jsonData){
        try {
            String result = channelsInventoryService.sale(jsonData);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,result);
        }catch (YesmywineException e){
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 商城冻结库存
    *@Date 2017/4/13 10:31
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/itf/freeze",method = RequestMethod.POST)
    public String freeze(Integer skuIds[],Integer [] counts, HttpServletRequest request){
        try {
            log.info(request.getRequestURL()+"  调用库存冻结");
            String result = channelsInventoryService.freeze(skuIds,counts);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,result);
        }catch (YesmywineException e){
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    public String PAASReleaseFreeze(String jsonData){
        JSONArray array = JSON.parseArray(ValueUtil.getFromJson(jsonData,"data"));
        for(int i=0;i<array.size();i++){
            JSONObject dataJSON = (JSONObject) array.get(i);
            Integer[] skuIds = new Integer[1];
            Integer[] counts = new Integer[1];
            skuIds[0] = Integer.valueOf(dataJSON.getString("skuId"));
            counts[0] = Integer.valueOf(dataJSON.getString("count"));
            releaseFreeze(skuIds,counts,null);
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED,"SUCCESS");
    }

    /*
    *@Author Gavin
    *@Description 商城释放冻结库存
    *@Date 2017/4/13 10:32
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/itf/releaseFreeze",method = RequestMethod.POST)
    public String releaseFreeze(Integer skuIds[],Integer [] counts, HttpServletRequest request){
        try {
            log.info(request.getRequestURL()+"  调用释放冻结库存");
            String result = channelsInventoryService.releaseFreeze(skuIds,counts);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,result);
        }catch (YesmywineException e){
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/7/6
    *@Param
    *@Description:增加在途库存
    */
    @RequestMapping(value = "/addEnRoute",method = RequestMethod.POST)
    public String addEnRoute(String jsonData){
        String result = channelsInventoryService.addEnRoute(jsonData);
        return ValueUtil.toJson(HttpStatus.SC_CREATED,result);
    }

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/7/6
    *@Param
    *@Description:扣减在途库存
    */
    @RequestMapping(value = "/subEnRoute",method = RequestMethod.POST)
    public String subEnRoute(String jsonData){
        String result = channelsInventoryService.subEnRoute(jsonData);
        return ValueUtil.toJson(HttpStatus.SC_CREATED,result);
    }

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/7/6
    *@Param
    *@Description: 库存是否充足
    */
    @RequestMapping(value = "/isInvEnough/itf")
    public String isInvEnough(Integer skuId,Integer count){
        ChannelsInventory result = channelsInventoryService.findBySkuId(skuId);
        Integer skuUseCount = result.getUseCount();
        if(skuUseCount<count){
            try {
                ValueUtil.isError("库存不足");
            } catch (YesmywineException e) {
                return ValueUtil.toError(e.getCode(),e.getMessage());
            }
        }
        return ValueUtil.toJson("SUCCESS");
    }

    /*
   *@Author SJQ
   *@Description 根据skuId查询商品库存
   *@CreateTime
   *@Params
   */
    @RequestMapping(value = "/skuInventory/itf", method = RequestMethod.GET)
    public String getSkuInventory(Integer skuId) {
        String result = RedisCache.get(Constants.SKU_ID + String.valueOf(skuId));
        if(result==null){
            ChannelsInventory list = channelsInventoryService.findBySkuId(skuId);
            return ValueUtil.toJson(list);
        }else{
            try {
                return ValueUtil.toJson(JSONUtil.jsonStrToObject(result,ChannelsInventory.class));
            } catch (MapperException e) {
                e.printStackTrace();
            } catch (TokenStreamException e) {
                e.printStackTrace();
            } catch (RecognitionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
