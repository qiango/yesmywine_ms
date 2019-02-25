package com.yesmywine.goods.entity;

import javax.persistence.*;

/**
 * Created by light on 2017/4/5.
 */
@Entity
@Table(name = "imgFile")
public class ImgFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(255) COMMENT '地址'")
    private String url;
    @Column(columnDefinition = "varchar(255) COMMENT '临时名称'")
    private String fileName;
    @Column(columnDefinition = "varchar(255) COMMENT '原始名称'")
    private String originName;
    @Column(columnDefinition = "int(11) COMMENT '高'")
    private Integer height;
    @Column(columnDefinition = "int(11) COMMENT '宽'")
    private Integer width;


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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
