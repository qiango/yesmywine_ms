package com.yesmywine.ware.entity;




import javax.persistence.*;

/**
 * Created by SJQ on 2017/1/5.
 *
 * @Description:库存渠道字典表
 */
@Entity
@Table(name = "channels")
public class Channels extends BaseEntity {

    @Id
    private Integer id;
    private String channelName;
    private String channelCode; //渠道编码
    private Integer type; //类别 0-实渠道  1-门店分公司渠道   2-客服系统渠道   3-通用渠道
    private String comment; //备注
    private Boolean ifSale; //是否用于销售
    private Boolean ifInventory;    //是否用于库存
    private Boolean ifProcurement;  //是否用于采购
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "parentChannelId")
    private Channels parentChannel;//上级渠道
    private Boolean canDelete;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIfSale() {
        return ifSale;
    }

    public void setIfSale(Boolean ifSale) {
        this.ifSale = ifSale;
    }

    public Boolean getIfInventory() {
        return ifInventory;
    }

    public void setIfInventory(Boolean ifInventory) {
        this.ifInventory = ifInventory;
    }

    public Boolean getIfProcurement() {
        return ifProcurement;
    }

    public void setIfProcurement(Boolean ifProcurement) {
        this.ifProcurement = ifProcurement;
    }

    public Channels getParentChannel() {
        return parentChannel;
    }

    public void setParentChannel(Channels parentChannel) {
        this.parentChannel = parentChannel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }
}
