package com.caolei.base.entity.extend;


import com.caolei.base.entity.BaseEntity;
import com.caolei.base.entity.NamedEntity;
import com.caolei.common.module.BaseModuleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 实体的名字 和 所属的标签
 *
 * @author cloud0072
 */
//@EntityInfo(description="实体的信息",entityName = "entityInfo", entityPath = "/entityInfo")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "label"})})
public class EntityResource
        extends BaseEntity
        implements NamedEntity, BaseModuleEntity {

    @Column
    private String name;

    @Column
    private String label;

    @Column(nullable = false, unique = true)
    private String code;

    public EntityResource(String label, String code) {
        this.label = label;
        this.code = code;
    }

    public EntityResource(Class<? extends BaseEntity> c) {
        String[] path = c.getCanonicalName().split("\\.");

        if (path.length > 2) {
            this.label = path[path.length - 3];
        } else {
            this.label = "entity";
        }
        this.code = path[path.length - 1];
        this.name = code;
    }

    public EntityResource() {
    }

    public String getName() {
        return name;
    }

}
