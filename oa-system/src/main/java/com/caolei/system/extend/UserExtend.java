package com.caolei.system.extend;

import com.caolei.common.api.BaseEntity;
import com.caolei.system.pojo.FileComponent;
import com.caolei.system.pojo.OperationLog;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 用户详情
 */
@Entity
@Table
public class UserExtend extends BaseEntity {

    /**
     * 头像
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "headPhoto_id")
    private FileComponent headPhoto;
    /**
     * 生日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column
    private Date birthday;
    /**
     * 操作日志
     */
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Set<OperationLog> logs;

    @Override
    protected String getTableName() {
        return "用户详情";
    }

    @Override
    protected String getModuleName() {
        return "system";
    }


    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public FileComponent getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(FileComponent headPhoto) {
        this.headPhoto = headPhoto;
    }

    public Set<OperationLog> getLogs() {
        return logs;
    }

    public void setLogs(Set<OperationLog> logs) {
        this.logs = logs;
    }
}
