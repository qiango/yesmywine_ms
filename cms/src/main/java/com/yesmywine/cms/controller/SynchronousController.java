
package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.CategoryService;
import com.yesmywine.cms.service.GoodsService;
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
 * Created by hz on 3/15/17.
 */
@RestController
@RequestMapping("/cms/synchronous")
public class SynchronousController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private GoodsService goodsService;


    @RequestMapping(value = "/category", method = RequestMethod.POST)
    public String category(@RequestParam Map<String, String> params) throws  Exception{
        try {
            ValueUtil.verify(params.get("id"),"id");
            ValueUtil.verify(params.get("name"),"name");
            ValueUtil.verify(params.get("synchronous"),"synchronous");
            Integer parentId = null;
            if(ValueUtil.notEmpity(params.get("parentId"))){
                parentId = Integer.valueOf(params.get("parentId").toString());
            }
            Boolean result = this.categoryService.synchronous(Integer.valueOf(params.get("id").toString()), params.get("name").toString()
                    , parentId , Integer.valueOf(params.get("synchronous").toString()));
            if(result){
                return ValueUtil.toJson(HttpStatus.SC_OK, "success");
            }else {
                return ValueUtil.toError("500", "erro");
            }
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

//    @RequestMapping(value = "/goods", method = RequestMethod.POST)
//    public String goods(String jsonDate) throws  ExceptionThread{
//        try {
//            Boolean result = this.goodsService.synchronous(jsonDate);
//            if(result){
//                return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
//            }else {
//                return ValueUtil.toError("500", "erro");
//            }
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(),e.getMessage());
//        }
//    }


    @RequestMapping(value = "/goods", method = RequestMethod.POST)
    public String synchronous(String jsonData) {   //同步商品
        try {
            Boolean synchronous = goodsService.synchronous(jsonData);
            if(synchronous){
                return ValueUtil.toJson(HttpStatus.SC_OK,"success");
            }
            return ValueUtil.toError("500", "erro");
        } catch (YesmywineException e) {
            return ValueUtil.toError(HttpStatus.SC_NO_CONTENT,"Erro");
        }
    }
}
