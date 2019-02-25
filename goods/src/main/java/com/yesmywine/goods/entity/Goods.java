package com.yesmywine.goods.entity;

import com.alibaba.fastjson.JSONArray;
import com.yesmywine.goods.bean.Item;
import com.yesmywine.goods.entityProperties.BaseEntity;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by WANG, RUIQING on 12/7/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "goods")
public class Goods extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Lob
    private String goodsImageUrl;  //商品图片url

    @Column(columnDefinition = "varchar(255) COMMENT '商品名称'")
    private String goodsName;//商品名称
    @Column(columnDefinition = "varchar(50) COMMENT '商品原始名称'")
    private String goodsOriginalName;//商品原始名称
    @Column(columnDefinition = "varchar(255) COMMENT '商品英文名'")
    private String goodsEnName;//商品英文名
    @Enumerated(EnumType.STRING)
    private Item item;//单品or多品/福袋
    @Column(columnDefinition = "varchar(50) COMMENT '商品编码'")
    private String goodsCode;//商品编码
    @Column(columnDefinition = "varchar(50) COMMENT '原始价格'")
    private String price;//原始价格
    @Column(columnDefinition = "varchar(50) COMMENT '销售价格'")
    private String salePrice;//销售价格
    @Column(columnDefinition = "varchar(50) COMMENT '参考价'")
    private String referencePrice;//参考价
    @Column(columnDefinition = "int(11) COMMENT '销量'")
    private Integer sales;//销量
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "goodsSkuId")
    private List<GoodsSku> goodsSku = new ArrayList<>();
    @Column(columnDefinition = "int(11) COMMENT '分类id'")
    private Integer categoryId;//分类id
    @Ignore
    @Transient
    private String categoryName;//分类名称
    @Column(columnDefinition = "varchar(50) COMMENT '分类完整id组合，用于查询'")
    private String categoryGroup;//分类完整id组合，用于查询

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "goodsPropId")
    private List<GoodsProp> goodsProp = new ArrayList<>();
    @Column(columnDefinition = "int(11) COMMENT '渠道id'")
    private  Integer channelId;
    @Column(columnDefinition = "varchar(50) COMMENT '渠道编码'")
    private String  channelCode;
    @Column(columnDefinition = "varchar(50) COMMENT '渠道名称'")
    private String channelName;
    @Column(columnDefinition = "int(11) COMMENT '销售模式１为预售，０为普通商品'")
    private Integer operate;//销售模式１为预售，０为普通商品,2抢购商品
    @Column(columnDefinition = "int(11) COMMENT '0正常、1上架审核中、2上架审核通过、3上架审核失败、4编辑审核中、5编辑审核通过、6编辑审核失败、7下架审核中、8下架审核通过、9下架审核失败'")
    private Integer status;  //0正常、1上架审核中、2上架审核通过、3上架审核失败、4编辑审核中、5编辑审核通过、6编辑审核失败、7下架审核中、8下架审核通过、9下架审核失败
    @Column(columnDefinition = "DATETIME COMMENT '开始时间'")
    private Date startTime;//开始时间
    @Column(columnDefinition = "DATETIME COMMENT '结束时间'")
    private Date endTime;//结束时间
    @Column(columnDefinition = "int(11) COMMENT '预售数量'")
    private Integer booknumber;//预售数量
    @Column(columnDefinition = "int(11) COMMENT '预售剩余数量'")
    private Integer  remainBooknumber;//预售剩余数量
    @Column(columnDefinition = "DATETIME COMMENT '预计发货时间'")
    private Date delivery;//预计发货时间
    @Column(columnDefinition = "int(11) COMMENT '(上架状态)0未上架,1已上架,2已下架'")
    private Integer goStatus;//(上架状态)0未上架,1已上架,2已下架
    @Column(columnDefinition = "DATETIME COMMENT '上架时间'")
    private Date listedTime;//上架时间
    @Column(columnDefinition = "int(11) COMMENT '评论数'")
    private Integer comments;//评论数
    @Column(columnDefinition = "double COMMENT '好评率'")
    private Double praise;//好评率
    @Column(columnDefinition = "int(2) COMMENT '是否打折 0:是，,1不打折'")
    private Integer discount;//是否打折 0:是，,1不打折
    @Column(columnDefinition = "int(2) COMMENT '是否支持存酒库 0:是'")
    private Integer library;//是否支持存酒库 0:是
    @Column(columnDefinition = "int(2) COMMENT '是否支持货到付款 0:是,1:'")
    private Integer cashOnDelivery;//是否支持货到付款 0:是,1:
    @Column(columnDefinition = "int(11) COMMENT '福袋的随机抽取数量'")// 不是
    private Integer randomNumber;//福袋的随机抽取数量
    @Column(columnDefinition = "int(11) COMMENT '同步用，存的pass层的goodsId'")
    private Integer passGoodsId;//同步用，存的pass层的goodsId
    @Column(columnDefinition = "int(11) COMMENT '预售状态  1-预售完成'")
    private Integer preStatus;
    @Column(columnDefinition = "varchar(20) COMMENT '虚拟商品类型,giftCard：礼品卡类型'")
    private String virtualType;
    @Column(columnDefinition = "bit(1) COMMENT '是否同步给门店'")
    private Boolean syncToStore;

    @Ignore
    @Transient
    private JSONArray jsonArraySku;
    @Ignore
    @Transient
    private JSONArray jsonArrayProp;


    public Integer getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(Integer preStatus) {
        this.preStatus = preStatus;
    }

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsImageUrl() {
        return goodsImageUrl;
    }

    public void setGoodsImageUrl(String goodsImageUrl) {
        this.goodsImageUrl = goodsImageUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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

    public Integer getPassGoodsId() {
        return passGoodsId;
    }

    public void setPassGoodsId(Integer passGoodsId) {
        this.passGoodsId = passGoodsId;
    }

    public String getGoodsEnName() {
        return goodsEnName;
    }

    public void setGoodsEnName(String goodsEnName) {
        this.goodsEnName = goodsEnName;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(String referencePrice) {
        this.referencePrice = referencePrice;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getOperate() {
        return operate;
    }

    public void setOperate(Integer operate) {
        this.operate = operate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getVirtualType() {
        return virtualType;
    }

    public void setVirtualType(String virtualType) {
        this.virtualType = virtualType;
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

    public Integer getGoStatus() {
        return goStatus;
    }

    public void setGoStatus(Integer goStatus) {
        this.goStatus = goStatus;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getRandomNumber() {
        return randomNumber;
    }

    public void setRandomNumber(Integer randomNumber) {
        this.randomNumber = randomNumber;
    }

    public String getGoodsOriginalName() {
        return goodsOriginalName;
    }

    public void setGoodsOriginalName(String goodsOriginalName) {
        this.goodsOriginalName = goodsOriginalName;
    }

    public List<GoodsSku> getGoodsSku() {
        return goodsSku;
    }

    public void setGoodsSku(List<GoodsSku> goodsSku) {
        this.goodsSku = goodsSku;
    }

    public List<GoodsProp> getGoodsProp() {
        return goodsProp;
    }

    public void setGoodsProp(List<GoodsProp> goodsProp) {
        this.goodsProp = goodsProp;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryGroup() {
        return categoryGroup;
    }

    public void setCategoryGroup(String categoryGroup) {
        this.categoryGroup = categoryGroup;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Date getListedTime() {
        return listedTime;
    }

    public void setListedTime(Date listedTime) {
        this.listedTime = listedTime;
    }

    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        this.comments = comments;
    }

    public Double getPraise() {
        return praise;
    }

    public void setPraise(Double praise) {
        this.praise = praise;
    }

    public Integer getLibrary() {
        return library;
    }

    public void setLibrary(Integer library) {
        this.library = library;
    }

    public Boolean getSyncToStore() {
        return syncToStore;
    }

    public void setSyncToStore(Boolean syncToStore) {
        this.syncToStore = syncToStore;
    }

    public JSONArray getJsonArraySku() {
        return jsonArraySku;
    }

    public void setJsonArraySku(JSONArray jsonArraySku) {
        this.jsonArraySku = jsonArraySku;
    }

    public JSONArray getJsonArrayProp() {
        return jsonArrayProp;
    }

    public void setJsonArrayProp(JSONArray jsonArrayProp) {
        this.jsonArrayProp = jsonArrayProp;
    }

    public Integer getCashOnDelivery() {
        return cashOnDelivery;
    }

    public void setCashOnDelivery(Integer cashOnDelivery) {
        this.cashOnDelivery = cashOnDelivery;
    }

    public Integer getRemainBooknumber() {
        return remainBooknumber;
    }

    public void setRemainBooknumber(Integer remainBooknumber) {
        this.remainBooknumber = remainBooknumber;
    }
}
