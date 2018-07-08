package com.caolei.system.pojo;


import com.caolei.system.api.BaseEntity;
import com.caolei.system.api.NamedEntity;
import com.caolei.system.constant.TableConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 权限分组
 *
 * @author cloud0072
 * @date 2018/6/12 22:37
 */
@Entity
@Table(name = "auth_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Role extends BaseEntity implements NamedEntity {

    @Column
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Column
    private String description;

    @Column
    private Boolean systemRole;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "auth_user_role", joinColumns = @JoinColumn(name = "auth_role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(name = "auth_role_permission", joinColumns = @JoinColumn(name = "auth_role_id"),
            inverseJoinColumns = @JoinColumn(name = "auth_permission_id"))
    private List<Permission> permissions;

    public Role() {
    }

    public Role(String name, String code, String description, Boolean systemRole) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.systemRole = systemRole;
    }

    @Override
    public String tableName() {
        return TableConstant.ROLE;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getSystemRole() {
        return systemRole;
    }

    public void setSystemRole(Boolean systemRole) {
        this.systemRole = systemRole;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users == null ? new ArrayList() : users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Permission> getPermissions() {
        return permissions == null ? new ArrayList<>() : permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

}
