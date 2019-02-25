package com.yesmywine.evaluation.bean;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 咨询表
 * Created by light on 2017/1/4.
 */
@Entity
@Table(name = "orderGoodsAdvice")
public class OrderGoodsAdvice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "goods")
    private Goods goods;// 商品id
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;
    @Column(columnDefinition = "varchar(255) COMMENT '商品名称'")
    private String goodsName;
    @Column(columnDefinition = "int(11) COMMENT '用户id'")
    private Integer userId;
    @Column(columnDefinition = "varchar(50) COMMENT '用户名称'")
    private String userName;
    @Column(columnDefinition = "LONGTEXT COMMENT '用户图片'")
    private String userImage;
    @Column(columnDefinition = "LONGTEXT COMMENT '图片'")
    private String image;//图片
    @Column(columnDefinition = "varchar(255) COMMENT '咨询'")
    private String advice;//咨询
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "adviceType")
    private AdviceType adviceType;//咨询类型
    @Column(columnDefinition = "int(11) COMMENT '满意'")
    private Integer satisfaction;//满意
    @Column(columnDefinition = "int(11) COMMENT '不满意'")
    private Integer discontent;//不满意
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

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Integer getDiscontent() {
        return discontent;
    }

    public void setDiscontent(Integer discontent) {
        this.discontent = discontent;
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

    public AdviceType getAdviceType() {
        return adviceType;
    }

    public void setAdviceType(AdviceType adviceType) {
        this.adviceType = adviceType;
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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}
