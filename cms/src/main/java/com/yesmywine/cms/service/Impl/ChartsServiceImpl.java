package com.yesmywine.cms.service.Impl;

import com.yesmywine.cms.dao.GoodsDao;
import com.yesmywine.cms.entity.Goods;
import com.yesmywine.cms.service.ChartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class ChartsServiceImpl implements ChartsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public Object findAll() {

        int page = 0;
        int size = 6;
//        Pageable pageable = (Pageable) new PageRequest(page, pageSize);
        Sort sort = new Sort(Sort.Direction.DESC, "Sales");
        Pageable pageable = new PageRequest(page, size, sort);
//        return goodsDao.findAllOrderBySales(pageable);
        Page<Goods> all = goodsDao.findAll(pageable);
        return all;

//        Pageable pageable =
//        goodsDao.findAllOrderBySales()
//        List<ActivityFirst> all = this.ActivityFirstDao.findAll();
//        JSONArray jsonArrayRe = new JSONArray();
//        for(ActivityFirst one: all) {
//            JSONArray jsonArray= new JSONArray();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", one.getId());
//            jsonObject.put("name", one.getName());
//            jsonObject.put("activityId", one.getActivityId());
//            PositionEntity one1 = this.positionDao.findOne(one.getPositionIdBack());
//            if(ValueUtil.notEmpity(one1)){
//                jsonObject.put("positionBack", one1.getPositionName());
//                jsonObject.put("positionIdBack", one.getPositionIdBack());
//            }
//            PositionEntity one2 = this.positionDao.findOne(one.getPositionIdBanner());
//            if(ValueUtil.notEmpity(one1)){
//                jsonObject.put("positionBanner", one2.getPositionName());
//                jsonObject.put("positionIdBanner", one.getPositionIdBanner());
//            }
//            jsonObject.put("templateId", one.getTemplateId());
//            List<ActivitySecent> byActivityFirstId = this.activitySecentDao.findByActivityFirstId(one.getId());
//
//            for(ActivitySecent activitySecent: byActivityFirstId){
//                JSONObject jsonObjectSecent = new JSONObject();
//                jsonObjectSecent.put("id", activitySecent.getId());
//                Goods one3 = this.goodsDao.findOne(activitySecent.getGoodsId());
//                jsonObjectSecent.put("secentGoodsId", activitySecent.getGoodsId());
//                jsonObjectSecent.put("secentGoodsName", one3.getGoodsName());
//                jsonArray.add(jsonObjectSecent);
//            }
//            jsonObject.put("activitySecent", jsonArray);
//            jsonArrayRe.add(jsonObject);
//        }
//        return null;
    }




}
