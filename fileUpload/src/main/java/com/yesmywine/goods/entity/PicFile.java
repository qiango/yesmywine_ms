package com.yesmywine.goods.entity;

import javax.persistence.*;

/**
 * Created by light on 2017/4/5.
 */
@Entity
@Table(name = "picFile")
public class PicFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(50) COMMENT '地址'")
    private String url;
    @Column(columnDefinition = "varchar(11) COMMENT '模块名称'")
    private String module;
    @Column(columnDefinition = "int(11) COMMENT '主id'")
    private Integer mId;
    @Column(columnDefinition = "varchar(11) COMMENT '临时名称'")
    private String tempFileName;
    @Column(columnDefinition = "varchar(11) COMMENT '最终文件名称'")
    private String formalFileName;
    @Column(columnDefinition = "varchar(11) COMMENT '原始名称'")
    private String originName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public String getTempFileName() {
        return tempFileName;
    }

    public void setTempFileName(String tempFileName) {
        this.tempFileName = tempFileName;
    }

    public String getFormalFileName() {
        return formalFileName;
    }

    public void setFormalFileName(String formalFileName) {
        this.formalFileName = formalFileName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }
}
