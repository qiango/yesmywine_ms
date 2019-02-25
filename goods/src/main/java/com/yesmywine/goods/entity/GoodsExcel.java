package com.yesmywine.goods.entity;

import javax.persistence.*;

/**
 * Created by hz on 12/12/16.
 */
@Entity
@Table(name = "goodsexcel")
public class GoodsExcel {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String name;
    private String className;
    private String keyName;
    private String keyType;
    private Integer startRow;
    private Integer startCol;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getStartCol() {
        return startCol;
    }

    public void setStartCol(Integer startCol) {
        this.startCol = startCol;
    }
}
