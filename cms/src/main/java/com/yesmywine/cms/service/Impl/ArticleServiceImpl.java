package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.cms.dao.ArticleDao;
import com.yesmywine.cms.dao.ColumnDao;
import com.yesmywine.cms.entity.ArticleEntity;
import com.yesmywine.cms.entity.ArticleResult;
import com.yesmywine.cms.entity.ColumnsEntity;
import com.yesmywine.cms.service.ArticleService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * Created by liqingqing on 2017/2/10.
 */
@Service
public class ArticleServiceImpl extends BaseServiceImpl<ArticleEntity,Integer> implements ArticleService {
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ColumnDao columnDao;
    @PersistenceContext
    private EntityManager entityManager;

    public String create(Map<String, String> parm) throws YesmywineException {



        Integer columnsId = Integer.parseInt(parm.get("columnsId"));
        String title = parm.get("title");
        String articleContent = parm.get("articleContent");

        ArticleEntity articleEntity = new ArticleEntity();
        articleEntity.setColumnsEntity(columnDao.findOne(columnsId));
        articleEntity.setTitle(title);
        articleEntity.setArticleContent(articleContent);
        if(articleContent.length()<20){
            articleEntity.setAbstracts(articleContent);
        }else {
            articleEntity.setAbstracts(articleContent.substring(0,20));
        }
        articleDao.save(articleEntity);
        return "sucess";
    }

    public String update(Integer id, Map<String, String> parm) throws YesmywineException {
        ValueUtil.verify(parm, new String[]{"id", "columnsId", "title", "articleContent"});
        parm.put("id", id.toString());
        ArticleEntity articleEntity = articleDao.findById(id);
        if (ValueUtil.isEmpity(articleEntity)) {
            return "nullValue";
        }
        Integer columnsId = Integer.parseInt(parm.get("columnsId"));
        String title = parm.get("title");
        String articleContent = parm.get("articleContent");
        articleEntity.setColumnsEntity(columnDao.findOne(columnsId));
        articleEntity.setTitle(title);
        articleEntity.setArticleContent(articleContent);
        articleEntity.setAbstracts(articleContent.substring(0,20));
        articleDao.save(articleEntity);
        return "sucess";
    }

    public ArticleEntity show(Integer id) throws YesmywineException {

        ValueUtil.verify(id, "idNull");
        ArticleEntity articleEntity = articleDao.findById(id);
        if (ValueUtil.isEmpity(articleEntity)) {
            return null;
        }
        return articleEntity;
    }

    @Override
    public JSONArray showContent(String code,Integer number) throws YesmywineException {
        ColumnsEntity columnsEntity=columnDao.findByCode(code);
        if(ValueUtil.isEmpity(number)){
            number=6;
        }
        List articleEntities = articleDao.findByColumnsName(columnsEntity.getId(),number);
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<articleEntities.size();i++){
            Object [] o = (Object[])articleEntities.get(i);
            Integer id= (Integer) o[0];
            String title=o[1].toString();
            String abstracts=null;
            if(ValueUtil.notEmpity(o[2])){
                abstracts=o[2].toString();
            }
            String time=o[3].toString();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("title",title);
            jsonObject.put("abstracts",abstracts);
            jsonObject.put("id",id);
            jsonObject.put("createTime",time);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    public String deletes(Integer id) throws YesmywineException {
        ValueUtil.verify(id, "idNull");
        articleDao.delete(id);
        return "sucess";
    }

    public List<ArticleEntity> index(Integer columnsId) throws YesmywineException {
        ValueUtil.verify(columnsId, "columnsIdNull");
        List<ArticleEntity> articleEntities = articleDao.findByColumnsEntity(columnDao.findOne(columnsId));
        if (ValueUtil.isEmpity(articleEntities)) {
            return null;
        }
        return articleEntities;
    }

    @Override
    public PageModel findByColumnName(Integer pageNo, Integer pageSize){
        Query query = entityManager.createNativeQuery("SELECT B.id,B.title,A.columnsName FROM  columns A ,article B WHERE A.id=B.columnsName",ArticleResult.class);
        query.setFirstResult(pageNo==null?0:pageNo-1);
        query.setMaxResults(pageSize==null?10:pageSize);
        List list = query.getResultList();
        Integer totalCount = articleDao.findCount();
        PageModel pageModel = new PageModel(pageNo==null?1:pageNo,pageSize==null?10:pageSize);
        pageModel.setTotalRows(Long.valueOf(totalCount));
        long tempTPd = pageModel.getTotalRows() % pageModel.getPageSize();
        Integer tempTp = Integer.valueOf((pageModel.getTotalRows() / pageModel.getPageSize()) + "");
        if (tempTPd == 0) {
            pageModel.setTotalPages(tempTp);
        } else {
            pageModel.setTotalPages(tempTp + 1);
        }
        pageModel.setContent(list);
        return pageModel;
    }

}
