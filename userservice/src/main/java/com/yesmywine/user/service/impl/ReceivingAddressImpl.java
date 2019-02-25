package com.yesmywine.user.service.impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.user.bean.ConstantData;
import com.yesmywine.user.dao.ReceivingAddressDao;
import com.yesmywine.user.entity.ReceivingAddress;
import com.yesmywine.user.service.CommonService;
import com.yesmywine.user.service.ReceivingAddressService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/5/3.
 */
@Service
@Transactional
public class ReceivingAddressImpl  extends BaseServiceImpl<ReceivingAddress, Integer> implements ReceivingAddressService {
    @Autowired
    private ReceivingAddressDao receivingAddressDao;
    @Autowired
    private CommonService<ReceivingAddress> commonService;

    public String create(Map<String, String> param,Integer userId) throws YesmywineException {


        Integer coumtUserId=receivingAddressDao.countUserId(userId);
//        判断收货地址是否达到上限
        if(coumtUserId>=20){
            ValueUtil.isError("收货地址已达上限！");
        }
//解析收货地址
        ValueUtil.verify(param, new String[]{"receiver", "provinceId",
                "province", "detailedAddress"});
        ReceivingAddress receivingAddress=new ReceivingAddress();
        String receiver=param.get("receiver");//收货人
        String provinceId=param.get("provinceId");//省
        String province=param.get("province");//省
        String cityId=param.get("cityId");//市
        String city=param.get("city");//市
        String areaId=param.get("areaId");//区
        String area=param.get("area");//区
        String detailedAddress=param.get("detailedAddress");//详细地址
        String phoneNumber=param.get("phoneNumber");//手机号码
        String fixedTelephone=param.get("fixedTelephone");//固定电话（选填）
        String mailbox=param.get("mailbox"); //邮箱（选填）
        String addressAlias=param.get("addressAlias"); //地址别名（选填）

        receivingAddress.setReceiver(receiver);
        receivingAddress.setProvinceId(Integer.valueOf(provinceId));
        receivingAddress.setProvince(province);
        if(ValueUtil.notEmpity(cityId)){
            receivingAddress.setCityId(Integer.valueOf(cityId));
            receivingAddress.setCity(city);
        }
        if(ValueUtil.notEmpity(areaId)){
            receivingAddress.setAreaId(Integer.valueOf(areaId));
            receivingAddress.setArea(area);
        }

        receivingAddress.setDetailedAddress(detailedAddress);
        receivingAddress.setPhoneNumber(phoneNumber);
        receivingAddress.setFixedTelephone(fixedTelephone);
        receivingAddress.setMailbox(mailbox);
        if(ValueUtil.isEmpity(addressAlias)){
            receivingAddress.setAddressAlias(receiver);
        }else {
            receivingAddress.setAddressAlias(addressAlias);
        }
        receivingAddress.setCreateTime(new Date());
        receivingAddress.setUserId(userId);
        List<ReceivingAddress> addressList= receivingAddressDao.findByUserId(userId);
        if(addressList==null){
            receivingAddress.setStatus(1);
        }else {
            receivingAddress.setStatus(0);
        }

        receivingAddressDao.save(receivingAddress);

        Map<String, String> map = new HashMap<>();
        map.put("id", receivingAddress.getId().toString());
//        同步收货地址
        if(!this.commonService.synchronous(receivingAddress, Dictionary.PAAS_HOST+ "/user/deliveryAddress/synchronous", 0,map)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError( "同步失败");
        }
        return "success";
    }

    public String delete(Integer id) throws YesmywineException {
        ValueUtil.verify(id, "idNull");
        ReceivingAddress receivingAddress=receivingAddressDao.findOne(id);
        receivingAddressDao.delete(receivingAddress);
//        删除同步
        if(!this.commonService.synchronous(receivingAddress, Dictionary.PAAS_HOST+ "/user/deliveryAddress/synchronous", 2)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError( "同步失败");
        }
        return "success";
    }

