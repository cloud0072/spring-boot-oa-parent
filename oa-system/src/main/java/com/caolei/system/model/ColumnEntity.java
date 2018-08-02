package com.caolei.system.model;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.constant.ColumnType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id", "fieldOrder"}))
public class ColumnEntity extends BaseEntity {
    /**
     * 字段名
     */
    @Column
    private String columnName;
    /**
     * 字段值 分类(DictCatalog)中为空
     */
    @Column
    private String columnValue;
    /**
     * 字段类型
     */
    @Column
    private ColumnType columnType;
    /**
     * 字段长度
     */
    @Column
    private Integer length;
    /**
     * 第几个字段
     */
    @Column
    private Integer fieldOrder;

    public ColumnEntity() {
    }

    public ColumnEntity(String columnName, String columnValue, ColumnType columnType, Integer length, Integer fieldOrder) {
        this.columnName = columnName;
        this.columnValue = columnValue;
        this.columnType = columnType;
        this.length = length;
        this.fieldOrder = fieldOrder;
    }

    public ColumnEntity(String columnName) {
        this.columnName = columnName;
        this.columnType = ColumnType.VARCHAR;
        this.length = 255;
    }

    @Override
    protected String getTableName(){
        return "字段实体";
    }

    @Override
    protected String getModuleName(){
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

    public Integer getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }
}
