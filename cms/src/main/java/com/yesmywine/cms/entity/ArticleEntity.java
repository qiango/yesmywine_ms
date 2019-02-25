package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by liqingqing on 2017/1/4.
 */
@Entity
@Table(name = "article")
public class ArticleEntity extends BaseEntity<Integer>{

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "columnsName")
    private ColumnsEntity columnsEntity;
    @Column(columnDefinition = "varchar(200) COMMENT '标题'")
    private String title;
    @Column(columnDefinition = "varchar(200) COMMENT '摘要'")
    private String abstracts;
    @Lob
    @Column(columnDefinition = "longtext COMMENT '文章内容'")
    private String articleContent;//文章内容


    public ColumnsEntity getColumnsEntity() {
        return columnsEntity;
    }

    public void setColumnsEntity(ColumnsEntity columnsEntity) {
        this.columnsEntity = columnsEntity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }
}
