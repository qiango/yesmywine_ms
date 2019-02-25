package com.yesmywine.ware.controller;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.jwt.customPerm.SecurestValid;
import com.yesmywine.util.basic.JSONUtil;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.ware.entity.Channels;
import com.yesmywine.ware.service.ChannelsService;
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
 * Created by SJQ on 2007/1/5.
 *
 * @Description:渠道管理
 */
@RestController
@RequestMapping("/inventory/channels")
public class ChannelsController {
    @Autowired
    private ChannelsService channelsService;

    /*
    *@Author SJQ
    *@Description 渠道列表
    *@CreateTime
    *@Params
    */
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer id) {
        try {
            MapUtil.cleanNull(params);
            if (id != null) {
                Channels channels = channelsService.findOne(id);
                ValueUtil.verifyNotExist(channels, "该渠道不存在！");
                return ValueUtil.toJson(channels);
            }

            if (null != params.get("all") && params.get("all").toString().equals("true")) {
                return ValueUtil.toJson(channelsService.findAll());
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
            pageModel = channelsService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /*
    *@Author SJQ
    *@Description 同步渠道信息
    *@CreateTime
    *@Params
    */
    @RequestMapping(value = "/syn", method = RequestMethod.POST)
    public String synchronizetion(String jsonData) {
        try {
            JSONObject jsonObject = JSON.parseObject(jsonData);
            String status = jsonObject.getString("msg");
            if (status.equals("save")) {
                create(jsonData);

            } else if (status.equals("update")) {
                update(jsonData);
            } else if (status.equals("delete")) {
                delete(jsonData);
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, "SUCCESS");
        } catch (YesmywineException e) {
            e.printStackTrace();
        } catch (RecognitionException e) {
            e.printStackTrace();
        } catch (MapperException e) {
            e.printStackTrace();
        } catch (TokenStreamException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    *@Author SJQ
    *@Description 创建渠道
    *@CreateTime
    *@Params
    */
    public String create(String jsonData) throws YesmywineException, RecognitionException, MapperException, TokenStreamException, ParseException {
        JSONObject result = (JSONObject) JSON.parseObject(jsonData);

        JSONObject channelJson = result.getJSONObject("data");
        String id = channelJson.getString("id");
        String channelName = channelJson.getString("channelName");
        String channelCode = channelJson.getString("channelCode");
        String type = channelJson.getString("type");
        String comment = channelJson.getString("comment");
        String ifSale = channelJson.getString("ifSale");
        String ifInventory = channelJson.getString("ifInventory");
        String ifProcurement = channelJson.getString("ifProcurement");
        Boolean canDelete = channelJson.getBoolean("canDelete");
        JSONObject parentChannelJson = channelJson.getJSONObject("parentChannel");
        Channels channels = new Channels();
        if(parentChannelJson!=null){
            String parentChannelId = parentChannelJson.getString("id");

            if (null != parentChannelId && !parentChannelId.equals("")) {
                Integer parentId = Integer.valueOf(parentChannelId);
                Channels parentChannel = channelsService.findOne(parentId);
                channels.setParentChannel(parentChannel);
            }
        }
        channels.setId(Integer.valueOf(id));
        channels.setChannelCode(channelCode);
        channels.setChannelName(channelName);
        channels.setType(Integer.valueOf(type));
        channels.setComment(comment);
        channels.setIfInventory(Boolean.valueOf(ifInventory).booleanValue());
        channels.setIfProcurement(Boolean.valueOf(ifProcurement).booleanValue());
        channels.setIfSale(Boolean.valueOf(ifSale).booleanValue());
        channels.setCanDelete(canDelete);

//            JSONObject dataJSON = JSON.parseObject(jsonData);
//            String code = dataJSON.getString("code");
//            if(!code.equals("201")){
//                ValueUtil.isError("传输参数格式有误");
//            }
//            JSONObject channelJSON = dataJSON.getJSONObject("data");
//            String createTime = channelJSON.getString("createTime");
//            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
//            SimpleDateFormat sdf_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//            Date d2 = sdf.parse(createTime);
//            channelJSON.put("createTime",sdf_str.format(d2));
//            Object obj = JSONUtil.jsonStrToObject(channelJSON.toJSONString(),Channels.class);
//            Channels channels = (Channels)obj;

        channelsService.save(channels);
        return ValueUtil.toJson(HttpStatus.SC_CREATED, channels);
    }


    /*
    *@Author SJQ
    *@Description 修改渠道
    *@CreateTime
    *@Params
    */
    public String update(String jsonData) throws YesmywineException, RecognitionException, MapperException, TokenStreamException, ParseException {
//            jsonData = UriEncoder.decode(jsonData);
        JSONObject result = (JSONObject) JSON.parseObject(jsonData);
        String code = result.getString("code");
        JSONObject channelJson = result.getJSONObject("data");
        String id = channelJson.getString("id");
        String channelName = channelJson.getString("channelName");
        String channelCode = channelJson.getString("channelCode");
        String type = channelJson.getString("type");
        String comment = channelJson.getString("comment");
        String ifSale = channelJson.getString("ifSale");
        String ifInventory = channelJson.getString("ifInventory");
        String ifProcurement = channelJson.getString("ifProcurement");
        Boolean canDelete = channelJson.getBoolean("canDelete");
        JSONObject parentChannelJson = channelJson.getJSONObject("parentChannel");
        Channels channels = new Channels();
        if(parentChannelJson!=null){
            String parentChannelId = parentChannelJson.getString("id");
            if (null != parentChannelId && !parentChannelId.equals("")) {
                Integer parentId = Integer.valueOf(parentChannelId);
                Channels parentChannel = channelsService.findOne(parentId);
                channels.setParentChannel(parentChannel);
            }
        }

        channels.setId(Integer.valueOf(id));
        channels.setChannelCode(channelCode);
        channels.setChannelName(channelName);
        channels.setType(Integer.valueOf(type));
        channels.setComment(comment);
        channels.setIfInventory(Boolean.valueOf(ifInventory).booleanValue());
        channels.setIfProcurement(Boolean.valueOf(ifProcurement).booleanValue());
        channels.setIfSale(Boolean.valueOf(ifSale).booleanValue());
        channels.setCanDelete(canDelete);

//            JSONObject dataJSON = JSON.parseObject(jsonData);
//            String code = dataJSON.getString("code");
//            if(!code.equals("201")){
//                ValueUtil.isError("传输参数格式有误");
//            }
//            JSONObject channelJSON = dataJSON.getJSONObject("data");
//            String createTime = channelJSON.getString("createTime");
//            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
//            SimpleDateFormat sdf_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//            Date d2 = sdf.parse(createTime);
//            channelJSON.put("createTime",sdf_str.format(d2));
//            Object obj = JSONUtil.jsonStrToObject(channelJSON.toJSONString(),Channels.class);
//            Channels channels = (Channels)obj;
//            if(channels==null){
//                return null;
//            }
        channelsService.save(channels);
        return ValueUtil.toJson(HttpStatus.SC_CREATED, channels);
    }


    /*
    *@Author SJQ
    *@Description 渠道删除
    *@CreateTime
    *@Params
    */
    public String delete(String jsonData) throws YesmywineException {
        JSONObject result = (JSONObject) JSON.parseObject(jsonData);
        String code = result.getString("code");
        if (!code.equals("201")) {
            ValueUtil.isError("传输参数格式有误");
        }
        String id = result.getString("data");
        ValueUtil.verify(id);
        channelsService.delete(Integer.valueOf(id));
        return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, "channels");
    }


}
