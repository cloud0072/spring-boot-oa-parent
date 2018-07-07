package com.caolei.system.pojo;


import com.caolei.system.api.BaseEntity;
import com.caolei.system.constant.TableConstant;


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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String method;

    @Column
    private Date createTime;

    @Column
    private String requestUrl;

    @Column
    private String ipAddress;

    @Column
    private String sessionId;

    public OperationLog() {
    }

    public OperationLog(User user, String method, String requestUrl, String ipAddress, String sessionId) {
        this.user = user;
        this.method = method;
        this.requestUrl = requestUrl;
        this.ipAddress = ipAddress;
        this.sessionId = sessionId;
    }

    @Override
    public String tableName() {
        return TableConstant.OPERATION_LOG;
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
