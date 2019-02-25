package com.yesmywine.logistics.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.logistics.dao.ShipperDao;
import com.yesmywine.logistics.entity.Shippers;
import com.yesmywine.logistics.service.ShipperService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by wangdiandian on 2017/4/6.
 */
@Service
public class ShipperImpl extends BaseServiceImpl<Shippers,Integer> implements ShipperService {
    @Autowired
    private ShipperDao shipperDao;

    public String saveShipper(String jsonData) throws YesmywineException {//新增承运商或修改承运商
        String id1 = ValueUtil.getFromJson(jsonData, "data", "id");//承运商id
        String shipperName = ValueUtil.getFromJson(jsonData, "data", "shipperName");//承运商名称
        String shipperCode = ValueUtil.getFromJson(jsonData, "data", "shipperCode");//承运商编码
        String depict = ValueUtil.getFromJson(jsonData, "data", "depict");//简短描述
        String shipperType = ValueUtil.getFromJson(jsonData, "data", "shipperType");//承运商类型（快递、物流）
        String collectingRate = ValueUtil.getFromJson(jsonData, "data", "collectingRate");//代收费率
        String lowestCollecting = ValueUtil.getFromJson(jsonData, "data", "lowestCollecting");//最低代收费
        String posRate = ValueUtil.getFromJson(jsonData, "data", "posRate");////POS机费率
        String initialPremium = ValueUtil.getFromJson(jsonData, "data", "initialPremium");//开始保价费
        String insuredRate = ValueUtil.getFromJson(jsonData, "data", "insuredRate");//保价费率
        String lowestInsuredRate = ValueUtil.getFromJson(jsonData, "data", "lowestInsuredRate");//最低保价费率
        String status = ValueUtil.getFromJson(jsonData, "data", "status");// 状态（0启用、1停用）

        Integer id = Integer.valueOf(id1);
        Shippers shippers1 = shipperDao.findOne(id);
        if (shippers1 == null) {
            Shippers shippers = new Shippers();
            shippers.setId(id);
            shippers.setShipperName(shipperName);
            shippers.setShipperCode(shipperCode);
            shippers.setDepict(depict);
            switch (shipperType) {
                case "0":
                    shippers.setShipperType(0);
                    break;
                default:
                    shippers.setShipperType(1);
                    String minimumCharge = ValueUtil.getFromJson(jsonData, "data", "minimumCharge");//最低收费（承运商类型为物流时才需要）
                    shippers.setMinimumCharge(Double.valueOf(minimumCharge));
                    break;
            }
            shippers.setCollectingRate(Double.valueOf(collectingRate));
            shippers.setLowestCollecting(Double.valueOf(lowestCollecting));
            shippers.setPosRate(Double.valueOf(posRate));
            shippers.setInitialPremium(Double.valueOf(initialPremium));
            shippers.setInsuredRate(Double.valueOf(insuredRate));
            shippers.setLowestInsuredRate(Double.valueOf(lowestInsuredRate));
            shippers.setStatus(Integer.valueOf(status));
            shippers.setDeleteEnum(0);
            shippers.setCreateTime(new Date());
            shipperDao.save(shippers);
        } else {
            shippers1.setShipperName(shipperName);
            shippers1.setDepict(depict);
                switch (shipperType) {
                    case "0":
                        shippers1.setShipperType(0);
                        break;
                    default:
                        shippers1.setShipperType(1);
                        String minimumCharge = ValueUtil.getFromJson(jsonData, "data", "minimumCharge");//最低收费（承运商类型为物流时才需要）
                        shippers1.setMinimumCharge(Double.valueOf(minimumCharge));
                        break;
                }
            shippers1.setCollectingRate(Double.valueOf(collectingRate));
            shippers1.setLowestCollecting(Double.valueOf(lowestCollecting));
            shippers1.setPosRate(Double.valueOf(posRate));
            shippers1.setInitialPremium(Double.valueOf(initialPremium));
            shippers1.setInsuredRate(Double.valueOf(insuredRate));
            shippers1.setLowestInsuredRate(Double.valueOf(lowestInsuredRate));
            shippers1.setStatus(Integer.valueOf(status));
            shippers1.setDeleteEnum(0);
                shipperDao.save(shippers1);
            }
            return "success";
    }
    public Shippers updateLoad(Integer id) throws YesmywineException {//加载显示承运商
        ValueUtil.verify(id, "idNull");
        Shippers shippers = shipperDao.findOne(id);
        return shippers;
    }

