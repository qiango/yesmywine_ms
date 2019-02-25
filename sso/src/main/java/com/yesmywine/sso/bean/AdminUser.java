package com.yesmywine.sso.bean;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户基本信息
 * Created by Administrator on 2016/12/7.
 */
@Entity
@Table(name = "adminUser")
public class AdminUser extends BaseEntity<Integer> {
    @Column(unique = true,columnDefinition = "varchar(50) COMMENT '用户名'")
    private String userName;
    @Column(columnDefinition = "varchar(50) COMMENT '密码'")
    private String password;
    @Column(unique = true,columnDefinition = "varchar(50) COMMENT '手机号'")
    private String phoneNumber;
    @Column(columnDefinition = "varchar(100) COMMENT '邮箱'")
    private String email;
    @Column(columnDefinition = "varchar(50) COMMENT '昵称'")
    private String nickName;

    @ManyToMany
    @JoinTable(name="userRole",
            joinColumns={@JoinColumn(name="uid", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="rid", referencedColumnName="id")})
    private Set<Roles> roles = new HashSet<>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }


    public void addToRoles(Roles role) {
        Set<Roles> rolesSet = getRoles();
        if (rolesSet == null) {
            rolesSet = new HashSet<Roles>();
            setRoles(rolesSet);
        }
        rolesSet.add(role);
    }
}
