package com.yesmywine.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.user.dao.CouponDao;
import com.yesmywine.user.dao.UserCouponDao;
import com.yesmywine.user.entity.Coupon;
import com.yesmywine.user.entity.UserCoupon;
import com.yesmywine.user.service.CouponService;
import com.yesmywine.user.service.UserCouponService;
import com.yesmywine.user.service.impl.UserCouponServiceImpl;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by ${shuang} on 2017/4/13.
 */
@RestController
@RequestMapping("/userservice/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponDao couponDao;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserCouponDao userCouponDao;
    @Autowired
    private UserCouponServiceImpl userCouponServiceimpl;

    @RequestMapping( method = RequestMethod.POST)
    public String create(@RequestParam Map<String, String> params){//创建
        try {
            ValueUtil.verify(params.get("couponName"));//名称
            ValueUtil.verify( params.get("amount"));//总数
            ValueUtil.verify(params.get("full"));//满多少元
            ValueUtil.verify(params.get("cut"));//减多少元
            ValueUtil.verify(params.get("drawStartTime"));//领取开始时间
            ValueUtil.verify(params.get("drawEndTime"));//领取结束时间
            ValueUtil.verify(params.get("activeStartTime"));//使用开始时间
            ValueUtil.verify(params.get("activeEndTime"));//使用结束时间
           String code = couponService.couponCreate(params);
           if(code.equals("500")){
               return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR,"时间冲突");
           }else {
               return code;
           }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping( method = RequestMethod.DELETE)
    public String deletes(Integer couponId){//删除
        try {
            ValueUtil.verify(couponId);
            return couponService.delete(couponId) ;
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/back", method = RequestMethod.GET)//单查多查(后台)
    public String backIndex(@RequestParam Map<String, Object> params, Integer couponId, Integer pageNo, Integer pageSize) {
           if(ValueUtil.notEmpity(couponId)){
            Coupon coupon = couponDao.findOne(couponId);
            Integer categoryId = coupon.getCategoryId();
            Integer brandId = coupon.getBrandId();
               String categoryName=null;
               String brandName=null;
               if(categoryId!=0){
                 categoryName = userCouponServiceimpl.http(categoryId,null);
            }else {
                 categoryName="全分类可用";
            }
               if(brandId!=0){
                    brandName = userCouponServiceimpl.http(null,brandId);
               }else {
                    brandName="全品牌可用";
               }
               JSONObject jsonObject = ValueUtil.toJsonObject(coupon);
               jsonObject.put("categoryName",categoryName);
               jsonObject.put("brandName",brandName);
             return ValueUtil.toJson(HttpStatus.SC_OK,jsonObject);
            }else {
               MapUtil.cleanNull(params);
               if(null!=params.get("all")&&params.get("all").toString().equals("true")){
                Integer auditStatus = Integer.valueOf((String)params.get("auditStatus"));
                   return ValueUtil.toJson(couponDao.findByAuditStatus(auditStatus));
               }else  if(null!=params.get("all")){
                   params.remove(params.remove("all").toString());
               }
               PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
               if (null != params.get("showFields")) {
                   pageModel.setFields(params.remove("showFields").toString());
               }
               if (pageNo != null) params.remove(params.remove("pageNo").toString());
               if (pageSize != null) params.remove(params.remove("pageSize").toString());

               params.put("auditStatus_ne",5);
               pageModel.addCondition(params);
               pageModel = couponService.findAll(pageModel);
               return ValueUtil.toJson(pageModel);
           }

    }

    @RequestMapping( method = RequestMethod.GET)//此优惠券下的人数
    public String showone(@RequestParam Map<String, String> params,Integer pageNo,Integer pageSize ){
            PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
            if (null != params.get("showFields")) {
                pageModel.setFields(params.remove("showFields").toString());
            }
            if (pageNo != null) params.remove(params.remove("pageNo").toString());
            if (pageSize != null) params.remove(params.remove("pageSize").toString());
            if(ValueUtil.isEmpity(params.get("couponId"))){
                params.remove(params.remove("couponId").toString());
            }
            pageModel.addCondition(params);
            pageModel = userCouponService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
    }

    @RequestMapping(value = "/front", method = RequestMethod.GET)//前台领取展示展示用
    public String frontIndex(@RequestParam Map<String, String> params,Integer pageNo,Integer pageSize ){
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        params.put("auditStatus","1");
        pageModel.addCondition(params);
        pageModel = couponService.findAll(pageModel);
        return ValueUtil.toJson(pageModel);
      /*  try {
            if(null!=params.get("all")&&params.get("all").toString().equals("true")){
                return ValueUtil.toJson(couponDao.findSome());
            }else  if(null!=params.get("all")){
                params.remove(params.remove("all").toString());
            }
            if (pageNo != null){
                params.remove(params.remove("pageNo").toString());
            }
            if (pageSize != null) {
                params.remove(params.remove("pageSize").toString());
            }

         Integer pn=  (pageNo == null ? 1 : pageNo) ;
         Integer ps = ( pageSize == null ? 10 : pageSize) ;
            return ValueUtil.toJson(couponService.frontIndex(pn,ps));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }*/
    }


    @RequestMapping(value = "/cancel", method = RequestMethod.PUT)//下架
    public String cancel(Integer couponId){
        try {
            return couponService.cancel(couponId);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/audit", method = RequestMethod.PUT)//审核
    public String audit(Integer couponId,Integer auditStatus, String remarks){
        try {
            return couponService.audit(couponId,auditStatus,remarks);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/filter/itf", method = RequestMethod.GET)//过滤
    public String filter(String couponIds,String username,String status){
        try {
            return couponService.filter(couponIds,username,status);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/check/task", method = RequestMethod.GET)//定时取消
    public void task(){
        try {
            couponService.task();
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
          ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/code",method = RequestMethod.GET)//二维码下发
    public String  pic(Integer couponId, HttpServletResponse response){
        Coupon coupon =couponDao.findOne(couponId);
        if(ValueUtil.isEmpity(coupon)){
            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"id为空");
        }
        //todo 字典表查url拼装
        String text =coupon.getId().toString();
        int width = 100;
        int height = 100;
        String format = "png";
        Hashtable hints= new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height,hints);
        } catch (WriterException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        File outputFile = new File("new.png");
        try {
            MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);
            response.setContentType("text/html; charset=UTF-8");
            response.setContentType("image/jpeg");
            FileInputStream fis = new FileInputStream(outputFile);
            OutputStream os = response.getOutputStream();
            try
            {
                int count = 0;
                byte[] buffer = new byte[1024 * 1024];
                while ((count = fis.read(buffer)) != -1)
                    os.write(buffer, 0, count);
                os.flush();
            }
            catch (IOException e)
            {
                Threads.createExceptionFile("userservice",e.getMessage());
                e.printStackTrace();
            }
            finally
            {
                if (os != null)
                    os.close();
                if (fis != null)
                    fis.close();
            }
        } catch (IOException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/use/itf", method = RequestMethod.POST)//优惠券使用
    public String user(Integer userId, Integer userCouponId ){
        try {
            return   userCouponService.use(userId,userCouponId);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/one/itf", method = RequestMethod.GET)//查询一个
    public String getone(Integer userCouponId){
        UserCoupon userCoupon = userCouponDao.findOne(userCouponId);
        Integer couponId = userCoupon.getCouponId();
        Coupon coupon = couponDao.findOne(couponId);
        return ValueUtil.toJson(HttpStatus.SC_OK,coupon);
    }

    @RequestMapping(value = "/return/itf", method = RequestMethod.POST)//优惠券退回
    public String couponReturn(Integer userId ,Integer userCouponId){
        try {
            return  ValueUtil.toJson(HttpStatus.SC_CREATED,userCouponService.returns(userId,userCouponId)) ;
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

}
