package com.yesmywine.goods.entityProperties;

import com.yesmywine.goods.bean.CanSearch;
import com.yesmywine.goods.bean.EntryMode;
import com.yesmywine.goods.bean.IsSku;
import com.yesmywine.goods.bean.IsUse;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by WANG, RUIQING on 12/7/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "properties")
public class Properties {
    //商品属性表
    @Id
    protected Integer id;
    protected Date createTime;
    @Column(unique = true)
    private String cnName;
    @Enumerated(EnumType.ORDINAL)
    private IsSku isSku;
    @Enumerated(EnumType.ORDINAL)
    private CanSearch canSearch;
    @Enumerated(EnumType.STRING)
    private EntryMode entryMode;   //手动还是列表
    @Enumerated(EnumType.ORDINAL)
    private IsUse isUse;
    @Column(columnDefinition = "varchar(200) COMMENT '编码'")
    private String code;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Properties() {
        this.createTime = new Date();
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public IsSku getIsSku() {
        return isSku;
    }

    public void setIsSku(IsSku isSku) {
        this.isSku = isSku;
    }

    public CanSearch getCanSearch() {
        return canSearch;
    }

    public void setCanSearch(CanSearch canSearch) {
        this.canSearch = canSearch;
    }

    public EntryMode getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(EntryMode entryMode) {
        this.entryMode = entryMode;
    }

    public IsUse getIsUse() {
        return isUse;
    }

    public void setIsUse(IsUse isUse) {
        this.isUse = isUse;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
