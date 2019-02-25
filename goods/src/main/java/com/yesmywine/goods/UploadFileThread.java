package com.yesmywine.goods;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.goods.dao.GoodsMirrorDao;
import com.yesmywine.goods.entity.GoodsMirror;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * Created by Administrator on 2017/8/5/005.
 */
public class UploadFileThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(UploadFileThread.class);

    private GoodsMirrorDao goodsMirrorDao;

    private Integer mirrorId;

    private Integer[] imgIds;

    private Integer goodsId;

    public UploadFileThread(GoodsMirrorDao goodsMirrorDao, Integer goodsId,Integer mirrorId, Integer[] imgIds) {
        this.goodsMirrorDao = goodsMirrorDao;
        this.goodsId = goodsId;
        this.mirrorId = mirrorId;
        this.imgIds = imgIds;
    }

    @Override
    public void run() {
        try{

            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/fileUpload/tempToFormal/itf", RequestMethod.post);
            httpRequest.addParameter("module", "goods");
            httpRequest.addParameter("mId", goodsId);
            String ids = "";
            String imageIds = "";
            for (int i = 0; i < imgIds.length; i++) {
                if (i == 0) {
                    ids = ids + imgIds[i];
                } else {
                    ids = ids + "," + imgIds[i];
                }
                httpRequest.addParameter("id", ids);
            }
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            logger.info("图片服务返回数据为==》" + temp);
            String cd = ValueUtil.getFromJson(temp, "code");
            if (!"201".equals(cd) || ValueUtil.isEmpity(cd)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("图片上传失败");
            } else {
                JSONArray maps = new JSONArray(imgIds.length);
                String result = ValueUtil.getFromJson(temp, "data");
                JsonParser jsonParser = new JsonParser();
                JsonArray image = jsonParser.parse(result).getAsJsonArray();
                for (int f = 0; f < image.size(); f++) {
                    String id = image.get(f).getAsJsonObject().get("id").getAsString();
                    String name = image.get(f).getAsJsonObject().get("name").getAsString();
                    com.alibaba.fastjson.JSONObject map1 = new com.alibaba.fastjson.JSONObject();
                    map1.put("id", id);
                    map1.put("name", name);
                    maps.add(map1);
                }

                logger.info("goodsMirrorDao==》" + goodsMirrorDao);

                String result1 =   maps.toJSONString().replaceAll( "\"", "\'");
                GoodsMirror goodsMirror = goodsMirrorDao.findOne(mirrorId);
                goodsMirror.setGoodsImageUrl(result1);
                goodsMirrorDao.save(goodsMirror);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
