package com.yesmywine.user.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.service.ReceivingAddressService;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/5/3.
 */
@RestController
public class ReceivingAddressController  {

    @Autowired
    private ReceivingAddressService receivingAddressService;

    @RequestMapping(value = "member/userservice/receivingAddress",method = RequestMethod.POST)
    public String create(@RequestParam Map<String, String> param, HttpServletRequest request) {//新增收货地址
        try {
            Integer userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                return "未登录";
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED,   receivingAddressService.create(param,userId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

    }

    @RequestMapping(value = "member/userservice/receivingAddress",method = RequestMethod.DELETE)
    public String delete(Integer id, HttpServletRequest request) {//删除收货地址
        try {
            Integer userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                return "未登录";
            }
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, receivingAddressService.delete(id));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "member/userservice/receivingAddress",method = RequestMethod.PUT)
    public String update(@RequestParam Map<String, String> param, HttpServletRequest request) {//修改保存收货地址
        try {
            Integer userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                return "未登录";
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, receivingAddressService.updateSave(param));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "member/userservice/receivingAddress/acquiesce",method = RequestMethod.PUT)
    public String acquiesce(Integer id, HttpServletRequest request) {//默认
        try {
            Integer userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                return "未登录";
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, receivingAddressService.acquiesce(id,userId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "member/userservice/receivingAddress/showOneDefault",method = RequestMethod.GET)
    public String showOneDefault(HttpServletRequest request) {//显示默认地址
        try {
            Integer userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                return "未登录";
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, receivingAddressService.showOneDefault(userId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "member/userservice/receivingAddress",method = RequestMethod.GET)
    public String index(HttpServletRequest request,Integer id) throws  Exception{
        Integer userId = UserUtils.getUserId(request);
        if(ValueUtil.isEmpity(userId)){
            return "未登录";
        }
        if(ValueUtil.isEmpity(id)){
            return ValueUtil.toJson(HttpStatus.SC_CREATED, receivingAddressService.findReceivingAddressAll(userId));
        }else {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, receivingAddressService.findOne(id));
        }
    }
    @RequestMapping(value = "/userservice/receivingAddress/itf",method = RequestMethod.GET)
    public String insideindex(@RequestParam Map<String, Object> params,Integer pageNo,Integer pageSize,Integer id) throws  Exception{
        //自提点调用
        if(id!=null){
            return ValueUtil.toJson(HttpStatus.SC_OK, receivingAddressService.showOne(id));
        }
        if(null!=params.get("all")&&params.get("all").toString().equals("true")){
            return ValueUtil.toJson(receivingAddressService.findAll());
        }else if(null!=params.get("all")){
            params.remove(params.remove("all").toString());
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = receivingAddressService.findAll(pageModel);
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
    }
}

