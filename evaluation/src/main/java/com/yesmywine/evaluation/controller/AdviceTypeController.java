package com.yesmywine.evaluation.controller;

import com.yesmywine.evaluation.bean.AdviceType;
import com.yesmywine.evaluation.service.AdviceService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 咨询类型接口
 * Created by light on 2017/1/9.
 */
@RestController
@RequestMapping("evaluation/adviceType")
public class AdviceTypeController {

    @Autowired
    private AdviceService adviceService;

    //根据id查询
    @RequestMapping(method = RequestMethod.GET)
    public String show(Integer id) {
        if(ValueUtil.isEmpity(id)){
            return ValueUtil.toJson(HttpStatus.SC_OK,this.adviceService.findAll());
        }else
            return ValueUtil.toJson(HttpStatus.SC_OK,this.adviceService.findOne(id));
    }

    //插入
    @RequestMapping(method = RequestMethod.POST)
    public String create(AdviceType adviceType) {
        try {
            ValueUtil.verify(adviceType.getName(), "name");
            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.adviceService.saveAdviceType(adviceType));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    //修改
    @RequestMapping(method = RequestMethod.PUT)
    public String update(AdviceType adviceType) {
        try {
            ValueUtil.verify(adviceType.getId(), "id");
            ValueUtil.verify(adviceType.getName(), "name");
            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.adviceService.saveAdviceType(adviceType));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    //删除
    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(Integer id) {
        try {
            ValueUtil.verify(id, "id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,this.adviceService.deleteById(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

}
