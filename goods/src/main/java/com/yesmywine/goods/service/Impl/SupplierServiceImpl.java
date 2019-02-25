package com.yesmywine.goods.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.bean.DeleteEnum;
import com.yesmywine.goods.bean.SupplierTypeEnum;
import com.yesmywine.goods.dao.SkuDao;
import com.yesmywine.goods.dao.SupplierDao;
import com.yesmywine.goods.entity.Sku;
import com.yesmywine.goods.entityProperties.Supplier;
import com.yesmywine.goods.service.SupplierService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.date.DateUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/3/15.
 */
@Service
public class SupplierServiceImpl extends BaseServiceImpl<Supplier, Integer> implements SupplierService {

    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private SkuDao skuDao;

    public String addSupplier(Map<String, String> param) throws YesmywineException {//新增供应商
        Supplier supplier = new Supplier();
        supplier.setSupplierName(param.get("supplierName"));
//        String supplierCode = Encode.getSalt(3);//生成供应商编码
        supplier.setSupplierCode(param.get("supplierCode"));
        String supplierType = param.get("supplierType");
        switch (supplierType) {
            case "distribution":
                supplier.setSupplierType(SupplierTypeEnum.distribution);
                break;
            case "consignment":
                supplier.setSupplierType(SupplierTypeEnum.consignment);
                break;
            case "seaAmoy":
                supplier.setSupplierType(SupplierTypeEnum.seaAmoy);
                break;
        }
        supplier.setProvince(param.get("province"));
        supplier.setProvinceId(Integer.valueOf(param.get("provinceId")));
        supplier.setCity(param.get("city"));
        supplier.setCityId(Integer.valueOf(param.get("cityId")));
        supplier.setArea(param.get("area"));
        supplier.setAreaId(Integer.valueOf(param.get("areaId")));
        supplier.setAddress(param.get("address"));
        supplier.setPostCode(param.get("postCode"));
        supplier.setContact(param.get("contact"));
        supplier.setTelephone(param.get("telephone"));
        supplier.setMobilePhone(param.get("mobilePhone"));
        supplier.setFax(param.get("fax"));
        supplier.setMailbox(param.get("mailbox"));
        supplier.setGrade(param.get("grade"));
        supplier.setAccountNumber(param.get("accountNumber"));
        supplier.setCredit(param.get("credit"));
        supplier.setProcurementCycl(param.get("procurementCycl"));
        supplier.setPaymentType(param.get("paymentType"));
        supplier.setInvoiceCompany(param.get("invoiceCompany"));
        supplier.setPrimarySupplier(param.get("primarySupplier"));
//        String imageIds = param.get("merchantLogo");
//        if(ValueUtil.notEmpity(imageIds)){
//            Integer length=Integer.valueOf(param.get("num").toString());
//            Map[] image = new Map[length];
//            for (int i=0;i<image.length;i++) {
//                Map<String, String> map1 = new HashMap<>();
//                map1.put("id", String.valueOf(param.get("id" + i)));
//                map1.put("name", String.valueOf(param.get("name" + i)));
//                image[i] = map1;
//            }
//            supplier.setMerchantLogo(image);
//        }

        supplier.setProductManager(param.get("productManager"));
        supplier.setBank(param.get("bank"));
        supplier.setBankAccount(param.get("bankAccount"));
        supplier.setDutyParagraph(param.get("dutyParagraph"));
        String paymentDays =param.get("paymentDays");
        if(paymentDays!=null) {
            supplier.setPaymentDays(DateUtil.toDate(paymentDays, "yyyy-mm-dd"));
        }
        supplier.setDeleteEnum(DeleteEnum.NOT_DELETE);
        supplierDao.save(supplier);
        return "success";
    }

    public Supplier updateLoad(Integer id) throws YesmywineException {//加载显示供应商
        ValueUtil.verify(id, "idNull");
        Supplier suppliers = supplierDao.findOne(id);
        return suppliers;
    }
    public String delete(Integer id) throws YesmywineException {//删除供应商
        ValueUtil.verify(id, "idNull");
//        查看供应商，被使用，不能删除
        Supplier supplier=new Supplier();
        supplier.setId(id);
        List<Sku> sku=skuDao.findBySupplier(supplier);
        if(sku.size()!=0){
            return "supplierUsed";
        }
        Supplier supplier1 = supplierDao.findOne(id);
        supplier1.setDeleteEnum(DeleteEnum.DELETED);
        supplierDao.save(supplier1);
        return "success";
    }

    @Override
    public String synchronous(Map<String, String> param) throws YesmywineException {
        if(0==Integer.parseInt(String.valueOf(param.get("synchronous")))){
            addSupplier(param);
            return "add success";
        }else if(2==Integer.parseInt(String.valueOf(param.get("synchronous")))){
            Supplier supplier1 = supplierDao.findBySupplierCode(param.get("supplierCode"));
            supplier1.setDeleteEnum(DeleteEnum.DELETED);
            supplierDao.save(supplier1);
            return "delete success";
        }else {
            updateSave(param);
            return "update success";
        }

    }

    public String updateSave(Map<String, String> param) throws YesmywineException {//修改保存供应商
        Integer supplierId = Integer.parseInt(param.get("id"));
//        Supplier supplier = supplierDao.findOne(supplierId);
        Supplier supplier = supplierDao.findBySupplierCode(param.get("supplierCode"));

        supplier.setSupplierName(param.get("supplierName"));
        String supplierType = param.get("supplierType");
        switch (supplierType) {
            case "distribution":
                supplier.setSupplierType(SupplierTypeEnum.distribution);
                break;
            case "consignment":
                supplier.setSupplierType(SupplierTypeEnum.consignment);
                break;
            case "seaAmoy":
                supplier.setSupplierType(SupplierTypeEnum.seaAmoy);
                break;
        }
        supplier.setProvince(param.get("province"));
        supplier.setProvinceId(Integer.valueOf(param.get("provinceId")));
        supplier.setCity(param.get("city"));
        supplier.setCityId(Integer.valueOf(param.get("cityId")));
        supplier.setArea(param.get("area"));
        supplier.setAreaId(Integer.valueOf(param.get("areaId")));
        supplier.setAddress(param.get("address"));
        supplier.setPostCode(param.get("postCode"));
        supplier.setContact(param.get("contact"));
        supplier.setTelephone(param.get("telephone"));
        supplier.setMobilePhone(param.get("mobilePhone"));
        supplier.setFax(param.get("fax"));
        supplier.setMailbox(param.get("mailbox"));
        supplier.setGrade(param.get("grade"));
        supplier.setAccountNumber(param.get("accountNumber"));
        supplier.setCredit(param.get("credit"));
        supplier.setProcurementCycl(param.get("procurementCycl"));
        supplier.setPaymentType(param.get("paymentType"));
        supplier.setInvoiceCompany(param.get("invoiceCompany"));
        supplier.setPrimarySupplier(param.get("primarySupplier"));
//        supplier1.setMerchantLogo(param.get("merchantLogo"));
        supplier.setProductManager(param.get("productManager"));
        supplier.setBank(param.get("bank"));
        supplier.setBankAccount(param.get("bankAccount"));
        supplier.setDutyParagraph(param.get("dutyParagraph"));
        String paymentDays =param.get("paymentDays");
        if(paymentDays!=null) {
            supplier.setPaymentDays(DateUtil.toDate(paymentDays, "yyyy-mm-dd"));
        }
        supplierDao.save(supplier);
        return "success";

    }

}
