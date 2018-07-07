package com.caolei.system.pojo;


import com.caolei.system.api.BaseEntity;
import com.caolei.system.constant.TableConstant;
import com.caolei.system.utils.EncryptUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 基础用户
 * @author cloud0072
 * @date 2018/6/12 22:37
 */
@Entity
@Table(name = "auth_user" ,uniqueConstraints = @UniqueConstraint(columnNames = {"account"}))
public class User extends BaseEntity {

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
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column
    private Date createTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column
    private Date lastLoginTime;

    @OneToOne
    private UserDetail userDetail;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "auth_user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "auth_role_id"))
    private List<Role> roles;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "auth_user_permission", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "auth_permission_id"))
    private List<Permission> permissions;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<OperationLog> logs;

    public User() {
    }

    public User(String account) {
        this.account = account;
    }

    public User(String account, String password, String userName, String email) {
        this();
        this.account = account;
        this.password = password;
        this.userName = userName;
        this.email = email;
    }

    public User setDefaultValue() {
        this.salt = this.salt != null ? salt : EncryptUtils.getInstance().getHash_salt();
        this.active = this.active == null ? true : this.active;
        this.superUser = this.superUser == null ? false : this.superUser;
        this.createTime = this.createTime == null ? new Date() : this.createTime;
        this.lastLoginTime = this.lastLoginTime == null ? new Date() : this.lastLoginTime;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getSuperUser() {
        return superUser;
    }

    public void setSuperUser(Boolean superUser) {
        this.superUser = superUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public List<Role> getRoles() {
        return roles == null ? new ArrayList() : roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Permission> getPermissions() {
        return permissions == null ? new ArrayList() : permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<OperationLog> getLogs() {
        return logs == null ? new ArrayList() : logs;
    }

    public void setLogs(List<OperationLog> logs) {
        this.logs = logs;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String tableName() {
        return TableConstant.USER;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
