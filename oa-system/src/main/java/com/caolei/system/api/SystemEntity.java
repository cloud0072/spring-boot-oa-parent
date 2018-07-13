package com.caolei.system.api;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @ClassName: SystemEntity
 * @Description: TODO
 * @author caolei
 * @date 2018/7/13 18:49
 */
@MappedSuperclass
public abstract class SystemEntity extends BaseEntity{

    @Column
    protected Boolean systemEntity;

    public boolean isSystemEntity(){
        return systemEntity == null ? false : systemEntity;
    }

    public Boolean getSystemEntity() {
        return systemEntity;
    }

    public void setSystemEntity(Boolean systemEntity) {
        this.systemEntity = systemEntity;
    }
}
