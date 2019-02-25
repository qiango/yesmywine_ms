package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.AreaDao;
import com.yesmywine.cms.dao.UserAreaDao;
import com.yesmywine.cms.service.LocationService;
import com.yesmywine.cms.entity.Area;
import com.yesmywine.cms.entity.UserArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class LocationServiceImpl implements LocationService {

    @Autowired
    private AreaDao areaDao;
    @Autowired
    private UserAreaDao userAreaDao;


    public Object findAreaAll(){
        return this.areaDao.findAll();
    }

    public Object findUserAreaOne(Integer userId){
        JSONObject jsonObject = new JSONObject();
        UserArea byUserId = this.userAreaDao.findByUserId(userId);
        jsonObject.put("userId", byUserId.getUserId());
        jsonObject.put("cityId", byUserId.getAreaId());
        Area one = this.areaDao.findOne(byUserId.getAreaId());
        jsonObject.put("cityName", one.getCityName());
        return jsonObject;
    }

    @Override
    public String insert(Integer userId, Integer areaId) {
        try {
            UserArea userArea = new UserArea();
            userArea.setAreaId(areaId);
            userArea.setUserId(userId);
            this.userAreaDao.save(userArea);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

}
