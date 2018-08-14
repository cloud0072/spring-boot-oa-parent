//package com.caolei.system.model;
//
//import com.caolei.common.api.BaseEntity;
//import com.caolei.common.constant.ColumnType;
////import com.caolei.system.pojo.DictCatalog;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import javax.persistence.*;
//import java.util.Set;
//
//@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"id", "fieldOrder"}))
//public class ColumnConfig extends BaseEntity {
//    /**
//     * 字段名
//     */
//    @Column
//    private String columnName;
//    /**
//     * 字段类型
//     */
//    @Column
//    private ColumnType columnType;
//    /**
//     * 字段长度
//     */
//    @Column
//    private Integer length;
//    /**
//     * 第几个字段
//     */
//    @Column
//    private Integer fieldOrder;
//    /**
//     * 字段描述
//     */
//    @Column
//    private String columnDescription;
//
//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "dict_catalog_id")
//    private DictCatalog dictCatalog;
//
//    @JsonIgnore
//    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "config", fetch = FetchType.LAZY)
//    private Set<ColumnValue> values;
//
//    public ColumnConfig() {
//    }
//
//    public ColumnConfig(String columnName, ColumnType columnType, Integer length, Integer fieldOrder,
//                        String columnDescription, DictCatalog dictCatalog, Set<ColumnValue> values) {
//        this.columnName = columnName;
//        this.columnType = columnType;
//        this.length = length;
//        this.fieldOrder = fieldOrder;
//        this.columnDescription = columnDescription;
//        this.dictCatalog = dictCatalog;
//        this.values = values;
//    }
//
//    @Override
//    protected String getTableName() {
//        return "字段设置";
//    }
//
//    @Override
//    protected String getModuleName() {
//        return "system";
//    }
//
//    public String getColumnName() {
//        return columnName;
//    }
//
//    public void setColumnName(String columnName) {
//        this.columnName = columnName;
//    }
//
//    public ColumnType getColumnType() {
//        return columnType;
//    }
//
//    public void setColumnType(ColumnType columnType) {
//        this.columnType = columnType;
//    }
//
//    public Integer getLength() {
//        return length;
//    }
//
//    public void setLength(Integer length) {
//        this.length = length;
//    }
//
//    public Integer getFieldOrder() {
//        return fieldOrder;
//    }
//
//    public void setFieldOrder(Integer fieldOrder) {
//        this.fieldOrder = fieldOrder;
//    }
//
//    public String getColumnDescription() {
//        return columnDescription;
//    }
//
//    public void setColumnDescription(String columnDescription) {
//        this.columnDescription = columnDescription;
//    }
//
//    public DictCatalog getDictCatalog() {
//        return dictCatalog;
//    }
//
//    public void setDictCatalog(DictCatalog dictCatalog) {
//        this.dictCatalog = dictCatalog;
//    }
//
//    public Set<ColumnValue> getValues() {
//        return values;
//    }
//
//    public void setValues(Set<ColumnValue> values) {
//        this.values = values;
//    }
//}
