package com.yesmywine.sso.bean;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by SJQ on 2017/5/25.
 */
@Entity
@Table(name = "perms")
public class Perms extends BaseEntity<Integer>{
    @Column(columnDefinition = "varchar(255) COMMENT '权限标识'")
    private String permKey;
    @Column(columnDefinition = "varchar(50) COMMENT '权限名称'")
    private String permName;
    @Column(columnDefinition = "int(11) COMMENT '该权限属于哪个菜单'")
    private Integer parentId;
    @Column(columnDefinition = "int(2) COMMENT '菜单级别  1  2  3'")
    private Integer level;
    @Column(columnDefinition = "varchar(8) COMMENT '是否是菜单，true-是  false-否'")
    private String isMenu;
    @Column(columnDefinition = "bit(1) COMMENT '是否可删除'")
    private Boolean canDelete;

    public String getPermKey() {
        return permKey;
    }

    public void setPermKey(String permKey) {
        this.permKey = permKey;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(String isMenu) {
        this.isMenu = isMenu;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }
}