    public String delete(String jsonData) throws YesmywineException {//删除承运商
        String id1 = ValueUtil.getFromJson(jsonData, "data", "id");//承运商id
        Integer id=Integer.valueOf(id1);
        ValueUtil.verify(id, "idNull");
        Shippers shippers = shipperDao.findOne(id);
        shippers.setDeleteEnum(1);
        shipperDao.save(shippers);
        return "success";
    }

//    public String updateSave(String json) throws YesmywineException {//修改保存供应商
//        JSONArray adjustArray = JSON.parseArray(json);
//        for (int i = 0; i < adjustArray.size(); i++) {
//            JSONObject adjustCommand = (JSONObject) adjustArray.get(i);
//            Integer shipperId = adjustCommand.getInteger("id");
//            Shippers shippers = shipperDao.findOne(shipperId);
//            String shipperName = adjustCommand.getString("shipperName");//承运商名称
//            String depict = adjustCommand.getString("depict");//简短描述
//            String shipperType = adjustCommand.getString("shipperType");////承运商类型（快递、物流）
//            String collectingRate = adjustCommand.getString("collectingRate");//代收费率
//            String lowestCollecting = adjustCommand.getString("lowestCollecting");//最低代收费
//            String posRate = adjustCommand.getString("posRate");//POS机费率
//            String initialPremium = adjustCommand.getString("initialPremium");//开始保价费
//            String insuredRate = adjustCommand.getString("insuredRate");//保价费率
//            String lowestInsuredRate = adjustCommand.getString("lowestInsuredRate");//最低保价费率
//            String status = adjustCommand.getString("status");
//            shippers.setShipperName(shipperName);
////        shippers.setShipperCode(param.get("shipperCode"));
//            shippers.setDepict(depict);
//            switch (shipperType) {
//                case "0":
//                    shippers.setShipperType(ShipperTypeEnum.express);
//                    break;
//                default:
//                    shippers.setShipperType(ShipperTypeEnum.logistics);
//                    String minimumCharge = adjustCommand.getString("minimumCharge");//最低保价费率
//                    shippers.setMinimumCharge(Double.valueOf(minimumCharge));
//                    break;
//            }
//            shippers.setCollectingRate(Double.valueOf(collectingRate));
//            shippers.setLowestCollecting(Double.valueOf(lowestCollecting));
//            shippers.setPosRate(Double.valueOf(posRate));
//            shippers.setInitialPremium(Double.valueOf(initialPremium));
//            shippers.setInsuredRate(Double.valueOf(insuredRate));
//            shippers.setLowestInsuredRate(Double.valueOf(lowestInsuredRate));
//            switch (status) {
//                case "0":
//                    shippers.setStatus(StatusEnum.enable);
//                    break;
//                default:
//                    shippers.setStatus(StatusEnum.disable);
//                    break;
//            }
//            shippers.setDeleteEnum(DeleteEnum.NOT_DELETE);
//            shipperDao.save(shippers);
//        }
//        return "success";
//
//    }

    public Shippers shippersName(String shipperCode) throws YesmywineException {//根据承运商code显示承运商
        Shippers shippers= shipperDao.findByShipperCode(shipperCode);
        if(shippers==null) {
            ValueUtil.isError("没有承运商");
        }
            return shippers;
    }
}

