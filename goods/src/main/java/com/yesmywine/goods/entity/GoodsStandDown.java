package com.yesmywine.goods.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hz on 4/12/17.
 */
@Entity
@Table(name = "goodsStandDown")
public class GoodsStandDown extends BaseEntity<Integer>{

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "goodsId")
    private Goods goods;
    @Column(columnDefinition = "DATETIME COMMENT '申请时间'")
    private Date applyTime;
    @Column(columnDefinition = "varchar(11) COMMENT '申请人'")
    private String applyUser;
    @Column(columnDefinition = "int(11) COMMENT '审核类型(0：上架审核,1：下架审核,2:商品信息审核)'")
    private Integer checkType;//审核类型(0：上架审核,1：下架审核,2:商品信息审核)
    @Column(columnDefinition = "int(11) COMMENT '审核状态(0:待审核,1:审核通过,2:未通过)'")
    private Integer checkState;//审核状态(0:待审核,1:审核通过,2:未通过)
    @Column(columnDefinition = "varchar(255) COMMENT '审核说明'")
    private String instructions;//审核说明
    @Column(columnDefinition = "varchar(255) COMMENT '审核人'")
    private String userName;//审核人
    @Column(columnDefinition = "DATETIME COMMENT '审核时间'")
    private Date checkTime;//审核时间

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }
}
