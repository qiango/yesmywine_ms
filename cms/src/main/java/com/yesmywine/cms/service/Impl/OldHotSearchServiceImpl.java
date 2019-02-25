package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.OldHotSearchFirstDao;
import com.yesmywine.cms.dao.OldHotSearchSecentDao;
import com.yesmywine.cms.entity.OldHotSearchFirst;
import com.yesmywine.cms.entity.OldHotSearchSecent;
import com.yesmywine.cms.service.OldHotSearchService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.List;


/**
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class OldHotSearchServiceImpl implements OldHotSearchService {

    @Autowired
    private OldHotSearchFirstDao oldHotSearchFirstDao;
    @Autowired
    private OldHotSearchSecentDao oldHotSearchSecentDao;


    @Override
    public Object findOne(Integer id) {
        OldHotSearchFirst one = this.oldHotSearchFirstDao.findOne(id);
        if(ValueUtil.isEmpity(one)){
            return "没有此栏目";
        }
        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", one.getId());
        jsonObject.put("name", one.getHotSearchFirstName());
        List<OldHotSearchSecent> byLikeFirstId = this.oldHotSearchSecentDao.findByHotSearchFirstId(one.getId());

        for(OldHotSearchSecent oldHotSearchSecent: byLikeFirstId){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", oldHotSearchSecent.getId());
            jsonObjectSecent.put("hotSearchSecentName", oldHotSearchSecent.getHotSearchSecentName());
            jsonArray.add(jsonObjectSecent);
        }
        jsonObject.put("oldHotSearchSecent", jsonArray);
        return jsonObject;
    }

    @Override
    public Object findAll() {
        List<OldHotSearchFirst> all = this.oldHotSearchFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(OldHotSearchFirst one: all) {
            JSONArray jsonArray= new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getHotSearchFirstName());
            List<OldHotSearchSecent> byLikeFirstId = this.oldHotSearchSecentDao.findByHotSearchFirstId(one.getId());

            for(OldHotSearchSecent oldHotSearchSecent: byLikeFirstId){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", oldHotSearchSecent.getId());
                jsonObjectSecent.put("hotSearchSecentName", oldHotSearchSecent.getHotSearchSecentName());
                jsonArray.add(jsonObjectSecent);
            }
            jsonObject.put("oldHotSearchSecent", jsonArray);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }

    @Override
    public String insert(String name, String jsonString) {
        try {
            OldHotSearchFirst byName = this.oldHotSearchFirstDao.findByHotSearchFirstName(name);
            if(ValueUtil.notEmpity(byName)){
                return "此栏目已存在";
            }
            List<OldHotSearchFirst> all = this.oldHotSearchFirstDao.findAll();
            if(all.size()>=4){
                return "栏目已满";
            }
            OldHotSearchFirst oldHotSearchFirst = new OldHotSearchFirst();
            oldHotSearchFirst.setHotSearchFirstName(name);
            this.oldHotSearchFirstDao.save(oldHotSearchFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String hotSearchSecentName = jsonObject.get("hotSearchSecentName").toString();
                    OldHotSearchSecent oldHotSearchSecent = new OldHotSearchSecent();
                    oldHotSearchSecent.setHotSearchSecentName(hotSearchSecentName);
                    oldHotSearchSecent.setHotSearchFirstId(oldHotSearchFirst.getId());
                    this.oldHotSearchSecentDao.save(oldHotSearchSecent);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String update(Integer id, String name, String jsonString) {
        try {
            OldHotSearchFirst byName = this.oldHotSearchFirstDao.findByHotSearchFirstName(name);
            if(ValueUtil.notEmpity(byName)&& byName.getId() != id){
                return "此栏目已存在";
            }
            OldHotSearchFirst oldHotSearchFirst = new OldHotSearchFirst();
            oldHotSearchFirst.setHotSearchFirstName(name);
            oldHotSearchFirst.setId(id);
            this.oldHotSearchFirstDao.save(oldHotSearchFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String hotSearchSecentName = jsonObject.get("hotSearchSecentName").toString();
                    OldHotSearchSecent oldHotSearchSecent = new OldHotSearchSecent();
                    oldHotSearchSecent.setHotSearchSecentName(hotSearchSecentName);
                    oldHotSearchSecent.setHotSearchFirstId(oldHotSearchFirst.getId());
                    OldHotSearchSecent byHotSearchFirstIdAndHotSearchSecentName = this.oldHotSearchSecentDao.findByHotSearchFirstIdAndHotSearchSecentName(oldHotSearchFirst.getId(), hotSearchSecentName);
                    if(ValueUtil.notEmpity(byHotSearchFirstIdAndHotSearchSecentName)){
                        oldHotSearchSecent.setId(byHotSearchFirstIdAndHotSearchSecentName.getId());
                    }
                    this.oldHotSearchSecentDao.save(oldHotSearchSecent);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String deleteFirst(Integer id) {
        try{
            this.oldHotSearchFirstDao.delete(id);
            this.oldHotSearchSecentDao.deleteByHotSearchFirstId(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecent(Integer id) {
        try{
            this.oldHotSearchSecentDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }
}
