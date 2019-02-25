package com.yesmywine.cms.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.cms.dao.FlashPurchaseFirstDao;
import com.yesmywine.cms.dao.FlashPurchaseSecentDao;
import com.yesmywine.cms.dao.PanicBuyingFirstDao;
import com.yesmywine.cms.dao.PanicBuyingSecentDao;
import com.yesmywine.cms.entity.FlashPurchaseFirst;
import com.yesmywine.cms.entity.FlashPurchaseSecent;
import com.yesmywine.cms.entity.PanicBuyingFirst;
import com.yesmywine.cms.entity.PanicBuyingSecent;
import com.yesmywine.cms.service.AdverService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by yly on 2017/1/16.
 */
@RestController
@RequestMapping("/cms/delGoods")
public class DelGoodsController {
    @Autowired
    private FlashPurchaseFirstDao flashPurchaseFirstDao;
    @Autowired
    private FlashPurchaseSecentDao flashPurchaseSecentDao;
    @Autowired
    private PanicBuyingFirstDao panicBuyingFirstDao;
    @Autowired
    private PanicBuyingSecentDao panicBuyingSecentDao;

    @RequestMapping(value = "/itf", method = RequestMethod.POST)
    public String saveAdver(Integer activityId) {
        try {
            List<FlashPurchaseFirst> byActivityId =
                    this.flashPurchaseFirstDao.findByActivityId(activityId);
            for(FlashPurchaseFirst flashPurchaseFirst: byActivityId){
                Integer id = flashPurchaseFirst.getId();
                List<FlashPurchaseSecent> byFlashPurchaseFirstId =
                        this.flashPurchaseSecentDao.findByFlashPurchaseFirstId(id);
                for(FlashPurchaseSecent flashPurchaseSecent: byFlashPurchaseFirstId){
                    this.flashPurchaseSecentDao.delete(flashPurchaseSecent);
                }
            }
            List<PanicBuyingFirst> byActivityId1 =
                    this.panicBuyingFirstDao.findByActicityId(activityId);
            for(PanicBuyingFirst panicBuyingFirst: byActivityId1){
                Integer id = panicBuyingFirst.getId();
                List<PanicBuyingSecent> byPanicBuyingFirstId =
                        this.panicBuyingSecentDao.findByPanicBuyingFirstId(id);
                for(PanicBuyingSecent panicBuyingSecent: byPanicBuyingFirstId){
                    this.panicBuyingSecentDao.delete(panicBuyingSecent);
                }
            }

            return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
        } catch (Exception e) {
            return ValueUtil.toError("500", "erro", e.getMessage());
        }

    }


}
