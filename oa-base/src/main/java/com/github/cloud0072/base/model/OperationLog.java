package com.github.cloud0072.base.model;


import com.github.cloud0072.base.util.UserUtils;
import com.github.cloud0072.common.annotation.EntityInfo;
import com.github.cloud0072.common.module.BaseModuleEntity;
import com.github.cloud0072.common.util.HttpUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 系统管理日志
 *
 * @author cloud0072
 */
@EntityInfo(description="系统访问日志",entityName = "operationLog", entityPath = "/operationLog")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table
public class OperationLog
        extends BaseEntity
        implements BaseModuleEntity {
    /**
     * 操作用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
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
    private LocalDateTime createTime;

    public OperationLog() {
    }

    public OperationLog(HttpServletRequest request) {
        this.user = UserUtils.getCurrentUser();
//        if (user == null){
//            user = new User();
//        }
        this.method = request.getMethod();
        this.requestUrl = request.getRequestURL().toString();
        this.ipAddress = HttpUtils.IPAddress(request);
        this.sessionId = getSessionId();
        this.createTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "OperationLog{" +
                (user == null ? "" : "user=" + user.getUserName()) +
                ", method='" + method + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
