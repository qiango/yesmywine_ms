package com.yesmywine.sso.bean;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by SJQ on 2017/5/25.
 */
@Entity
@Table(name = "roles")
public class Roles extends BaseEntity<Integer> {
    private String roleName;

    @Column(columnDefinition = "bit(1) COMMENT '是否拥有所有权限'")
    private Boolean haveAllPerms;
    @Column(columnDefinition = "bit(1) COMMENT '是否可删除'")
    private Boolean canDelete;

    @ManyToMany
    @JoinTable(name="rolePerm",
            joinColumns={@JoinColumn(name="rid", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="pid", referencedColumnName="id")})
    private Set<Perms> perms = new HashSet<>();

//    @ManyToMany
//    @JoinTable(name="userRole",
//            joinColumns={@JoinColumn(name="rid", referencedColumnName="id")},
//            inverseJoinColumns={@JoinColumn(name="uid", referencedColumnName="id")})
//    private Set<UserInformation> users = new HashSet<>();

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<Perms> getPerms() {
        return perms;
    }

    public void setPerms(Set<Perms> perms) {
        this.perms = perms;
    }


    public void addToPerms(Perms perm) {
        Set<Perms> permsSet = getPerms();
        if (permsSet == null) {
            permsSet = new HashSet<Perms>();
            setPerms(permsSet);
        }
        permsSet.add(perm);
    }

    public Boolean getHaveAllPerms() {
        return haveAllPerms;
    }

    public void setHaveAllPerms(Boolean haveAllPerms) {
        this.haveAllPerms = haveAllPerms;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }
}
