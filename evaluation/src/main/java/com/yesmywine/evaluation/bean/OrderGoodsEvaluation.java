package com.yesmywine.evaluation.bean;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by wangdiandian on 2016/12/19.
 */
@Entity
@Table(name = "orderGoodsEvaluation")
public class OrderGoodsEvaluation {//订单商品评价
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "Bigint(20) COMMENT '订单编码'")
    private Long orderNo;//订单编码
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "goods")
    private Goods goods;// 商品
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;
    @Column(columnDefinition = "varchar(255) COMMENT '商品名称'")
    private String goodsName;
    @Column(columnDefinition = "int(11) COMMENT '用户id'")
    private Integer userId;
    @Column(columnDefinition = "varchar(50) COMMENT '用户名称'")
    private String userName;
    @Column(columnDefinition = "varchar(50) COMMENT '用户真正名称'")
    private String userRealName;
    @Column(columnDefinition = "LONGTEXT COMMENT '用户图片'")
    private String userImage;
    @Column(columnDefinition = "int(11) COMMENT '商品得星'")
    private Integer goodScores;//商品得星
    @Column(columnDefinition = "int(11) COMMENT '评价类型'")
    private Integer appType;//评价类型（0：好评，1：中评，2：差评，3：有图）
    @Column(columnDefinition = "LONGTEXT COMMENT '图片'")
    private String image;//图片
    @Column(columnDefinition = "varchar(255) COMMENT '买家评价'")
    private String evaluation;//买家评价
    @Column(columnDefinition = "int(11) COMMENT '有用'")
    private Integer useful;//有用
    @Column(columnDefinition = "int(11) COMMENT '无用'")
    private Integer useless;//无用
    @Column(columnDefinition = "int(11) COMMENT '审核标识'")
    private Integer status;//审核标识
    @OneToMany(cascade = CascadeType.REFRESH)
    private List<Reply> reply;//回复
    @Column(columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;//创建时间


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Integer getGoodScores() {
        return goodScores;
    }

    public void setGoodScores(Integer goodScores) {
        this.goodScores = goodScores;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public Integer getUseful() {
        return useful;
    }

    public void setUseful(Integer useful) {
        this.useful = useful;
    }

    public Integer getUseless() {
        return useless;
    }

    public void setUseless(Integer useless) {
        this.useless = useless;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public List<Reply> getReply() {
        return reply;
    }

    public void setReply(List<Reply> reply) {
        this.reply = reply;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }
}
