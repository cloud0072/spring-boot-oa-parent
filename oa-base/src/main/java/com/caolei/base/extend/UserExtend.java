package com.caolei.base.extend;

import com.caolei.common.annotation.EntityInfo;
import com.caolei.common.api.entity.BaseEntity;
import com.caolei.base.pojo.FileComponent;
import com.caolei.base.pojo.OperationLog;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 用户详情
 */
@EntityInfo(entityName = "用户详情", entityPath = "/userDetails" , tableName = "用户详情")
@EqualsAndHashCode(callSuper = true)
@Data
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

    public UserExtend() {
    }


}
