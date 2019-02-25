package com.yesmywine.goods.entityProperties;

import com.yesmywine.goods.bean.BrandEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by WANG, RUIQING on 12/7/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String brandCnName;        //品牌中文名
    private String brandEnName;        //品牌英文名
    private String logoImage;
    @Enumerated(EnumType.ORDINAL)
    private BrandEnum brandEnum;    //状态是否可用
    private Date createdTime;
    private Integer createdUserId;

    public Brand() {
        this.brandEnum = BrandEnum.available;
    }

    public Brand(String brandCnName) {
        this.brandCnName = brandCnName;
        this.brandEnum = BrandEnum.available;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandCnName() {
        return brandCnName;
    }

    public void setBrandCnName(String brandCnName) {
        this.brandCnName = brandCnName;
    }

    public String getBrandEnName() {
        return brandEnName;
    }

    public void setBrandEnName(String brandEnName) {
        this.brandEnName = brandEnName;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public BrandEnum getBrandEnum() {
        return brandEnum;
    }

    public void setBrandEnum(BrandEnum brandEnum) {
        this.brandEnum = brandEnum;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }
}
