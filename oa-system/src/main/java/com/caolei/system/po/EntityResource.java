package com.caolei.system.po;


import com.caolei.system.api.BaseEntity;
import com.caolei.system.api.NamedEntity;
import com.caolei.system.constant.TableConstant;
import org.apache.poi.ss.formula.functions.Na;


import javax.persistence.*;

/**
 * 实体的名字 和 所属的标签
 *
 * @author cloud0072
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "label"})})
public class EntityResource extends BaseEntity implements NamedEntity {

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

    @Override
    public String getGroupName() {
        //namedEntity
        return getLabel();
    }

    @Override
    public String tableName() {
        return TableConstant.ENTITY_NAME;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
