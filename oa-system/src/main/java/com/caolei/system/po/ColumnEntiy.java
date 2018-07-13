package com.caolei.system.po;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.constant.ColumnType;
import com.caolei.system.constant.TableConstant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @ClassName: Column
 * @Description: TODO
 * @author caolei
 * @date 2018/7/13 19:52
 */
@Entity
@Table
public class ColumnEntiy extends BaseEntity {

    @Column
    private String name;
    @Column
    private ColumnType columnType;
    @Column
    private Integer length;

    public ColumnEntiy(String name, ColumnType columnType, Integer length) {
        this.name = name;
        this.columnType = columnType;
        this.length = length;
    }

    public ColumnEntiy() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String tableName() {
        return TableConstant.COLUMN_ENTITY;
    }
}
