package com.yesmywine.user.controller;

import com.yesmywine.user.service.StoreWineService;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ${shuang} on 2017/8/10.
 */
@RestController
@RequestMapping("/userservice/storeWine")
public class StoreWineController {
    @Autowired
    private StoreWineService storeWineService;

    @RequestMapping(value = "/itf",method = RequestMethod.POST)//存酒
    public String storage(Integer userId ,String json){
        try {
            return  storeWineService.storage(userId,json);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/load/itf",method = RequestMethod.POST)//内部显示提酒数据
    public String getLoadItf(String json,Integer userId){
        return storeWineService.show(userId,json);
    }


    @RequestMapping(value = "/returnload/itf",method = RequestMethod.POST)//内部显示退酒数据
    public String getReturnLoadItf(String json,Integer userId){
        return storeWineService.returnShow(userId,json);
    }


    @RequestMapping(value = "/extract/itf",method = RequestMethod.POST)//取酒
    public String extract(String json, String extractorderNumber,  Integer userId){

        try {
            return storeWineService.extract(json,extractorderNumber,userId) ;
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/return/itf",method = RequestMethod.POST)//退酒
    public String returns(String json, String returnOrderNumber,  Integer userId){
        try {
            return storeWineService.returns(json,returnOrderNumber,userId) ;
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/returncancel/itf",method = RequestMethod.POST)//退酒
    public String returncancel(String json, String returnOrderNumber,  Integer userId){
        try {
            return storeWineService.returnComfirmcancel(json,returnOrderNumber,userId) ;
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/cancel/itf",method = RequestMethod.POST)//取消
    public String cancel(String json, String extractorderNumber,  Integer userId){
        try {
            return storeWineService.cancel(json,extractorderNumber,userId) ;
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/returnConfirm/itf",method = RequestMethod.POST)//退货确定
    public String returnConfirm(String json, String extractorderNumber,  Integer userId){

        try {
            return storeWineService.returnComfirm(json,extractorderNumber,userId) ;
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/confirm/itf",method = RequestMethod.POST)//支付确定
    public String confirm(String json, String extractorderNumber,  Integer userId){

        try {
            return storeWineService.comfirm(json,extractorderNumber,userId) ;
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}
