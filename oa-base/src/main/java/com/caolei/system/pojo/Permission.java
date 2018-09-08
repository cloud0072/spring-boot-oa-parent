package com.caolei.system.pojo;

import com.caolei.common.annotation.EntityInfo;
import com.caolei.common.api.entity.NamedEntity;
import com.caolei.common.api.entity.SystemEntity;
import com.caolei.common.api.module.BaseModuleEntity;
import com.caolei.common.constant.Operation;
import com.caolei.common.util.StringUtils;
import com.caolei.system.extend.EntityResource;
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
@EntityInfo(entityName = "权限", entityPath = "/permission")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "auth_permission", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code"}),
        @UniqueConstraint(columnNames = {"name"})
})
public class Permission extends SystemEntity implements NamedEntity, BaseModuleEntity {

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
    protected String getTableName() {
        return "认证权限";
    }

    @Override
    protected String getmodulePath() {
        return "system";
    }
}
