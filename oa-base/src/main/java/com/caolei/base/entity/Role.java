package com.caolei.base.entity;


import com.caolei.common.annotation.EntityInfo;
import com.caolei.common.module.BaseModuleEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;


/**
 * 角色
 *
 * @author cloud0072
 * @date 2018/6/12 22:37
 */
@EntityInfo(description="角色",entityName = "role", entityPath = "/role")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "auth_role", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}),
        @UniqueConstraint(columnNames = {"code"})
})
public class Role
        extends BaseEntity
        implements NamedEntity, SystemEntity, BaseModuleEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Column
    private String description;
    /**
     * 是否是系统自带的实体
     */
    @Column
    private Boolean systemEntity;

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

    public Role(String name, String code, String description, Boolean systemEntity) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.systemEntity = systemEntity == null ? false : systemEntity;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
