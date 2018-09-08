package com.caolei.system.pojo;


import com.caolei.common.annotation.EntityInfo;
import com.caolei.common.api.entity.BaseEntity;
import com.caolei.common.api.module.BaseModuleEntity;
import com.caolei.common.util.HttpUtils;
import com.caolei.system.util.UserUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 系统管理日志
 *
 * @author cloud0072
 */
@EntityInfo(entityName = "系统访问日志", entityPath = "/operationLog")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table
public class OperationLog extends BaseEntity implements BaseModuleEntity {
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
    private Date createTime;

    public OperationLog() {
    }

    public OperationLog(HttpServletRequest request) {
        this.user = UserUtils.getCurrentUser();
        this.method = request.getMethod();
        this.requestUrl = request.getRequestURL().toString();
        this.ipAddress = HttpUtils.IPAddress(request);
        this.sessionId = getSessionId();
        this.createTime = new Date();
    }

    @Override
    protected String getTableName() {
        return "系统管理日志";
    }

    @Override
    protected String getmodulePath() {
        return "system";
    }

}
