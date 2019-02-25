package com.yesmywine.goods.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.dao.GoodsDao;
import com.yesmywine.goods.dao.GoodsLabelDao;
import com.yesmywine.goods.dao.LabelDao;
import com.yesmywine.goods.entity.Goods;
import com.yesmywine.goods.entity.GoodsLabel;
import com.yesmywine.goods.entity.Label;
import com.yesmywine.goods.service.LabelService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hz on 2017/4/24.
 */
@Service
public class LabelServiceImpl extends BaseServiceImpl<Label, Integer> implements LabelService {

    @Autowired
    private LabelDao tagDao;
    @Autowired
    private GoodsLabelDao goodsLabelDao;
    @Autowired
    private GoodsDao goodsDao;

//    @Override
//    public Map<String, String> query(String name) {
//        Map<String, String> map = new HashMap<>();
//        try {
//            List<Label> byName = tagDao.findByName(name);
//            if (ValueUtil.notEmpity(byName) && byName.size() > 0) {
//                map.put("name", byName.get(0).getName());
//                Integer pId = byName.get(0).getpId();
//                if (ValueUtil.notEmpity(pId)) {
//                    Label one = tagDao.findOne(pId);
//                    map.put("pName", one.getName());
//                } else {
//                    map.put("pName", "");
//                }
//                map.put("sum", String.valueOf(byName.size()));
//            }
//        }catch (ExceptionThread e){
//            map.put("erro", e.toString());
//            return map;
//        }
//        return map;
//    }

    @Override
    public String create(Map<String, String> map) throws YesmywineException {
        String name=map.get("name");
        if(null!=tagDao.findByName(name)){
            ValueUtil.isError("该标签名已存在");
        }
        Label label=new Label();
        label.setName(name);
        if(ValueUtil.notEmpity(map.get("pId"))){
            label.setParentName(tagDao.findOne(Integer.parseInt(map.get("pId"))));
        }else {
            label.setParentName(null);
        }
        tagDao.save(label);
        return "success";
    }

    @Override
    public String put(Map<String, Object> map) {
        try{
            Label tag = new Label();
            tag.setId(Integer.valueOf(map.get("id").toString()));
            tag.setName(map.get("name").toString());
            if(ValueUtil.notEmpity(map.get("pId"))){
                tag.setParentName(tagDao.findOne(Integer.parseInt(map.get("pId").toString())));
            }
            tagDao.save(tag);
        }catch (NumberFormatException e){
            return "参数类型不正确";
        }catch (Exception e){
            return "数据插入异常";
        }
        return "success";
    }

    @Override
    public String deleteLabel(Integer id) throws YesmywineException {
        Label label=tagDao.findOne(id);
        if(null!=tagDao.findByParentName(label)){
            ValueUtil.isError("该标签有子标签,不可删除");
        }else if(goodsLabelDao.findByLabelId(id).size()!=0){
            ValueUtil.isError("该标签下有商品,不可删除");
        }else
            tagDao.delete(id);
        return "success";
    }

    public String deleteGoods(Integer id) throws YesmywineException {
        goodsLabelDao.delete(id);
        return "success";
    }

//    @Override
//    public List<Object> findIndex() {
//        List<Object> labels=tagDao.findByIndex();
//        return labels;
////    }
//
//    public void delete1(Integer id)  {
//        Label label = tagDao.findOne(id);
//        if (ValueUtil.notEmpity(label)) {
//            tagDao.delete(id);
//        }
//        List<Label> Labels = tagDao.findByPId(id);
//        for (Label label1 : Labels) {
//            tagDao.delete(label1.getId());
//            id = label1.getId();
//            if (ValueUtil.isEmpity(tagDao.findByPId(id))) {
//                break;
//            } else {
//                delete1(id);
//            }
//        }
//    }

//    @Override
//    public Object findByName(String name) {
//        List<Label> list = tagDao.findByName(name);
//        if (list.size()==0){
//            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"此名称不存在");
//        }else {
//            JSONArray jsonArray = new JSONArray();
//
//            List<Integer> goodsIds = new ArrayList<>();
//            for (int i = 0; i <list.size() ; i++) {
//                goodsIds.add(list.get(i).getGoodsId()) ;
//            }
//            for (int i = 0; i <goodsIds.size() ; i++) {
//                Goods goods = goodsDao.findOne(goodsIds.get(i));
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("id",goods.getId().toString());
//                jsonObject.put("name", goods.getGoodsName());
//                jsonArray.add(jsonObject);
//            }
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("name",list.get(0).getName());
//            jsonObject.put("pid",list.get(0).getpId().toString());
//            String pName = tagDao.findOne(list.get(0).getpId()).getName();
//            jsonObject.put("pName",pName);
//            jsonObject.put("goods",jsonArray);
//            return jsonObject;
//        }
//    }

//    @Override
//    public Object findlist() {
//        JSONArray jsonArray1 = new JSONArray();
//        List<Label> list = tagDao.findAll();
//        for (int i = 0; i <list.size() ; i++) {
//            Label label = list.get(i);
//            JSONObject jsonObject2 = new JSONObject();
//            Map<String,String> map = new HashMap<>();
//            jsonObject2.put("id",label.getId());
//            jsonObject2.put("name",label.getName());
//            jsonObject2.put("pid",label.getpId());
//            Label label1 = tagDao.findOne(list.get(0).getpId());
//            String name = null;
//             if (ValueUtil.notEmpity(label1)){
//                 name = label1.getName();
//             }
//            jsonObject2.put("pName",name);
//            Goods goods = goodsDao.findOne(label.getGoodsId());
//            map.put("id",label.getGoodsId().toString());
//            map.put("name",goods.getGoodsName());
//            jsonObject2.put("good",map);
//            jsonArray1.add(jsonObject2);
//        }
//        return jsonArray1;
//    }
}
