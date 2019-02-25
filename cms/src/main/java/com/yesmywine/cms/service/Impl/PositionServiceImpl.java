package com.yesmywine.cms.service.Impl;


import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.cms.dao.*;
import com.yesmywine.cms.entity.*;
import com.yesmywine.cms.service.PositionService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yly on 2017/2/10.
 */
@Service
@Transactional
public class PositionServiceImpl extends BaseServiceImpl<PositionEntity, Integer> implements PositionService {
    //注入广告位的DAO
    @Autowired
    private PositionEntityDao positionDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private AdverDao adverDao;
    @Autowired
    private ActivityColumnDao activityColumnDao;
    @Autowired
    private ActivityFirstDao activityFirstDao;
    @Autowired
    private AppAdvertisingDao appAdvertisingDao;
    @Autowired
    private FlashPurchasePositionDao flashPurchasePositionDao;
    @Autowired
    private HomePagePositionDao homePagePositionDao;
    @Autowired
    private OldFirstDao oldFirstDao;
    @Autowired
    private OldPositionDao oldPositionDao;
    @Autowired
    private PanicBuyingPositionDao panicBuyingPositionDao;
    @Autowired
    private PlateFirstDao plateFirstDao;
    @Autowired
    private SaleFirstDao saleFirstDao;
    @Autowired
    private SalePositionDao salePositionDao;


    @Override
    public String savePosition(PositionEntity position, String adverIds) {
        try{
            String[] split = adverIds.split(",");
            if(position.getPositionType()==0 && split.length>1){
                return "此广告位是单图";
            }
            String positionName = position.getPositionName();
            PositionEntity position1 =positionDao.findByPositionName(positionName);
            if(ValueUtil.notEmpity(position1)){
                return "名称已被使用";
            }
            positionDao.save(position);
            for(String adver: split){
                AdverPositionEntity adverPositionEntity = new AdverPositionEntity();
                AdverEntity one = this.adverDao.findOne(Integer.valueOf(adver));
                if(ValueUtil.isEmpity(one)){
                    return adver+":此资源不存在";
                }
                adverPositionEntity.setAdverEntity(one);
                adverPositionEntity.setPositionEntity(position);
                this.adverPositionDao.save(adverPositionEntity);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "erro";
        }
        return "success";
    }


    @Override
    public String delete(Integer positionId) throws Exception{
        try{
            Boolean used = this.used(positionId);
            if(!used){
                return "已被使用，不能删除";
            }
//            PositionEntity positionEntity = new PositionEntity();
//            positionEntity.setId(positionId);
//            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);

            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(positionId);
            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
            this.positionDao.delete(positionId);

            for(AdverPositionEntity adverPositionEntity: byPositionEntity){
                this.adverPositionDao.delete(adverPositionEntity);
            }
        }catch (Exception e){
            throw e;
        }
        return "success";
    }



    @Override
    public String update(PositionEntity position, String adverIds) {
        try {
            String positionName = position.getPositionName();
            PositionEntity position1 =positionDao.findByPositionName(positionName);
            if(ValueUtil.notEmpity(position1)){
                if(position1.getId()!=position.getId()){
                    return "名称已被使用";
                }
            }

            //删除原来的广告资源。
            List<AdverPositionEntity> PositionEntityList = this.adverPositionDao.findByPositionEntity(position);
            for(AdverPositionEntity adverPositionEntity: PositionEntityList){
                adverPositionDao.delete(adverPositionEntity);
            }
            //添加新的。
            String[] split = adverIds.split(",");
            if(position.getPositionType()==0 && split.length>1){
                return "此广告位是单图";
            }
            ArrayList<AdverPositionEntity> arrayList = new ArrayList<>();
            for(String adver: split){
                AdverPositionEntity adverPositionEntity = new AdverPositionEntity();
                AdverEntity newAdverEntity = adverDao.findOne(Integer.valueOf(adver));
                if(ValueUtil.isEmpity(newAdverEntity)){
                    return adver+":此资源不存在";
                }
                adverPositionEntity.setAdverEntity(newAdverEntity);
                adverPositionEntity.setPositionEntity(position);
                List<AdverPositionEntity> byAdverEntityAndPositionEntity = this.adverPositionDao.findByAdverEntityAndPositionEntity(newAdverEntity, position);
                if(byAdverEntityAndPositionEntity.size()==0) {
                    arrayList.add(adverPositionEntity);
                }
            }
            adverPositionDao.save(arrayList);
            positionDao.save(position);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "erro";
        }
        return "success";

     /*
        try {
            String[] split = adverIds.split(",");
            if(position.getPositionType()==0 && split.length>1){
                return "此广告位是单图";
            }
            positionDao.save(position);
            for(String adver: split){
                AdverPositionEntity adverPositionEntity = new AdverPositionEntity();
                AdverEntity one = this.adverDao.findOne(Integer.valueOf(adver));
                if(ValueUtil.isEmpity(one)){
                    return adver+":此资源不存在";
                }
                adverPositionEntity.setAdverEntity(one);
                adverPositionEntity.setPositionEntity(position);
                List<AdverPositionEntity> byAdverEntityAndPositionEntity = this.adverPositionDao.findByAdverEntityAndPositionEntity(one, position);
                if(byAdverEntityAndPositionEntity.size()==0) {
                    this.adverPositionDao.save(adverPositionEntity);
                }
            }
        }catch (ExceptionThread e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "erro";
        }
        return "success";*/
    }


    public Boolean used(Integer positionId){
        List<ActivityColumn> b1 = this.activityColumnDao.findByPositionId(positionId);
        List<ActivityFirst> b2 = this.activityFirstDao.findByPositionIdBanner(positionId);
        List<AppAdvertising> b3 = this.appAdvertisingDao.findByTopIdOrCollaborateIdOrImportantId(positionId,
                positionId, positionId);
        List<FlashPurchasePosition> b4 = this.flashPurchasePositionDao.findByPositionId(positionId);
        List<HomePagePosition> b5 = this.homePagePositionDao
                .findByBannerPositionIdOrPositionIdOneOrPositionIdTwoOrPositionIdThree(positionId,
                        positionId, positionId, positionId);
        List<OldFirst> b6 = this.oldFirstDao.findByPositionId(positionId);
        List<OldPosition> b7 = this.oldPositionDao.findByFirstPositionIdOrSecentPositionId(positionId,
                positionId);
        List<PanicBuyingPosition> b8 = this.panicBuyingPositionDao.findByPositionId(positionId);
        List<PlateFirst> b9 = this.plateFirstDao.findByFirstPositionIdOrSecentPositionIdOrThirdPositionIdOrFourthPositionIdOrAppPositionId(positionId,
                positionId, positionId, positionId, positionId);
        List<SaleFirst> b10 = this.saleFirstDao.findByPositionId(positionId);
        List<SalePosition> b11 = this.salePositionDao.findByPositionId(positionId);

        if(b1.size()>0 || b2.size() > 0 || b3.size() > 0|| b4.size() > 0|| b5.size() > 0|| b6.size() > 0|| b7.size() > 0
                || b8.size() > 0|| b9.size() > 0|| b10.size() > 0|| b11.size() > 0) {
            return false;
        }
        return true;
    }

}
