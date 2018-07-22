package com.caolei.system.pojo;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.po.ColumnEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class DictEntity extends BaseEntity {

    @Column
    private Integer orderField;
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "dict_entity_id")
    private List<ColumnEntity> columnEntities;

    @Override
    public String tableName() {
        return "字典实体";
    }

    @Override
    public String moduleName() {
        return "system";
    }

    public List<ColumnEntity> getColumnEntities() {
        return columnEntities;
    }

    public void setColumnEntities(List<ColumnEntity> columnEntities) {
        this.columnEntities = columnEntities;
    }

    public Integer getOrderField() {
        return orderField;
    }

    public void setOrderField(Integer orderField) {
        this.orderField = orderField;
    }
}
