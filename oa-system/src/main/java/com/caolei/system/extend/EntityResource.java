package com.caolei.system.extend;


import com.caolei.common.api.BaseEntity;
import com.caolei.common.api.NamedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 实体的名字 和 所属的标签
 *
 * @author cloud0072
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "label"})})
public class EntityResource
        extends BaseEntity
        implements NamedEntity {

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
            this.label = "pojo";
        }
        this.code = path[path.length - 1];
    }

    public EntityResource() {
    }

    /*@Override
    public String getGroupName() {
        //namedEntity
        return getLabel();
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected String getTableName() {
        return "实体名";
    }

    @Override
    protected String getModuleName() {
        return "system";
    }

    public String getLabel() {
        return label == null ? "" : label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
