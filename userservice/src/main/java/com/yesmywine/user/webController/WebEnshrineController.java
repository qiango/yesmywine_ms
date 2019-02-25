package com.yesmywine.user.webController;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.user.dao.EnshrineDao;
import com.yesmywine.user.entity.Enshrine;
import com.yesmywine.user.service.EnshrineService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by ${shuang} on 2017/6/13.
 */
@RestController
@RequestMapping("/member/userservice/enshrine")
public class WebEnshrineController {

    @Autowired
    private EnshrineService enshrineService;
    @Autowired
    private EnshrineDao enshrineDao;

    @RequestMapping( method = RequestMethod.POST)
    public String create(String goodsIds,Integer status,HttpServletRequest request){
        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
               ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        try {
            return  ValueUtil.toJson(HttpStatus.SC_CREATED,enshrineService.enshrine(userId, goodsIds,status)) ;
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }



    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public String check(Integer goodsId,HttpServletRequest request){
        Integer userId = null;
        try {
            ValueUtil.verify(goodsId);
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        Enshrine enshrine = enshrineDao.findByUserIdAndGoodsId(userId,goodsId);
        if(ValueUtil.isEmpity(enshrine)){//可以收藏
           return ValueUtil.toJson(HttpStatus.SC_OK,true);//可存1
        }else {//不可收藏
            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,false);//不可存 0
        }
    }

    @RequestMapping( method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,HttpServletRequest request) {

        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                ValueUtil.isError("未登录") ;
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        MapUtil.cleanNull(params);
             params.put("userId",userId);
            if(null!=params.get("all")&&params.get("all").toString().equals("true")){
                return ValueUtil.toJson(enshrineService.findAll());
            }else  if(null!=params.get("all")){
                params.remove(params.remove("all").toString());
            }
            PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
            if (null != params.get("showFields")) {
                pageModel.setFields(params.remove("showFields").toString());
            }
            if (pageNo != null) params.remove(params.remove("pageNo").toString());
            if (pageSize != null) params.remove(params.remove("pageSize").toString());
            for (String key :params.keySet()) {
                if(ValueUtil.isEmpity(params.get(key))){
                    params.remove(key);
                }
            }
            pageModel.addCondition(params);
            pageModel = enshrineService.findAll(pageModel);
        try {
            return  enshrineService.page(pageModel);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

    }

}
