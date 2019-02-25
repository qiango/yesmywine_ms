package com.yesmywine.logistics.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangdiandian on 2017/3/27.
 */
@Entity
@Table(name="shippers")
public class Shippers  {
    @Id
    private Integer id;
    @Column(columnDefinition = "varchar(255) COMMENT '承运商名称'")
    private String shipperName;//承运商名称
    @Column(columnDefinition = "varchar(255) COMMENT '承运商编码'")
    private String shipperCode;//承运商编码
    @Column(columnDefinition = "varchar(255) COMMENT '简短描述'")
    private String depict;//简短描述
    @Column(columnDefinition = "int(11) COMMENT '承运商类型（0快递、1物流）'")
    private Integer shipperType;//承运商类型（0快递、1物流）
    @Column(columnDefinition = " DOUBLE COMMENT '代收费率'")
    private Double collectingRate;//代收费率
    @Column(columnDefinition = " DOUBLE COMMENT '最低代收费'")
    private Double lowestCollecting;//最低代收费
    @Column(columnDefinition = " DOUBLE COMMENT 'POS机费率'")
    private Double posRate;//POS机费率
    @Column(columnDefinition = " DOUBLE COMMENT '开始保价费'")
    private Double initialPremium;//开始保价费
    @Column(columnDefinition = " DOUBLE COMMENT '保价费率'")
    private Double insuredRate;//保价费率
    @Column(columnDefinition = " DOUBLE COMMENT '最低保价费率'")
    private Double lowestInsuredRate;//最低保价费率
    @Column(columnDefinition = " DOUBLE COMMENT '最低收费（承运商类型为物流时才需要）'")
    private Double minimumCharge;//最低收费（承运商类型为物流时才需要）
    @Column(columnDefinition = "int(11) COMMENT '状态（0启用、1停用）'")
    private Integer status; // 状态（0启用、1停用）
    @Column(columnDefinition = "int(11) COMMENT '删除标识'")
    private Integer deleteEnum;
    @Column(columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getDepict() {
        return depict;
    }

    public void setDepict(String depict) {
        this.depict = depict;
    }

    public Integer getShipperType() {
        return shipperType;
    }

    public void setShipperType(Integer shipperType) {
        this.shipperType = shipperType;
    }

    public Double getCollectingRate() {
        return collectingRate;
    }

    public void setCollectingRate(Double collectingRate) {
        this.collectingRate = collectingRate;
    }

    public Double getLowestCollecting() {
        return lowestCollecting;
    }

    public void setLowestCollecting(Double lowestCollecting) {
        this.lowestCollecting = lowestCollecting;
    }

    public Double getPosRate() {
        return posRate;
    }

    public void setPosRate(Double posRate) {
        this.posRate = posRate;
    }

    public Double getInitialPremium() {
        return initialPremium;
    }

    public void setInitialPremium(Double initialPremium) {
        this.initialPremium = initialPremium;
    }

    public Double getInsuredRate() {
        return insuredRate;
    }

    public void setInsuredRate(Double insuredRate) {
        this.insuredRate = insuredRate;
    }

    public Double getLowestInsuredRate() {
        return lowestInsuredRate;
    }

    public void setLowestInsuredRate(Double lowestInsuredRate) {
        this.lowestInsuredRate = lowestInsuredRate;
    }

    public Double getMinimumCharge() {
        return minimumCharge;
    }

    public void setMinimumCharge(Double minimumCharge) {
        this.minimumCharge = minimumCharge;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleteEnum() {
        return deleteEnum;
    }

    public void setDeleteEnum(Integer deleteEnum) {
        this.deleteEnum = deleteEnum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
