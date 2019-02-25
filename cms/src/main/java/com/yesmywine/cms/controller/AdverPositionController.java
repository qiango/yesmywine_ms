package com.yesmywine.cms.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.cms.entity.AdverPositionEntity;
import com.yesmywine.cms.service.AdverPositionService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by yly on 2017/1/16.
 */
@RestController
@RequestMapping("/cms/adverPosition")
public class AdverPositionController {
    @Autowired
    private AdverPositionService adverPositionService;

    /**
     * 新增广告素材
     *
     * @param
     * @return3
     */
    @RequestMapping(method = RequestMethod.POST)
    public String saveAdver(AdverPositionEntity adverPositionEntity) {
        try {
            ValueUtil.verify(adverPositionEntity.getAdverEntity(), "adver");
            ValueUtil.verify(adverPositionEntity.getPositionEntity(), "position");
            String result = adverPositionService.saveAP(adverPositionEntity);
            if("success".equals(result)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, result);
            }
            return ValueUtil.toError("500",result);

        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ValueUtil.toError("500","erro");

    }

    /**
     * 删除广告位中的广告素材
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteAdver(Integer adverId, Integer positionId) {
        try {
            ValueUtil.verify(adverId, "adverId");
            ValueUtil.verify(positionId, "positionId");
            String s = adverPositionService.deleteAP(adverId, positionId);
            if("success".equals(s)){
                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, "success");
            }
            return ValueUtil.toError("500", s);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ValueUtil.toError("500", "erro");
        }
    }


    /**
     * 修改广告素材
     *
     * @return
     */
    @RequestMapping( method = RequestMethod.PUT)
    public String updateAdver(AdverPositionEntity adverPositionEntity) {
        try {
            ValueUtil.verify(adverPositionEntity.getId(), "id");
            ValueUtil.verify(adverPositionEntity.getAdverEntity(), "adver");
            ValueUtil.verify(adverPositionEntity.getPositionEntity(), "position");
            String result = adverPositionService.saveAP(adverPositionEntity);
            if("success".equals(result)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, result);
            }

        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ValueUtil.toError("500","erro");
    }


    @RequestMapping(method = RequestMethod.GET)
    public String page(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize) {

        if(ValueUtil.notEmpity(params.get("id"))){
            return ValueUtil.toJson(adverPositionService.findOne(Integer.valueOf(params.get("id").toString())));
        }else if("true".equals(params.get("all"))) {
            return ValueUtil.toJson(adverPositionService.findAll());
        }if(ValueUtil.notEmpity(params.get("adverEntity"))){
            return ValueUtil.toJson(HttpStatus.SC_OK,this.adverPositionService.findByAdverEntity(Integer.valueOf(params.get("adverEntity").toString())));
        }if(ValueUtil.notEmpity(params.get("positionEntity"))){
            return ValueUtil.toJson(HttpStatus.SC_OK,this.adverPositionService.findByPositionEntity(Integer.valueOf(params.get("positionEntity").toString())));
        }

        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        return ValueUtil.toJson(adverPositionService.findAll(pageModel));
    }

}
