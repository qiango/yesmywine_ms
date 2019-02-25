package com.yesmywine.evaluation.controller;


import com.yesmywine.evaluation.bean.Lable;
import com.yesmywine.evaluation.dao.GoodsLableDao;
import com.yesmywine.evaluation.service.LableService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hz on 6/21/17.
 */
@RestController
@RequestMapping("/lable")
public class EvaluationLable {


    @Autowired
    private LableService lableService;
    @Autowired
    private GoodsLableDao goodsLableDao;

    @RequestMapping(method = RequestMethod.POST)
    public String create(Lable lable){
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,lableService.save(lable));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(Lable lable){
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,lableService.save(lable));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(Integer id){
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,lableService.delete(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/rank",method = RequestMethod.GET)
    public String rank(Integer goodsId){
        return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,goodsLableDao.findByGoodsIdOrderByCountDesc(goodsId));
    }


}
