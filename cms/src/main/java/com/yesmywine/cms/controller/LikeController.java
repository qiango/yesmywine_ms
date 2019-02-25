
package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.LikeService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页猜你喜欢
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/cms/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer likeFirstId) {   //查看
        if(ValueUtil.notEmpity(likeFirstId)){
            return ValueUtil.toJson(likeService.findOne(likeFirstId));
        }
        return ValueUtil.toJson(likeService.findAll());
    }

//    @RequestMapping(value = "/front", method = RequestMethod.GET)
//    public String frontIndex() {   //查看
//        return ValueUtil.toJson(likeService.FrontfindAll());
//    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(String name, String jsonString) {   //新增
        try {
            ValueUtil.verify(name,"name");
            ValueUtil.verify(jsonString,"jsonString");
            String insert = this.likeService.insert(name, jsonString);
            if("success".equals(insert)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
            }
            return ValueUtil.toError("500", insert);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, String name, String jsonString) {   //修改
        try {
            ValueUtil.verify(id,"id");
            ValueUtil.verify(name,"name");
            String update = this.likeService.update(id, name, jsonString);
            if("success".equals(update)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, update);
            }
            return ValueUtil.toError("500", update);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


//    @RequestMapping(value = "/first", method = RequestMethod.DELETE)
//    public String deleteFirst(Integer id) {
//        try {
//            ValueUtil.verify(id,"id");
//            String deleteFirst = this.likeService.deleteFirst(id);
//            if("success".equals(deleteFirst)){
//                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, deleteFirst);
//            }
//            return ValueUtil.toError("500", deleteFirst);
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(),e.getMessage());
//        }
//    }

//
    @RequestMapping(value = "/secent", method = RequestMethod.DELETE)
    public String deleteSecent(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            String deleteSecent = this.likeService.deleteSecent(id);
            if("success".equals(deleteSecent)){
                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, deleteSecent);
            }
            return ValueUtil.toError("500", deleteSecent);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}