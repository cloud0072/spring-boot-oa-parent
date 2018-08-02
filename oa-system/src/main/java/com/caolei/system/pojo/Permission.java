package com.caolei.system.pojo;

import com.caolei.system.util.NamedEntity;
import com.caolei.system.api.SystemEntity;
import com.caolei.system.constant.Operation;
import com.caolei.system.model.EntityResource;
import com.caolei.system.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Entity
@Table(name = "auth_permission", uniqueConstraints = {@UniqueConstraint(columnNames = {"code"})})
public class Permission extends SystemEntity implements NamedEntity {

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

    /**
     * FIXME 以后改为中文名
     *
     * @return
     */
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
    protected String getTableName(){
        return "认证权限";
    }

    @Override
    protected String getModuleName(){
        return "system";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
