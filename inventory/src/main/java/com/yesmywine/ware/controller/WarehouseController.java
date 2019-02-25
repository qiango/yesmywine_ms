package com.yesmywine.ware.controller;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.util.basic.JSONUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.ware.entity.Warehouses;
import com.yesmywine.ware.service.WarehouseService;
import com.sdicons.json.mapper.MapperException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by SJQ on 2017/1/4.
 *
 * @Description: 仓库管理
 */
@RestController
@RequestMapping("/inventory/warehouses")
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    /*
    *@Author SJQ
    *@Description 仓库列表
    *@CreateTime
    *@Params
    */
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer id) {
        try {

            if(id != null){
                Warehouses warehouse = warehouseService.findOne(id);
                ValueUtil.verifyNotExist(warehouse,"该仓库不存在");
                return ValueUtil.toJson(warehouse);
            }

            if (null != params.get("all") && params.get("all").toString().equals("true")) {
                return ValueUtil.toJson(warehouseService.findAll());
            } else if (null != params.get("all")) {
                params.remove(params.remove("all").toString());
            }
            PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
            if (null != params.get("showFields")) {
                pageModel.setFields(params.remove("showFields").toString());
            }
            if (pageNo != null) params.remove(params.remove("pageNo").toString());
            if (pageSize != null) params.remove(params.remove("pageSize").toString());
            pageModel.addCondition(params);
            pageModel = warehouseService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /*
   *@Author SJQ
   *@Description 同步仓库信息
   *@CreateTime
   *@Params
   */
    @RequestMapping(value = "/syn",method = RequestMethod.POST)
    public String  synchronizetion(String jsonData){
        try {
        JSONObject jsonObject = JSON.parseObject(jsonData);
        String status = jsonObject.getString("msg");
        if(status.equals("save")){
            create(jsonData);
        }else if(status.equals("update")){
            update(jsonData);
        }else if(status.equals("delete")){
            delete(jsonData);
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED,"SUCCESS");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (RecognitionException e) {
            e.printStackTrace();
        } catch (MapperException e) {
            e.printStackTrace();
        } catch (TokenStreamException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    *@Author SJQ
    *@Description 创建仓库
    *@CreateTime
    *@Params
    */
    public String create( String jsonData) throws ParseException, RecognitionException, MapperException, TokenStreamException {
        try {
            JSONObject result = (JSONObject) JSON.parseObject(jsonData);
            String code = result.getString("code");
            if(!code.equals("201")){
                ValueUtil.isError("传输参数格式有误");
            }
            JSONObject warehouseJson = result.getJSONObject("data");
//            String id = warehouseJson.getString("id");
//            String warehouseCode = warehouseJson.getString("warehouseCode");
//            String warehouseName = warehouseJson.getString("warehouseName");
//            String warehouseProvince = warehouseJson.getString("warehouseProvince");
//            String warehouseCity = warehouseJson.getString("warehouseCity");
//            String warehouseAddress = warehouseJson.getString("warehouseAddress");
//            String type = warehouseJson.getString("type");
//            String contactName = warehouseJson.getString("contactName");
//            String telephone = warehouseJson.getString("telephone");
//            String phone = warehouseJson.getString("phone");
//            String fax = warehouseJson.getString("fax");
//            String email = warehouseJson.getString("email");
//            String comment = warehouseJson.getString("comment");
//            String status = warehouseJson.getString("status");
//
//            Warehouses warehouse = new Warehouses();
//            warehouse.setId(Integer.valueOf(id));
//            warehouse.setWarehouseCode(warehouseCode);
//            warehouse.setWarehouseName(warehouseName);
//            warehouse.setWarehouseProvince(warehouseProvince);
//            warehouse.setWarehouseCity(warehouseCity);
//            warehouse.setWarehouseAddress(warehouseAddress);
//            warehouse.setType(Integer.valueOf(type));
//            warehouse.setContactName(contactName);
//            warehouse.setTelephone(telephone);
//            warehouse.setPhone(phone);
//            warehouse.setFax(fax);
//            warehouse.setEmail(email);
//            warehouse.setComment(comment);
//            warehouse.setStatus(status);

            JSONObject channelJSON = result.getJSONObject("data");
            String createTime = channelJSON.getString("createTime");
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
            SimpleDateFormat sdf_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date d2 = sdf.parse(createTime);
            channelJSON.put("createTime",sdf_str.format(d2));
            Object obj = JSONUtil.jsonStrToObject(channelJSON.toJSONString(),Warehouses.class);
            Warehouses warehouse = (Warehouses)obj;
            if(warehouse==null){
                return null;
            }
            warehouseService.save(warehouse);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, warehouse);
        }catch (YesmywineException e){
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    /*
    *@Author SJQ
    *@Description 修改仓库
    *@CreateTime
    *@Params
    */
    public String update( String jsonData) throws ParseException, RecognitionException, MapperException, TokenStreamException {
        try {
            JSONObject result = (JSONObject) JSON.parseObject(jsonData);
            String code = result.getString("code");
            if(!code.equals("201")){
                ValueUtil.isError("传输参数格式有误");
            }
            JSONObject warehouseJson = result.getJSONObject("data");
//            String id = warehouseJson.getString("id");
//            String warehouseCode = warehouseJson.getString("warehouseCode");
//            String warehouseName = warehouseJson.getString("warehouseName");
//            String warehouseProvince = warehouseJson.getString("warehouseProvince");
//            String warehouseCity = warehouseJson.getString("warehouseCity");
//            String warehouseAddress = warehouseJson.getString("warehouseAddress");
//            String type = warehouseJson.getString("type");
//            String contactName = warehouseJson.getString("contactName");
//            String telephone = warehouseJson.getString("telephone");
//            String phone = warehouseJson.getString("phone");
//            String fax = warehouseJson.getString("fax");
//            String email = warehouseJson.getString("email");
//            String comment = warehouseJson.getString("comment");
//            String status = warehouseJson.getString("status");
//
//            Warehouses warehouse = new Warehouses();
//            warehouse.setId(Integer.valueOf(id));
//            warehouse.setWarehouseCode(warehouseCode);
//            warehouse.setWarehouseName(warehouseName);
//            warehouse.setWarehouseProvince(warehouseProvince);
//            warehouse.setWarehouseCity(warehouseCity);
//            warehouse.setWarehouseAddress(warehouseAddress);
//            warehouse.setType(Integer.valueOf(type));
//            warehouse.setContactName(contactName);
//            warehouse.setTelephone(telephone);
//            warehouse.setPhone(phone);
//            warehouse.setFax(fax);
//            warehouse.setEmail(email);
//            warehouse.setComment(comment);
//            warehouse.setStatus(status);

            JSONObject channelJSON = result.getJSONObject("data");
            String createTime = channelJSON.getString("createTime");
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
            SimpleDateFormat sdf_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date d2 = sdf.parse(createTime);
            channelJSON.put("createTime",sdf_str.format(d2));
            Object obj = JSONUtil.jsonStrToObject(channelJSON.toJSONString(),Warehouses.class);
            Warehouses warehouse = (Warehouses)obj;

            warehouseService.save(warehouse);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, warehouse);
        }catch (YesmywineException e){
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    /*
    *@Author SJQ
    *@Description 删除仓库
    *@CreateTime
    *@Params
    */
    public String delete(String jsonData) {
        try {
            JSONObject result = (JSONObject) JSON.parseObject(jsonData);
            String code = result.getString("code");
            if(!code.equals("201")){
                ValueUtil.isError("传输参数格式有误");
            }
            String id = result.getString("data");
            warehouseService.delete(Integer.valueOf(id));
            return ValueUtil.toJson(HttpStatus.SC_CREATED, "warehouse");
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
}
