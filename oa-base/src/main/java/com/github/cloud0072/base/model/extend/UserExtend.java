package com.github.cloud0072.base.model.extend;

import com.github.cloud0072.base.model.FileComponent;
import com.github.cloud0072.base.model.OperationLog;
import com.github.cloud0072.base.model.BaseEntity;
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
//@EntityInfo(description="用户详情",entityName = "userDetails", entityPath = "/userDetails")
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
