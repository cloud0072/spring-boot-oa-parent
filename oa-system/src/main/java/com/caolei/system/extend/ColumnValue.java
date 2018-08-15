//package com.caolei.system.extend;
//
//import com.caolei.common.api.BaseEntity;
//
//import javax.persistence.*;
//
//@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"config_id", "entity_id"}))
//public class ColumnValue extends BaseEntity {
//    /**
//     * 值
//     */
//    @Column
//    private String columnValue;
//    /**
//     * 第几个字段
//     */
//    @Column
//    private Integer entityOrder;
//    /**
//     * 关联的字段设置
//     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "config_id")
//    private ColumnConfig config;
//    /**
//     * 关联的字典实体
//     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "entity_id")
//    private DictEntity entity;
//
//    public ColumnValue() {
//    }
//
//    public ColumnValue(String columnValue, Integer entityOrder, ColumnConfig config, DictEntity entity) {
//        this.columnValue = columnValue;
//        this.entityOrder = entityOrder;
//        this.config = config;
//        this.entity = entity;
//    }
//
//    @Override
//    protected String getTableName() {
//        return "字段实体";
//    }
//
//    @Override
//    protected String getModuleName() {
//        return "system";
//    }
//
//    public String getColumnValue() {
//        return columnValue;
//    }
//
//    public void setColumnValue(String columnValue) {
//        this.columnValue = columnValue;
//    }
//
//    public Integer getEntityOrder() {
//        return entityOrder;
//    }
//
//    public void setEntityOrder(Integer entityOrder) {
//        this.entityOrder = entityOrder;
//    }
//
//    public ColumnConfig getConfig() {
//        return config;
//    }
//
//    public void setConfig(ColumnConfig config) {
//        this.config = config;
//    }
//
//    public DictEntity getEntity() {
//        return entity;
//    }
//
//    public void setEntity(DictEntity entity) {
//        this.entity = entity;
//    }
//}
