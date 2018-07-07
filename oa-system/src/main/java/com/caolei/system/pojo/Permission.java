package com.caolei.system.pojo;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.constant.Operation;
import com.caolei.system.constant.TableConstant;
import com.caolei.system.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

/**
 * 认证权限
 * UniqueConstraint(columnNames = {"operation","name"})是联合唯一约束,两个都相同才抛异常
 * UniqueConstraint(columnNames = {"operation"}),@UniqueConstraint(columnNames = {"name"})为两个唯一约束
 *
 * @author cloud0072
 * @date 2018/6/12 22:40
 */
@Entity
@Table(name = "auth_permission", uniqueConstraints = {@UniqueConstraint(columnNames = {"code"})})
public class Permission extends BaseEntity {

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

    public Permission(EntityResource entityResource, Operation operation, String resourceId) {
        this.entityResource = entityResource;
        this.operation = operation;
        this.resourceId = resourceId;
        this.code = code();
    }

    public Permission(EntityResource entityResource, Operation operation) {
        this(entityResource, operation, null);
    }

    public Permission() {
    }

    private String code() {
        if (operation != null && entityResource != null) {
            StringBuilder code = new StringBuilder();
            code.append(this.entityResource.getCode()).append(":")
            .append(operation == Operation.ALL ? "*" : this.operation.name());
            if (!StringUtils.isEmpty(resourceId)) {
                code.append(":").append(resourceId);
            }
            return code.toString();
        }
        return null;
    }

    @Override
    public String tableName() {
        return TableConstant.PERMISSION;
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

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public EntityResource getEntityResource() {
        return entityResource;
    }

    public void setEntityResource(EntityResource entityResource) {
        this.entityResource = entityResource;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
