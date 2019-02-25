package com.yesmywine.cms.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.cms.dao.ArticleDao;
import com.yesmywine.cms.dao.ColumnDao;
import com.yesmywine.cms.entity.ArticleEntity;
import com.yesmywine.cms.entity.ColumnsEntity;
import com.yesmywine.cms.service.ColumnService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liqingqing on 2017/2/10.
 */
@Service
public class ColumnServiceImpl extends BaseServiceImpl<ColumnsEntity,Integer> implements ColumnService {
    @Autowired
    private ColumnDao columnDao;
    @Autowired
    private ArticleDao articleDao;


    public String create(Map<String, String> parm) throws YesmywineException {

        ValueUtil.verify(parm, new String[]{"pId", "columnsName"});

        String columnsName = parm.get("columnsName");
        Integer pId = Integer.parseInt(parm.get("pId"));
        ColumnsEntity byColumnsName = columnDao.findByColumnsNameAndPId(columnsName,pId);
        if(null!=byColumnsName&&byColumnsName.getpId()==pId){
            ValueUtil.isError("该栏目已存在");
        }else if(pId==0){
            ValueUtil.isError("不可新建一级栏目");
        }
        ColumnsEntity columnsEntity = new ColumnsEntity();
        columnsEntity.setColumnsName(columnsName);
        ColumnsEntity columnsEntity1=columnDao.findOne(pId);
        columnsEntity.setpId(pId);
        columnsEntity.setParentName(columnsEntity1.getColumnsName());
        columnDao.save(columnsEntity);
        return "sucess";
    }

    @Override
    public String initialize() throws YesmywineException {
        if(0!=columnDao.findAll().size()){
            ValueUtil.isError("初始化已完成,不可重复");
        }
        String columnsName="帮助中心,促销,公告";
        String arr[]=columnsName.split(",");
        List<ColumnsEntity> list=new ArrayList<>();
        for(int i=0;i<arr.length;i++){
            ColumnsEntity columnsEntity=new ColumnsEntity();
            columnsEntity.setColumnsName(arr[i]);
            columnsEntity.setpId(0);
            columnsEntity.setParentName(null);
            if(arr[i].equals("帮助中心")){
                columnsEntity.setCode("helpcenter");
            }else if(arr[i].equals("促销")){
                columnsEntity.setCode("promotion");
            }else {
                columnsEntity.setCode("announcement");
            }
            list.add(columnsEntity);
        }
        columnDao.save(list);
        return "initialize success";
    }

    public String update(Integer id, String columnsName, Integer pId) throws YesmywineException {

        ValueUtil.verify(id, "idNull");
        ValueUtil.verify(columnsName, "columnsNameNull");
        ValueUtil.verify(pId, "pIdNull");
        if(id==1||id==2||id==3){
            ValueUtil.isError("父栏目不可编辑");
        }
        ColumnsEntity columnsEntity = columnDao.findById(id);
        if (ValueUtil.isEmpity(columnsEntity)) {
            return "nullValue";
        }
        columnsEntity.setParentName(columnDao.findOne(pId).getColumnsName());
        columnsEntity.setpId(pId);
        columnsEntity.setColumnsName(columnsName);
        columnDao.save(columnsEntity);
        return "success";
    }

    public String delete(Integer id) throws YesmywineException {
        ValueUtil.verify(id, "idNull");
        if(id==1||id==2||id==3){
            ValueUtil.isError("不可删除父栏目");
        }
        List<ArticleEntity> byColumnsEntity = articleDao.findByColumnsEntity(columnDao.findOne(id));
        if(byColumnsEntity.size()!=0){
            ValueUtil.isError("该栏目下有文章不可删除");
        }
        columnDao.delete(id);
        return "sucess";
    }

    public ColumnsEntity show(Integer id) throws YesmywineException {
        ValueUtil.verify(id, "idNull");
        ColumnsEntity columnsEntity = columnDao.findById(id);
        if (ValueUtil.isEmpity(columnsEntity)) {
            return null;
        }
        return columnsEntity;
    }

    public List<ColumnsEntity> index() {
        List<ColumnsEntity> columnsEntities = columnDao.findAll();
        if (ValueUtil.isEmpity(columnsEntities)) {
            return null;
        }
        return columnsEntities;
    }
}
