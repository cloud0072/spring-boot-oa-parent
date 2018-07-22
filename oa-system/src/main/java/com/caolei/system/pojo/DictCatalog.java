package com.caolei.system.pojo;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.po.ColumnEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * @author caolei
 * @ClassName: 字典目录
 * @Description: TODO
 * @date 2018/7/13 18:42
 */
@Entity
@Table
public class DictCatalog extends BaseEntity {

    @Column
    private String name;
    @Column
    private String description;
    @Column
    private Boolean root;
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "dict_catalog_id")
    private List<ColumnEntity> columnConfig;
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "dict_catalog_id")
    private List<DictEntity> dictEntities;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private DictCatalog parent;
    @OneToMany(mappedBy = "parent", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Set<DictCatalog> children;

    @Override
    public String tableName() {
        return "字典目录";
    }

    @Override
    public String moduleName() {
        return "system";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRoot() {
        return root;
    }

    public void setRoot(Boolean root) {
        this.root = root;
    }

    public DictCatalog getParent() {
        return parent;
    }

    public void setParent(DictCatalog parent) {
        this.parent = parent;
    }

    public Set<DictCatalog> getChildren() {
        return children;
    }

    public void setChildren(Set<DictCatalog> children) {
        this.children = children;
    }

    public List<ColumnEntity> getColumnConfig() {
        return columnConfig;
    }

    public void setColumnConfig(List<ColumnEntity> columnConfig) {
        this.columnConfig = columnConfig;
    }

    public List<DictEntity> getDictEntities() {
        return dictEntities;
    }

    public void setDictEntities(List<DictEntity> dictEntities) {
        this.dictEntities = dictEntities;
    }
}
