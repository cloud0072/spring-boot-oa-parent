package com.caolei.system.po;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.constant.ColumnType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id", "orderField"}))
public class ColumnEntity extends BaseEntity {
    @Column
    private String columnName;
    @Column
    private String columnValue;
    @Column
    private ColumnType columnType;
    @Column
    private Integer orderField;
    @Column
    private Integer length;

    @Override
    public String tableName() {
        return "字段实体";
    }

    @Override
    public String moduleName() {
        return "system";
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

    public Integer getOrderField() {
        return orderField;
    }

    public void setOrderField(Integer orderField) {
        this.orderField = orderField;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
