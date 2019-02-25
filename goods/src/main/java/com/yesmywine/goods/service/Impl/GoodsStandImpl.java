package com.yesmywine.goods.service.Impl;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.dao.GoodsStandDownDao;
import com.yesmywine.goods.entity.Goods;
import com.yesmywine.goods.entity.GoodsStandDown;
import com.yesmywine.goods.service.GoodsStandService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by hz on 4/14/17.
 */
@Service
public class GoodsStandImpl extends BaseServiceImpl<GoodsStandDown,Integer> implements GoodsStandService{

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private GoodsStandDownDao goodsStandDownDao;


    @Override
    public PageModel findByGoods(String goodsName, String goodsCode, Integer checkState, Integer pageNo, Integer pageSize) throws YesmywineException {
        Integer totalCount=0;
        StringBuffer str=new StringBuffer();
        str.append(" SELECT a.* FROM goodsStandDown a ");
        if(ValueUtil.notEmpity(goodsName)){
            str.append("WHERE goodsId in (SELECT b.id FROM goods b WHERE b.goodsName LIKE "+"'"+"%"+goodsName+"%"+"'" );
            if(ValueUtil.notEmpity(goodsCode)){
                str.append(" AND b.goodsCode LIKE "+"'"+"%"+goodsCode+"%') ");
                if(ValueUtil.notEmpity(checkState)){
                    str.append("AND a.checkState"+"="+checkState.toString());
                    totalCount=goodsStandDownDao.findGoods(goodsName, goodsCode, checkState);
                }
                totalCount=goodsStandDownDao.findGoodsNameAndGoodsCode(goodsName, goodsCode);
            }else if(ValueUtil.notEmpity(checkState)){
                str.append(") AND a.checkState"+"="+checkState.toString());
                totalCount=goodsStandDownDao.findGoodsNameAndstatus(goodsName, checkState);
            }else {
                totalCount=goodsStandDownDao.findGoodsName(goodsName);
                str.append(")");
            }
        }else if (ValueUtil.notEmpity(goodsCode)){
            str.append("WHERE goodsId in (SELECT b.id FROM goods b WHERE b.goodsCode LIKE "+"'"+"%"+goodsCode+"%')" );
            if(ValueUtil.notEmpity(checkState)){
                str.append(" AND a.checkState"+"="+checkState.toString());
                totalCount=goodsStandDownDao.findGoodsCodeAndstatus(goodsCode, checkState);
            }else {
                totalCount=goodsStandDownDao.findGoodsCode(goodsCode);
            }
        }else if(ValueUtil.notEmpity(checkState)){
            str.append("WHERE checkState="+checkState.toString());
            totalCount=goodsStandDownDao.findByCheckState(checkState).size();
        }else {
            totalCount=goodsStandDownDao.findBycount();
        }
        str.append(" ORDER BY createTime DESC");
        Query query = entityManager.createNativeQuery(str.toString(),GoodsStandDown.class);
        query.setFirstResult((pageNo==null?0:pageNo-1)*(pageSize==null?10:pageSize));
        query.setMaxResults(pageSize==null?10:pageSize);
        List list = query.getResultList();
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
