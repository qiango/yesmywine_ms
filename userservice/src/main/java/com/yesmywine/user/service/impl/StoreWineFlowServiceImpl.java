package com.yesmywine.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.user.dao.StoreWineflowAttachDao;
import com.yesmywine.user.entity.StoreWineFlow;
import com.yesmywine.user.entity.StoreWineflowAttach;
import com.yesmywine.user.service.StoreWineFlowService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${shuang} on 2017/8/14.
 */
@Service
public class StoreWineFlowServiceImpl extends BaseServiceImpl<StoreWineFlow, Integer> implements StoreWineFlowService {
    @Autowired
    private StoreWineflowAttachDao storeWineflowAttachDao;

    //存酒记录分页
    @Override
    public PageModel page(PageModel pageModel) throws YesmywineException {
        List<JSONObject> list = pageModel.getContent();
        if(list.size()==0){
            return pageModel;
        }
        List<Object> newList = new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            String storeWineFlow = ValueUtil.toJson(list.get(i));
            JSONObject newJson = JSONObject.parseObject(ValueUtil.getFromJson(storeWineFlow,"data"));
            String extractorderNumber = newJson.getString("extractorderNumber");
            List<StoreWineflowAttach>  list1 = storeWineflowAttachDao.findByExtractorderNumber(extractorderNumber);
            newJson.put("goods",list1);
            newList.add(newJson);
        }
        pageModel.setContent(newList);
//        System.out.println(list.get(0));
        return pageModel;
    }
}
