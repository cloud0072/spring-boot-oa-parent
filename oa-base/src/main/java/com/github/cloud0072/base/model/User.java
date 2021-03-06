package com.github.cloud0072.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.cloud0072.base.model.extend.UserExtend;
import com.github.cloud0072.common.annotation.EntityInfo;
import com.github.cloud0072.common.module.BaseModuleEntity;
import com.github.cloud0072.common.util.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 基础用户
 *
 * @author cloud0072
 * @date 2018/6/12 22:37
 */
@EntityInfo(description = "基础用户", entityName = "user", entityPath = "/user")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "auth_user", uniqueConstraints = @UniqueConstraint(columnNames = {"account"}))
public class User
        extends BaseEntity
        implements NamedEntity, SystemEntity, BaseModuleEntity {

    @Column(nullable = false, unique = true)
    private String account;
    @JsonIgnore
    @Column
    private String password;
    @Column
    private String userName;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private String salt;
    @Column
    private Boolean active;
    @Column
    private Boolean superUser;
    /**
     * 是否是系统自带的实体
     */
    @Column
    private Boolean systemEntity;
    /**
     * 祖册时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private LocalDateTime createTime;
    /**
     * 最后登录时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private LocalDateTime lastLoginTime;
    /**
     * 用户详情
     */
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "extend_id")
    private UserExtend extend;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "auth_user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "auth_role_id"))
    private Set<Role> roles;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "auth_user_permission", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "auth_permission_id"))
    private Set<Permission> permissions;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<OperationLog> operationLogs;

    public User() {
    }

    public User(String account) {
        this.setDefaultValue();
        this.account = account;
    }

    public User(String account, String password, String userName, String email, Boolean systemEntity) {
        this.setDefaultValue();
        this.account = account;
        this.password = password;
        this.userName = userName;
        this.email = email;
        this.systemEntity = systemEntity == null ? Boolean.FALSE : this.systemEntity;
    }

    public User setDefaultValue() {
        this.salt = this.salt == null ? StringUtils.UUID32() : salt;
        this.active = this.active == null ? Boolean.TRUE : this.active;
        this.superUser = this.superUser == null ? Boolean.FALSE : this.superUser;
        this.systemEntity = this.systemEntity == null ? Boolean.FALSE : this.systemEntity;
        this.createTime = this.createTime == null ? LocalDateTime.now() : this.createTime;
        this.lastLoginTime = this.lastLoginTime == null ? LocalDateTime.now() : this.lastLoginTime;
        this.extend = this.extend == null ? new UserExtend() : this.extend;
        this.roles = this.roles == null ? new HashSet<>() : this.roles;
        this.permissions = this.permissions == null ? new HashSet<>() : this.permissions;
        return this;
    }

    /**
     * 使用 is+名称方式获取 Boolean 的值 并判断非空
     * 好处 可以使用 Example 查询 而不必被默认的 boolean 默认为 false 所困扰
     * 同理 使用枚举类 取代 int类型数据 防止默认值污染实体
     *
     * @return
     */
    @Transient
    public Boolean isSuperUser() {
        return superUser == null ? false : superUser;
    }
    @Transient
    public Boolean isActive() {
        return active == null ? false : active;
    }

    @Override
    public String getName() {
        //namedEntity
        return getUserName();
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", salt='" + salt + '\'' +
                ", active=" + active +
                ", superUser=" + superUser +
                ", createTime=" + createTime +
                ", lastLoginTime=" + lastLoginTime +
                '}';
    }


}
