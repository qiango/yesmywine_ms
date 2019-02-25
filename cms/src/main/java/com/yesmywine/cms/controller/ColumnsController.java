package com.yesmywine.cms.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.cms.service.ColumnService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by liqingqing on 2017/1/6.
 */
@RestController
@RequestMapping("/cms/column")
public class ColumnsController {
    @Autowired
    private ColumnService columnService;

    /**
     * 栏目增加接口
     *
     * @param parm
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam Map<String, String> parm) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,columnService.create(parm));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/initialize",method = RequestMethod.POST)
    public String initialize() {//初始化栏目
        try {
            return ValueUtil.toJson(columnService.initialize());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /**
     * 栏目修改接口
     *
     * @param id
     * @param columnsName
     * @param pId
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public String update(Integer id, String columnsName, Integer pId) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,columnService.update(id, columnsName, pId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /**
     * 栏目删除接口
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delete(Integer id) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,columnService.delete(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }

    /**
     * 根据栏目id查询接口
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String show(Integer id) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,columnService.show(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }

    /**
     * 栏目查询接口
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,Integer id) {
        MapUtil.cleanNull(params);
        if(id!=null){
            return ValueUtil.toJson(HttpStatus.SC_OK,columnService.findOne(id));
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = columnService.findAll(pageModel);
        return ValueUtil.toJson(pageModel);
    }
}