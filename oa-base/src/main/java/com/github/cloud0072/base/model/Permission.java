package com.github.cloud0072.base.model;

import com.github.cloud0072.base.model.extend.EntityResource;
import com.github.cloud0072.common.annotation.EntityInfo;
import com.github.cloud0072.common.module.BaseModuleEntity;
import com.github.cloud0072.common.constant.Operation;
import com.github.cloud0072.common.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * 认证权限
 * UniqueConstraint(columnNames = {"operation","name"})是联合唯一约束,两个都相同才抛异常
 * UniqueConstraint(columnNames = {"operation"}),@UniqueConstraint(columnNames = {"name"})为两个唯一约束
 *
 * @author cloud0072
 * @date 2018/6/12 22:40
 */
@EntityInfo(description="权限",entityName = "permission", entityPath = "/permission")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "auth_permission", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code"}),
        @UniqueConstraint(columnNames = {"name"})
})
public class Permission
        extends BaseEntity
        implements NamedEntity, SystemEntity, BaseModuleEntity {

    @Column
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @OneToOne
    private EntityResource entityResource;

    @Column
    private Operation operation;

    @Column(name = "resource_id")
    private String resourceId;
    /**
     * 是否是系统自带的实体
     */
    @Column
    private Boolean systemEntity;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "auth_user_permission", joinColumns = @JoinColumn(name = "auth_permission_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "auth_role_permission", joinColumns = @JoinColumn(name = "auth_permission_id"),
            inverseJoinColumns = @JoinColumn(name = "auth_role_id"))
    private List<Role> roles;

    public Permission(EntityResource entityResource, Operation operation, String resourceId, Boolean systemEntity) {
        this.entityResource = entityResource;
        this.operation = operation;
        this.resourceId = resourceId;
        this.code = code();
        this.name = name();
        this.systemEntity = systemEntity == null ? false : systemEntity;
    }

    public Permission() {
    }

    private String name() {
        return code;
    }

    private String code() {
        if (operation != null && entityResource != null) {
            StringBuilder code = new StringBuilder()
                    .append(this.entityResource.getCode()).append(":")
                    .append(operation.code());
            if (!StringUtils.isEmpty(resourceId)) {
                code.append(":").append(resourceId);
            } else {
                code.append(":*");
            }
            return code.toString();
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", entityResource=" + entityResource +
                ", operation=" + operation +
                ", resourceId='" + resourceId + '\'' +
                '}';
    }
}
