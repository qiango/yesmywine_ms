package com.yesmywine.cms.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.cms.service.AdverService;
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
@RequestMapping("/cms/adver")
public class AdverController {
    @Autowired
    private AdverService adverService;

    /**
     * 新增广告素材
     *
     * @param
     * @return3
     */
    @RequestMapping(method = RequestMethod.POST)
    public String saveAdver(@RequestParam Map<String, String> params) {
        try {
            ValueUtil.verify(params.get("adverName"), "adverName");
            ValueUtil.verify(params.get("remark"), "remark");
            ValueUtil.verify(params.get("width"), "width");
            ValueUtil.verify(params.get("height"), "height");
            ValueUtil.verify(params.get("inOrOut"), "inOrOut");
            ValueUtil.verify(params.get("relevancy"), "relevancy");
            ValueUtil.verify(params.get("startTime"), "startTime");
            ValueUtil.verify(params.get("endTime"), "endTime");
            String update = adverService.saveAdver(params);
            if("success".equals(update)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, update);
            }
            return ValueUtil.toError("500", update);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }

    /**
     * 删除广告素材
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteAdver(Integer adverId) {
        try {
            ValueUtil.verify(adverId, "adverId");
            String delete = adverService.delete(adverId);
            if("success".equals(delete)){
                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, "success");
            }
            return ValueUtil.toError("500", delete);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return ValueUtil.toError("500", "已被使用");
        }
    }


    /**
     * 修改广告素材
     *
     * @return
     */
    @RequestMapping( method = RequestMethod.PUT)
    public String updateAdver(@RequestParam Map<String, String> params) {
        try {
            ValueUtil.verify(params.get("id"), "id");
            ValueUtil.verify(params.get("adverName"), "adverName");
            ValueUtil.verify(params.get("remark"), "remark");
            ValueUtil.verify(params.get("width"), "width");
            ValueUtil.verify(params.get("height"), "height");
            ValueUtil.verify(params.get("inOrOut"), "inOrOut");
            ValueUtil.verify(params.get("relevancy"), "relevancy");
            ValueUtil.verify(params.get("startTime"), "startTime");
            ValueUtil.verify(params.get("endTime"), "endTime");
            String update = adverService.update(params);
            if("success".equals(update)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, update);
            }
            return ValueUtil.toError("500", update);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMsg());
        }
    }

//    /**
//     * 根据广告素材ID查询广告素材信息
//     *
//     * @param adverVo
//     * @return
//     */
//    @RequestMapping(value = "/query", method = RequestMethod.GET)
//    public String queryAdver(AdverVo adverVo) {
//        return ValueUtil.toJson(adverService.findAdver(adverVo));
//    }


//    /**
//     * 查询所有广告素材
//     *
//     * @param params
//     * @return
//     */
//    @RequestMapping(value = "/index", method = RequestMethod.GET)
//    public String index(@RequestParam Map<String, Object> params) {
//        return ValueUtil.toJson(adverService.findAll(new PageModel(params)));
//    }

    /**
     * 查询所有广告素材并分页
     *
     * @param params
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String page(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize) {

        if(ValueUtil.notEmpity(params.get("adverId"))){
            String adverId =(String) params.get("adverId");
            try {
                return ValueUtil.toJson(adverService.page(adverId));
            } catch (YesmywineException e) {
                return ValueUtil.toError(e.getCode(), e.getMessage());
            }
        }else if("true".equals(params.get("all"))) {
            return ValueUtil.toJson(adverService.findAll());
        }

        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel.addCondition(params);
        pageModel = adverService.findAll(pageModel);
        return ValueUtil.toJson(adverService.findAll(pageModel));
    }

}
