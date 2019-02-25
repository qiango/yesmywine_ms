package com.yesmywine.activity.service.impl;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.bean.WareEnum;
import com.yesmywine.activity.ifttt.dao.ActivityGoodsDao;
import com.yesmywine.activity.entity.ActivityGoods;
import com.yesmywine.activity.entity.GoodsMirroring;
import com.yesmywine.activity.service.ActivityGoodsService;
import com.yesmywine.activity.service.ActivityService;
import com.yesmywine.activity.ifttt.service.GoodsService;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.db.base.ehcache.CacheStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by SJQ on 2017/6/16.
 */
@Service
public class ActivityGoodsServiceImpl extends BaseServiceImpl<ActivityGoods, Integer>
        implements ActivityGoodsService {

    @PersistenceContext
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Autowired
    private ActivityGoodsDao activityGoodsDao;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ActivityService activityService;


    @Override
    public PageModel findByActivityId(Integer activityId,Integer pageNo,Integer pageSize) {
        Query query = entityManager.createNativeQuery("select * from goodsMirroring g where g.goStatus=1 and g.goodsId in (select irg.targetId from regulationGoods irg where irg.activityId=:activityId and irg.ware=:ware) ",GoodsMirroring.class);
        query.setParameter("activityId",activityId);
        query.setParameter("ware", WareEnum.Main.getValue());
        query.setFirstResult(pageNo==null?0:pageNo-1);
        query.setMaxResults(pageSize==null?10:pageSize);
        List list = query.getResultList();
        Integer totalCount = goodsService.CountByActivityIdAndWare(activityId,WareEnum.Main);
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

    @Override
    public void deleteByRegulationId(Integer id) {
        activityGoodsDao.deleteByRegulationId(id);
    }

    @Override
//    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'ActivityGoodsServiceImpl_findByGoodsId_'+#goodsId")
    public List<ActivityGoods> findByGoodsId(String goodsId) {
//        GoodsMirroring goodsMirroring = goodsService.findById(goodsId);
        GoodsMirroring goodsMirroring = new GoodsMirroring();
        goodsMirroring.setGoodsId(goodsId);
        return activityGoodsDao.findByGoods(goodsId);
    }

    @Override
    public void deleteByActivityId(Integer activityId) {
        activityGoodsDao.deleteByActivityId(activityId);
    }

    @Override
    public List<ActivityGoods> findByActivityId(Integer id) {
        return activityGoodsDao.findByActivityId(id);
    }

    @Override
    public List<ActivityGoods> isJoinOtherRushPur(Integer targetId, String startTime) {
        return activityGoodsDao.isJoinOtherRushPur(targetId,startTime);
    }

    @Override
    public List<String> checkGoodsIsJoinOtherActivity(List<Integer> activityIds, String[] goodsIds) {
        return activityGoodsDao.checkGoodsIsJoinOtherActivity(activityIds,goodsIds);
    }

    @Override
    public List<String> checkGoodsIsJoinShareActivity(List<Integer> activityIds, String[] goodsIds) {
        return activityGoodsDao.checkGoodsIsJoinShareActivity(activityIds,goodsIds);
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'findByGoodsIdAndActivityStatus'+#goodsId+#status")
    public List<ActivityGoods> findByGoodsIdAndActivityStatus(String goodsId, ActivityStatus status) {
        return activityGoodsDao.findByGoodsIdAndActivityStatus(goodsId,status.name());
    }

    @Override
    public void delete(Integer id) {
        activityGoodsDao.delete(id);
    }

    @Override
    public void deleteByActivityIdAndGoods(Integer activityId, Integer goodsId) {
        GoodsMirroring goodsMirroring = goodsService.findById(goodsId.toString());
        ActivityGoods activityGoods = activityGoodsDao.findByActivityIdAndGoodsId(activityId,goodsMirroring.getId());
//        activityGoodsDao.deleteByActivityIdAndGoods(activityId,goodsMirroring);
        activityGoodsDao.delete(activityGoods);
    }

}
