package com.caolei.system.pojo;

import com.caolei.system.api.NamedEntity;
import com.caolei.system.api.SystemEntity;
import com.caolei.system.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Entity
@Table(name = "auth_user", uniqueConstraints = @UniqueConstraint(columnNames = {"account"}))
public class User extends SystemEntity implements NamedEntity {

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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private Date lastLoginTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private Date birthday;

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
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Set<OperationLog> logs;

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
        return this;
    }

    @Override
    protected String getTableName() {
        return "基础用户";
    }

    @Override
    protected String getModuleName(){
        return "system";
    }

    @Override
    public String getName() {
        //namedEntity
        return getUserName();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Boolean isActive() {
        return active == null ? false : active;
    }

    public Boolean getSuperUser() {
        return superUser;
    }

    public void setSuperUser(Boolean superUser) {
        this.superUser = superUser;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Permission> getPermissions() {
        if (permissions == null) {
            permissions = new HashSet<>();
        }
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<OperationLog> getLogs() {
        if (logs == null) {
            logs = new HashSet<>();
        }
        return logs;
    }

    public void setLogs(Set<OperationLog> logs) {
        this.logs = logs;
    }

}
