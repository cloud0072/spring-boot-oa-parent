package com.caolei.common.api.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author caolei
 * @ClassName: SystemEntity
 * @Description: TODO
 * @date 2018/7/13 18:49
 */
@MappedSuperclass
public abstract class SystemEntity extends BaseEntity {

    @Column
    protected Boolean systemEntity;

    public Boolean getSystemEntity() {
        return systemEntity;
    }

    public void setSystemEntity(Boolean systemEntity) {
        this.systemEntity = systemEntity;
    }

    public Boolean isSystemEntity() {
        return systemEntity == null ? false : systemEntity;
    }

}
