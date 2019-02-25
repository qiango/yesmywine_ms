package com.yesmywine.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.cms.dao.PositionEntityDao;
import com.yesmywine.cms.entity.PositionEntity;
import com.yesmywine.cms.service.AdverPositionService;
import com.yesmywine.cms.service.PositionService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yly on 2017/1/16.
 */
@RestController
@RequestMapping("/cms/position")
public class PositionController {
    @Autowired
    private PositionService positionService;
    @Autowired
    private AdverPositionService adverPositionService;
    @Autowired
    private PositionEntityDao posDao;

    /**
     * 新增广告位
     *
     * @param position
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public String savePosition(PositionEntity position, String adverIds) {
        try {
            ValueUtil.verify(position.getPositionName(), "positionName");
            ValueUtil.verify(position.getPositionType(), "positionType");
            ValueUtil.verify(position.getPositionDesc(), "positionDesc");
            ValueUtil.verify(position.getWidth(), "width");
            ValueUtil.verify(position.getHeight(), "height");
            ValueUtil.verify(adverIds, "adverIds");
            String result = positionService.savePosition(position, adverIds);
            if("success".equals(result)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED,result);
            }
            return ValueUtil.toError("500",result);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }



    @RequestMapping(method = RequestMethod.DELETE)
    public String deletePosition(Integer positionId) {
        try {
            ValueUtil.verify(positionId, "positionId");
            String delete = positionService.delete(positionId);
            if("success".equals(delete)) {
                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, delete);
            }else {
                return ValueUtil.toError("500", delete);
            }
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ValueUtil.toError("500", "已被使用");
        }
    }



    @RequestMapping(method = RequestMethod.PUT)
    public String updatePosition(PositionEntity position, String adverIds) {
        try {
            ValueUtil.verify(position.getPositionName(), "positionName");
            ValueUtil.verify(position.getPositionType(), "positionType");
            ValueUtil.verify(position.getPositionDesc(), "positionDesc");
            ValueUtil.verify(position.getWidth(), "width");
            ValueUtil.verify(position.getHeight(), "height");
            ValueUtil.verify(position.getStatus(), "status");
            ValueUtil.verify(position.getId(), "id");
            ValueUtil.verify(adverIds, "adverIds");
            String code = positionService.update(position, adverIds);
            if("success".equals(code)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, code);
            }else {
                return ValueUtil.toError("500", code);
            }
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }


//    /**
//     * 根据广告位ID查询广告位信息
//     *
//     * @param positionId
//     * @return
//     */
//    @RequestMapping(value = "/query/{positionId}")
//    public String queryPosition(@PathVariable("positionId") Integer positionId) {
//        try {
//            ValueUtil.verify(positionId);
//            return ValueUtil.toJson(positionService.findOne(positionId));
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(), e.getMessage());
//        }
//    }


//    /**
//     * 查询所有广告位
//     *
//     * @param params
//     * @return
//     */
//    @RequestMapping(method = RequestMethod.GET)
//    public String index(@RequestParam Map<String, Object> params) {
//        if(ValueUtil.notEmpity(params.get("positionId"))){
//            return ValueUtil.toJson(positionService.findOne(Integer.valueOf(params.get("positionId").toString())));
//        }else {
//            return ValueUtil.toJson(positionService.findAll(new PageModel(params)));
//        }
//    }


    /**
     * 查询所有广告位并分页
     *
     * @param params
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize) {

        if(ValueUtil.notEmpity(params.get("positionId"))){
            return ValueUtil.toJson(this.adverPositionService.findByPositionEntityShowAdver(Integer.valueOf(params.get("positionId").toString())));
        }else if("true".equals(params.get("all"))) {
            String positionType =(String) params.get("positionType");
            String status = (String)params.get("status");
            List<PositionEntity> list = new ArrayList<>();
            if(ValueUtil.notEmpity(positionType)&&ValueUtil.isEmpity(status)){
                int positionType1 =Integer.valueOf(positionType);
                list =  posDao.findByPositionType(positionType1);//通过type查所有的
            }else if(ValueUtil.isEmpity(positionType)&&ValueUtil.notEmpity(status)){
                int status1 =Integer.valueOf(status);
                list =  posDao.findByStatus(status1);//通过status查所有的
            }else if(ValueUtil.notEmpity(positionType)&&ValueUtil.notEmpity(status)){
                int positionType1 =Integer.valueOf(positionType);
                int status1 =Integer.valueOf(status);
                list =  posDao.findByPositionTypeAndStatus(positionType1,status1);//通过type，status查
            }else if (ValueUtil.isEmpity(positionType)&&ValueUtil.isEmpity(status)){
                list =  posDao.findAll();//列表
            }
            JSONObject jsonObject=new JSONObject();
                jsonObject.put("content",list);
                return ValueUtil.toJson(jsonObject);
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (params.get("showFields") != null) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = positionService.findAll(pageModel);
        return ValueUtil.toJson(pageModel);
    }


}
