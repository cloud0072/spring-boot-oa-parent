package com.caolei.system.pojo;


import com.caolei.system.api.BaseEntity;


import javax.persistence.*;
import java.util.Date;

/**
 * 系统管理日志
 *
 * @author cloud0072
 */
@Entity
@Table
public class OperationLog extends BaseEntity {
    /**
     * 操作用户
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * httpMethod
     */
    @Column
    private String method;
    /**
     * 请求URL
     */
    @Column
    private String requestUrl;
    /**
     * IP地址
     */
    @Column
    private String ipAddress;
    /**
     * SessionId
     */
    @Column
    private String sessionId;
    /**
     * 访问时间
     */
    @Column
    private Date createTime;

    public OperationLog() {
    }

    public OperationLog(User user, String method, String requestUrl, String ipAddress, String sessionId) {
        this.user = user;
        this.method = method;
        this.requestUrl = requestUrl;
        this.ipAddress = ipAddress;
        this.sessionId = sessionId;
        this.createTime = new Date();
    }

    @Override
    public String tableName() {
        return "系统管理日志";
    }

    @Override
    public String moduleName() {
        return "system";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}
