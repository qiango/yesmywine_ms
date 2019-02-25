package com.yesmywine.goods.entityProperties;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.goods.bean.DeleteEnum;
import com.yesmywine.goods.bean.SupplierTypeEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by WANG, RUIQING on 12/7/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "supplier")
public class Supplier extends BaseEntity<Integer> {

    @Column(columnDefinition = "varchar(200) COMMENT '供应商名称'")
    private String supplierName;  //供应商名称
    @Column(columnDefinition = "varchar(200) COMMENT '供应商编码'")
    private String supplierCode;//供应商编码
    @Enumerated(EnumType.STRING)
    private SupplierTypeEnum supplierType;//供应商分类
    @Column(columnDefinition = "int(11) COMMENT '省id'")
    private Integer provinceId;//省
    @Column(columnDefinition = "varchar(200) COMMENT '省'")
    private String province;//省
    @Column(columnDefinition = "int(11) COMMENT '市id'")
    private Integer cityId;//市
    @Column(columnDefinition = "varchar(200) COMMENT '市'")
    private String city;//市
    @Column(columnDefinition = "int(11) COMMENT '区id'")
    private Integer areaId;//区
    @Column(columnDefinition = "varchar(200) COMMENT '区'")
    private String area;//区
    @Column(columnDefinition = "varchar(200) COMMENT '地址'")
    private String address;//地址
    @Column(columnDefinition = "varchar(200) COMMENT '邮编'")
    private String postCode;//邮编
    @Column(columnDefinition = "varchar(200) COMMENT '联系人'")
    private String contact;//联系人
    @Column(columnDefinition = "varchar(200) COMMENT '电话'")
    private String telephone;//电话
    @Column(columnDefinition = "varchar(200) COMMENT '手机'")
    private String mobilePhone;//手机
    @Column(columnDefinition = "varchar(200) COMMENT '传真'")
    private String fax;//传真
    @Column(columnDefinition = "varchar(200) COMMENT '邮箱'")
    private String mailbox;//邮箱
    @Column(columnDefinition = "varchar(200) COMMENT '等级'")
    private String grade;//等级
    @Column(columnDefinition = "varchar(200) COMMENT '账号'")
    private String accountNumber;//账号
    @Column(columnDefinition = "varchar(200) COMMENT '信用度'")
    private String credit;//信用度
    @Column(columnDefinition = "varchar(200) COMMENT '采购周期'")
    private String procurementCycl;//采购周期
    @Column(columnDefinition = "varchar(200) COMMENT '支付方式  0-支付宝、 1-银联 、2-直联、3-货到付款、3-后结帐'")
    private String paymentType;//支付方式  0-支付宝、 1-银联 、2-直联、3-货到付款、3-后结帐
    @Column(columnDefinition = "varchar(200) COMMENT '收发票公司名称'")
    private String invoiceCompany;//收发票公司名称
    @Column(columnDefinition = "varchar(200) COMMENT '一级供应商'")
    private String primarySupplier;//一级供应商
    @Column(columnDefinition = "varchar(200) COMMENT '商家标识'")
    private String merchantIdentification;//商家标识
    @Column(columnDefinition = "varchar(200) COMMENT '产品经理'")
    private String productManager;//产品经理
    @Column(columnDefinition = "varchar(200) COMMENT '开户行'")
    private String bank;//开户行
    @Column(columnDefinition = "varchar(200) COMMENT '银行账号'")
    private String bankAccount;//银行账号
    @Column(columnDefinition = "varchar(200) COMMENT '税号'")
    private String dutyParagraph;//税号
    @Column(columnDefinition = " DATETIME COMMENT '账期'")
    private Date paymentDays;//账期
    @Enumerated(EnumType.ORDINAL)
    private DeleteEnum deleteEnum;  //ordinal枚举存数字

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public SupplierTypeEnum getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(SupplierTypeEnum supplierType) {
        this.supplierType = supplierType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getProcurementCycl() {
        return procurementCycl;
    }

    public void setProcurementCycl(String procurementCycl) {
        this.procurementCycl = procurementCycl;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Date getPaymentDays() {
        return paymentDays;
    }

    public void setPaymentDays(Date paymentDays) {
        this.paymentDays = paymentDays;
    }

    public String getInvoiceCompany() {
        return invoiceCompany;
    }

    public void setInvoiceCompany(String invoiceCompany) {
        this.invoiceCompany = invoiceCompany;
    }

    public String getPrimarySupplier() {
        return primarySupplier;
    }

    public void setPrimarySupplier(String primarySupplier) {
        this.primarySupplier = primarySupplier;
    }

    public String getMerchantIdentification() {
        return merchantIdentification;
    }

    public void setMerchantIdentification(String merchantIdentification) {
        this.merchantIdentification = merchantIdentification;
    }

    public String getProductManager() {
        return productManager;
    }

    public void setProductManager(String productManager) {
        this.productManager = productManager;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getDutyParagraph() {
        return dutyParagraph;
    }

    public void setDutyParagraph(String dutyParagraph) {
        this.dutyParagraph = dutyParagraph;
    }


    public DeleteEnum getDeleteEnum() {
        return deleteEnum;
    }

    public void setDeleteEnum(DeleteEnum deleteEnum) {
        this.deleteEnum = deleteEnum;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }
}
