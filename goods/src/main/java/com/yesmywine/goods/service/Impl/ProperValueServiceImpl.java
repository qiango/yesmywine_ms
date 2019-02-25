package com.yesmywine.goods.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.db.base.ehcache.CacheStatement;
import com.yesmywine.goods.dao.ProperValueDao;
import com.yesmywine.goods.dao.PropertiesDao;
import com.yesmywine.goods.entityProperties.PropertiesValue;
import com.yesmywine.goods.service.ProperValueService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangdiandian on 2017/4/27.
 */
@Service
public class ProperValueServiceImpl  extends BaseServiceImpl<PropertiesValue, Integer> implements ProperValueService {
    @Autowired
    private ProperValueDao properValueDao;
    @Autowired
    private PropertiesDao propertiesDao;


    public String addPrpoValue(String jsonData) throws YesmywineException {


        String jsonDataS = ValueUtil.getFromJson(jsonData, "data");

        JSONArray adjustArray = JSON.parseArray(jsonDataS);
        List<PropertiesValue> list = new ArrayList<>();
        for (int i = 0; i < adjustArray.size(); i++) {
            JSONObject adjustCommand = (JSONObject) adjustArray.get(i);
            PropertiesValue propertiesValue = new PropertiesValue();
//            String cnName = adjustCommand.getString("cnName"); //属性名
            String cnValue = adjustCommand.getString("cnValue");//属性值
            Integer propertiesId = adjustCommand.getInteger("propertiesId");
            String code = adjustCommand.getString("code");
            Integer id = adjustCommand.getInteger("id");
//            propertiesValue.setCnName(cnName);
            propertiesValue.setCnValue(cnValue);
            propertiesValue.setPropertiesId(propertiesId);
            propertiesValue.setCode(code);
            propertiesValue.setId(id);
            propertiesValue.setCreateTime(new Date());
            list.add(propertiesValue);
        }
        properValueDao.save(list);
        return "success";
    }

    @CacheEvict(value= CacheStatement.PROPERTIES_VALUE)
    public String deletePrpoValue(String id) throws YesmywineException {
        String[] strs = id.split(";");
            List<PropertiesValue> propertiesValues = new ArrayList<>();
            for (int i = 0; i < strs.length; i++) {
                Integer id1 = Integer.valueOf(strs[i]);
                    PropertiesValue propertiesValue = properValueDao.findOne(id1);
                    propertiesValues.add(propertiesValue);
                }
            properValueDao.delete(propertiesValues);
//        Integer ids=Integer.valueOf(id);
//            PropertiesValue propertiesValue = properValueDao.findOne(ids);
////            propertiesValue.setIsDelete(1);
//            properValueDao.save(propertiesValue);
        return "success";
    }

    @Override
    @Cacheable(value= CacheStatement.PROPERTIES_VALUE,key = "'ProperValue_'+#id")
    public PropertiesValue findById(Integer id) {
        return this.properValueDao.findOne(id);
    }
}