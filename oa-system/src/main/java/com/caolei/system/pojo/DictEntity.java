//package com.caolei.system.pojo;
//
//import com.caolei.system.api.BaseEntity;
//import com.caolei.system.model.ColumnEntity;
//
//import javax.persistence.*;
//import java.util.List;
//import java.util.Set;
//
//@Entity
//@Table
//public class DictEntity extends BaseEntity {
//    /**
//     * 字典分类
//     */
//    @Column
//    private DictCatalog dictCatalog;
//    @ManyToOne
//    private DictEntity parent;
//    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_id")
//    private Set<DictEntity> children;
//    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
//
//    @JoinColumn(name = "dict_entity_id")
//    private List<ColumnEntity> columnEntities;
//
//    private String name;
//
//    @Override
//    protected String getTableName(){
//        return "字典实体";
//    }
//
//    @Override
//    protected String getModuleName(){
//        return "system";
//    }
//
//    public List<ColumnEntity> getColumnEntities() {
//        return columnEntities;
//    }
//
//    public void setColumnEntities(List<ColumnEntity> columnEntities) {
//        this.columnEntities = columnEntities;
//    }
//}
