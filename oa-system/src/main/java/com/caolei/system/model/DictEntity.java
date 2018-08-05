package com.caolei.system.model;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.pojo.DictCatalog;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
public class DictEntity extends BaseEntity {
    /**
     * 父节点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private DictEntity parent;
    /**
     * 子节点
     */
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<DictEntity> children;
    /**
     * 字典分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dict_catalog_id")
    private DictCatalog dictCatalog;
    /**
     * 实体的值
     */
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "entity", fetch = FetchType.LAZY)
    private Set<ColumnValue> values;

    public DictEntity() {
    }

    public DictEntity(DictEntity parent, Set<DictEntity> children, DictCatalog dictCatalog, Set<ColumnValue> values) {
        this.parent = parent;
        this.children = children;
        this.dictCatalog = dictCatalog;
        this.values = values;
    }

    @Override
    protected String getTableName() {
        return "字典实体";
    }

    @Override
    protected String getModuleName() {
        return "system";
    }

    public DictCatalog getDictCatalog() {
        return dictCatalog;
    }

    public void setDictCatalog(DictCatalog dictCatalog) {
        this.dictCatalog = dictCatalog;
    }

    public DictEntity getParent() {
        return parent;
    }

    public void setParent(DictEntity parent) {
        this.parent = parent;
    }

    public Set<DictEntity> getChildren() {
        return children;
    }

    public void setChildren(Set<DictEntity> children) {
        this.children = children;
    }

    public Set<ColumnValue> getValues() {
        return values;
    }

    public void setValues(Set<ColumnValue> values) {
        this.values = values;
    }
}
