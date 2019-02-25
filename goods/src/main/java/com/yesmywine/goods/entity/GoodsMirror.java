package com.yesmywine.goods.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.goods.bean.Item;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hz on 4/12/17.
 */
@Entity
@Table(name = "goodsMirror")
public class GoodsMirror extends BaseEntity<Integer>{      //商品镜像表(编辑审核用)


    private Integer goodsId;
    private Item item;
    private String goodsName;
    @Lob
    private String goodsImageUrl;
    @Ignore
    @Transient
    private String propString;//属性及属性值
    @OneToMany(fetch = FetchType.LAZY ,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "goodsPropId")
    private List<PropMirror> propMirrors = new ArrayList<>();
    private String goodsEnName;//英文名
    private String salePrice;
    private Integer operate;//销售类型
    private String price;
    private String goodsCode;
    private String channelName;
    private String channelCode;
    private Integer discount;
    private Integer library;//是否支持存酒库
    private Integer cashOnDelivery;//是否支持货到付款 0:是,1:不是
    private String virtualType;//虚拟商品类型
    private Date applyTime;
    private String applyUser;
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private Integer booknumber;//预售数量
    private Integer remainBooknumber;//预售剩余数量
    private Date delivery;//预计发货时间
    private Integer checkType;//审核类型(0：上架审核,1：下架审核,2:商品信息审核)
    private Integer checkState;//审核状态(0:待审核,1:审核通过,2:未通过)
    private String instructions;//审核说明
    private String userName;//审核人
    private Date checkTime;//审核时间
    private Boolean syncToStore;



    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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

    public String getGoodsEnName() {
        return goodsEnName;
    }

    public void setGoodsEnName(String goodsEnName) {
        this.goodsEnName = goodsEnName;
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

    public Boolean getSyncToStore() {
        return syncToStore;
    }

    public void setSyncToStore(Boolean syncToStore) {
        this.syncToStore = syncToStore;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImageUrl() {
        return goodsImageUrl;
    }

    public void setGoodsImageUrl(String goodsImageUrl) {
        this.goodsImageUrl = goodsImageUrl;
    }

    public String getPropString() {
        return propString;
    }

    public void setPropString(String propString) {
        this.propString = propString;
    }

    public List<PropMirror> getPropMirrors() {
        return propMirrors;
    }

    public void setPropMirrors(List<PropMirror> propMirrors) {
        this.propMirrors = propMirrors;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public Integer getCheckType() {
        return checkType;
    }

    public void setCheckType(Integer checkType) {
        this.checkType = checkType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getBooknumber() {
        return booknumber;
    }

    public void setBooknumber(Integer booknumber) {
        this.booknumber = booknumber;
    }

    public Date getDelivery() {
        return delivery;
    }

    public void setDelivery(Date delivery) {
        this.delivery = delivery;
    }

    public Integer getOperate() {
        return operate;
    }

    public void setOperate(Integer operate) {
        this.operate = operate;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getLibrary() {
        return library;
    }

    public void setLibrary(Integer library) {
        this.library = library;
    }

    public Integer getCashOnDelivery() {
        return cashOnDelivery;
    }

    public void setCashOnDelivery(Integer cashOnDelivery) {
        this.cashOnDelivery = cashOnDelivery;
    }

    public String getVirtualType() {
        return virtualType;
    }

    public void setVirtualType(String virtualType) {
        this.virtualType = virtualType;
    }

    public Integer getRemainBooknumber() {
        return remainBooknumber;
    }

    public void setRemainBooknumber(Integer remainBooknumber) {
        this.remainBooknumber = remainBooknumber;
    }
}
