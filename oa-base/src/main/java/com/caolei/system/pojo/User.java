package com.caolei.system.pojo;

import com.caolei.common.annotation.EntityInfo;
import com.caolei.common.api.entity.NamedEntity;
import com.caolei.common.api.entity.SystemEntity;
import com.caolei.common.api.module.BaseModuleEntity;
import com.caolei.common.util.StringUtils;
import com.caolei.system.extend.UserExtend;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 基础用户
 *
 * @author cloud0072
 * @date 2018/6/12 22:37
 */
@EntityInfo(entityName = "基础用户", entityPath = "/user")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "auth_user", uniqueConstraints = @UniqueConstraint(columnNames = {"account"}))
public class User extends SystemEntity implements NamedEntity, BaseModuleEntity {

    @Column(nullable = false, unique = true)
    private String account;
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
     * 祖册时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Date createTime;
    /**
     * 最后登录时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Date lastLoginTime;
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
        this.systemEntity = systemEntity == null ? false : this.systemEntity;
    }

    public User setDefaultValue() {
        this.salt = this.salt == null ? StringUtils.UUID32() : salt;
        this.active = this.active == null ? true : this.active;
        this.superUser = this.superUser == null ? false : this.superUser;
        this.systemEntity = this.systemEntity == null ? false : this.systemEntity;
        this.createTime = this.createTime == null ? new Date() : this.createTime;
        this.lastLoginTime = this.lastLoginTime == null ? new Date() : this.lastLoginTime;
        this.extend = new UserExtend();
        this.roles = new HashSet<>();
        this.permissions = new HashSet<>();
        return this;
    }

    @Override
    public String getName() {
        //namedEntity
        return getUserName();
    }

    /**
     * 使用 is+名称方式获取 Boolean 的值 并判断非空
     * 好处 可以使用 Example 查询 而不必被默认的 boolean 默认为 false 所困扰
     * 同理 使用枚举类 取代 int类型数据 防止默认值污染实体
     *
     * @return
     */
    public Boolean isSuperUser() {
        return superUser == null ? false : superUser;
    }

    public Boolean isActive() {
        return active == null ? false : active;
    }

}