    public String updateSave(Map<String, String> param) throws YesmywineException {
        ValueUtil.verify(param, new String[]{"id","receiver", "provinceId",
                "province", "detailedAddress", "phoneNumber"});
        String id=param.get("id");
        ReceivingAddress receivingAddress=receivingAddressDao.findOne(Integer.valueOf(id));
        String receiver=param.get("receiver");//收货人
        String provinceId=param.get("provinceId");//省
        String province=param.get("province");//省
        String cityId=param.get("cityId");//市
        String city=param.get("city");//市
        String areaId=param.get("areaId");//区
        String area=param.get("area");//区
        String detailedAddress=param.get("detailedAddress");//详细地址
        String phoneNumber=param.get("phoneNumber");//手机号码
        String fixedTelephone=param.get("fixedTelephone");//固定电话（选填）
        String mailbox=param.get("mailbox"); //邮箱（选填）
        String addressAlias=param.get("addressAlias"); //地址别名（选填）
//        String status=param.get("status"); //状态0默认，1不默认

        receivingAddress.setReceiver(receiver);
        receivingAddress.setProvinceId(Integer.valueOf(provinceId));
        receivingAddress.setProvince(province);
        if(ValueUtil.notEmpity(cityId)){
            receivingAddress.setCityId(Integer.valueOf(cityId));
            receivingAddress.setCity(city);
        }
        if(ValueUtil.notEmpity(areaId)){
            receivingAddress.setAreaId(Integer.valueOf(areaId));
            receivingAddress.setArea(area);
        }
        receivingAddress.setDetailedAddress(detailedAddress);
        receivingAddress.setPhoneNumber(phoneNumber);
        receivingAddress.setFixedTelephone(fixedTelephone);
        receivingAddress.setMailbox(mailbox);
        receivingAddress.setAddressAlias(addressAlias);
//        receivingAddress.setStatus(Integer.valueOf(status));

        receivingAddressDao.save(receivingAddress);
        if(!this.commonService.synchronous(receivingAddress, Dictionary.PAAS_HOST+ "/user/deliveryAddress/synchronous", 1)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError( "同步失败");
        }
        return "success";
    }

    public  ReceivingAddress showOne(Integer id) throws YesmywineException {
        ValueUtil.verify(id, "idNull");
        ReceivingAddress receivingAddress=receivingAddressDao.findOne(Integer.valueOf(id));
        return  receivingAddress;
    }
    public  ReceivingAddress showOneDefault(Integer userId) throws YesmywineException {
        ValueUtil.verify(userId, "userIdNull");
        ReceivingAddress receivingAddress=receivingAddressDao.findByUserIdAndStatus(userId,1);
        if(receivingAddress==null){
            ValueUtil.isError("暂无默认收货地址！");
        }
        return  receivingAddress;
    }
    public String acquiesce(Integer id,Integer userId) throws YesmywineException{
//        这应该是设置默认地址
        ValueUtil.verify(id, "idNull");
        ReceivingAddress receivingAddress=receivingAddressDao.findOne(id);
        receivingAddress.setStatus(1);
        receivingAddressDao.save(receivingAddress);
        List<ReceivingAddress> list=receivingAddressDao.findByUserIdAndIdNot(userId,id);
        list.forEach(r -> {
            r.setStatus(0);//不默认
        });
        receivingAddressDao.save(list);
        for(int i=0;i<list.size();i++) {
            if(!this.commonService.synchronous(list.get(i), Dictionary.PAAS_HOST+ "/user/deliveryAddress/synchronous", 1)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError( "同步失败");
            }
        }
        if(!this.commonService.synchronous(receivingAddress, Dictionary.PAAS_HOST+ "/user/deliveryAddress/synchronous", 1)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError( "同步失败");
        }
        return "success";
    }


    public  List<ReceivingAddress> findReceivingAddressAll(Integer userId)throws YesmywineException{
//查找所有的收货地址
        List<ReceivingAddress> receivingAddressList=receivingAddressDao.findByUserId(userId);
        return receivingAddressList;

    }

}
