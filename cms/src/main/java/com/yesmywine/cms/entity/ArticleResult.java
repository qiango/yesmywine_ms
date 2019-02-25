package com.yesmywine.cms.entity;

import javax.persistence.*;

/**
 * Created by hz on 8/15/17.
 */
@Entity
@Table(name = "articleResult")
public class ArticleResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;//主键，自增
    private String title;
    private String columnsName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColumnsName() {
        return columnsName;
    }

    public void setColumnsName(String columnsName) {
        this.columnsName = columnsName;
    }
}
