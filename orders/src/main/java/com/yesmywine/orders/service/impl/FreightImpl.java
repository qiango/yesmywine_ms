package com.yesmywine.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.orders.dao.FreightAttachDao;
import com.yesmywine.orders.dao.FreightDao;
import com.yesmywine.orders.entity.Freight;
import com.yesmywine.orders.entity.FreightAttach;
import com.yesmywine.orders.service.FreightService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hz on 6/8/17.
 */
@Service
public class FreightImpl extends BaseServiceImpl<Freight,Integer> implements FreightService{
    @Autowired
    private FreightDao freightDao;
    @Autowired
    private FreightAttachDao freightAttachDao;
    @Override
    public String saveAdviceType(Map<String, String> params , String json) throws YesmywineException {
        // [{"id":1,"name":"北京市"},{"id":2,"name":"湖北省"}]
        Freight freight1=new Freight();
        String areaName=params.get("areaName");
        if(ValueUtil.notEmpity(areaName)) {
            freight1 = freightDao.findByAreaName(params.get("areaName"));
            if (freight1 == null) {
                Freight freight=new Freight();
                freight.setAreaName(params.get("areaName"));
                freight.setCourierfees(Double.valueOf(params.get("courierfees")));
                freight.setOrderFree(Double.valueOf(params.get("orderFree")));
                freight.setTransfers(params.get("transfers"));
                freight.setTrasfersOrder(Double.valueOf(params.get("trasfersOrder")));
                freight.setTransfersAmount(Double.valueOf(params.get("transfersAmount")));
                freight.setCashOnDelivery(params.get("cashOnDelivery"));
                freight1 = freightDao.save(freight);
            }
        }
        JSONArray jsonArray = JSON.parseArray(json);
        if(jsonArray.size()!=0){
            List<FreightAttach> list = new ArrayList<>();
            for (int i = 0; i <jsonArray.size() ; i++) {
                FreightAttach freightAttach = new FreightAttach();
                JSONObject jsonObject =(JSONObject)jsonArray.get(i);
                freightAttach.setFreightId(freight1.getId());
                String id = jsonObject.get("id").toString();
                String name = jsonObject.get("name").toString();
                freightAttach.setAreaId(id);
                freightAttach.setName(jsonObject.get("name").toString());
                if (ValueUtil.notEmpity(freightAttachDao.findByAreaId(id))
                        ||ValueUtil.notEmpity(freightAttachDao.findByName(name))){
                    ValueUtil.isError("插入数据重复"+jsonObject);
                }
                list.add(freightAttach);
            }
            freightAttachDao.save(list);
        }
        return "success";
    }

    @Override
    @Transactional
    public String deleteById(Integer id) throws YesmywineException {
        freightAttachDao.deleteByFreightId(id);
        freightDao.delete(id);
        return "success";
    }

    @Override
    public JSONObject calculate(String areaId, double orderAmount) throws YesmywineException {
        JSONObject freightJson = new JSONObject();
        Freight freight = null;
        try {
            FreightAttach freightAttach=freightAttachDao.findByAreaId(areaId);
            Integer freightId=freightAttach.getFreightId();
            freight = freightDao.findOne(freightId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(freight.getCashOnDelivery().equals("0")){//是否货到付款
            freightJson.put("cashOnDelivery","yes");
        }else {
            freightJson.put("cashOnDelivery","no");
        }
        if(freight.getTransfers().equals("0")){//是否需要调拨费用
            if(orderAmount>=freight.getTrasfersOrder()){
                freightJson.put("transfersAmount",freight.getTransfersAmount());//调拨费用
            }else{
                freightJson.put("transfersAmount",0.0);
            }
        }else {
            freightJson.put("transfersAmount",0.0);
        }
        if(orderAmount>=freight.getOrderFree()){//快递费
            freightJson.put("courierfees",0.0);
        }else {
            freightJson.put("courierfees",freight.getCourierfees());
        }
        freightJson.put("areaPostage",freight.getCourierfees());//该地区邮费
        return freightJson;
    }
}
