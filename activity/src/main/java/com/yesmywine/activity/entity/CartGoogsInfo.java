package com.yesmywine.activity.entity;

import com.alibaba.fastjson.JSONArray;

import javax.persistence.Column;
import java.util.Set;

/**
 * Created by by on 2017/6/22.
 */
public class CartGoogsInfo {
    @Column(columnDefinition = "varchar(10) COMMENT '商品id'")
    private String goodsId;
    private String goodsCode;
    @Column(columnDefinition = "int(10) COMMENT '分类id'")
    private Integer categoryId;
    @Column(columnDefinition = "int(10) COMMENT '品牌id'")
    private Integer brandId;
    private Set<GoodsSku> skuInfo;
    @Column(columnDefinition = "varchar(255) COMMENT '商品名称'")
    private String goodsName;
    @Column(columnDefinition = "longtext COMMENT '商品图片'")
    private String goodsImg;
    @Column(columnDefinition = "int(10) COMMENT '商品数量'")
    private Integer goodsCount;
    @Column(columnDefinition = "BIT(1) COMMENT '是否支持存酒库'")
    private Boolean isKeep;
    @Column(columnDefinition = "BIT(1) COMMENT '是否选择'")
    private Boolean isChoose;
    @Column(columnDefinition = "double(20) COMMENT '原价'")
    private Double originPrice;
    @Column(columnDefinition = "double(20) COMMENT '现价'")
    private Double nowPrice;
    private Double subTotal;//商品原价*数量
    private Double subTradeTotal;//商品原价+换购价
    private Double memberRivilege;//原价与折扣价的差价
    private Object activity;
    private Integer activityId;
    private Integer regulationId;
    private JSONArray otherActivity;
    private CartGoogsInfo chlidGoodsInfo;//换购商品
    private String saleModel;//销售模式
    private String goodsType;//商品类型 single-单品 plural-组合商品 luckBage-福袋
    private Double activityPrice;//活动价
    private Double balance;//差价
    private Object regulationInfo;
    private Object goStatus;
    private Object virtualType;
    private Double memberAmmount;


    public CartGoogsInfo(String goodsId, String goodsCode,String goodsName, String goodsImg,
                         Integer goodsCount, Boolean isKeep,
                         Boolean isChoose, Double originPrice,
                         Double nowPrice, Double subTotal,
                         Double memberRivilege, Object activity,
                         JSONArray otherActivity,
                         Integer activityId,Integer regulationId,Integer categoryId,
                         Integer brandId,Set<GoodsSku> skuInfo,String saleModel,
                         String goodsType,Double activityPrice,Object goStatus,Object virtualType,
                         Double memberAmmount
                            ) {
        this.goodsId = goodsId;
        this.goodsCode = goodsCode;
        this.goodsName = goodsName;
        this.goodsImg = goodsImg;
        this.goodsCount = goodsCount;
        this.isKeep = isKeep;
        this.isChoose = isChoose;
        this.originPrice = originPrice;
        this.nowPrice = nowPrice;
        this.subTotal = subTotal;
        this.memberRivilege = memberRivilege;
        this.activity = activity;
        this.otherActivity = otherActivity;
        this.activityId = activityId;
        this.regulationId = regulationId;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.skuInfo = skuInfo;
        this.saleModel = saleModel;
        this.goodsType = goodsType;
        this.activityPrice = activityPrice;
        this.goStatus = goStatus;
        this.virtualType = virtualType;
        this.memberAmmount = memberAmmount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Object getRegulationInfo() {
        return regulationInfo;
    }

    public void setRegulationInfo(Object regulationInfo) {
        this.regulationInfo = regulationInfo;
    }

    public Double getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(Double activityPrice) {
        this.activityPrice = activityPrice;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getSaleModel() {
        return saleModel;
    }

    public void setSaleModel(String saleModel) {
        this.saleModel = saleModel;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Boolean getKeep() {
        return isKeep;
    }

    public void setKeep(Boolean keep) {
        isKeep = keep;
    }

    public Boolean getChoose() {
        return isChoose;
    }

    public void setChoose(Boolean choose) {
        isChoose = choose;
    }

    public Double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Double originPrice) {
        this.originPrice = originPrice;
    }

    public Double getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(Double nowPrice) {
        this.nowPrice = nowPrice;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Double getMemberRivilege() {
        return memberRivilege;
    }

    public void setMemberRivilege(Double memberRivilege) {
        this.memberRivilege = memberRivilege;
    }

    public JSONArray getOtherActivity() {
        return otherActivity;
    }

    public void setOtherActivity(JSONArray otherActivity) {
        this.otherActivity = otherActivity;
    }

    public Object getActivity() {
        return activity;
    }

    public void setActivity(Object activity) {
        this.activity = activity;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getRegulationId() {
        return regulationId;
    }

    public void setRegulationId(Integer regulationId) {
        this.regulationId = regulationId;
    }

    public CartGoogsInfo getChlidGoodsInfo() {
        return chlidGoodsInfo;
    }

    public void setChlidGoodsInfo(CartGoogsInfo chlidGoodsInfo) {
        this.chlidGoodsInfo = chlidGoodsInfo;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Set<GoodsSku> getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(Set<GoodsSku> skuInfo) {
        this.skuInfo = skuInfo;
    }

    public Object getGoStatus() {
        return goStatus;
    }

    public void setGoStatus(Object goStatus) {
        this.goStatus = goStatus;
    }

    public Object getVirtualType() {
        return virtualType;
    }

    public void setVirtualType(Object virtualType) {
        this.virtualType = virtualType;
    }

    public Double getSubTradeTotal() {
        return subTradeTotal;
    }

    public void setSubTradeTotal(Double subTradeTotal) {
        this.subTradeTotal = subTradeTotal;
    }

    public Double getMemberAmmount() {
        return memberAmmount;
    }

    public void setMemberAmmount(Double memberAmmount) {
        this.memberAmmount = memberAmmount;
    }
}
