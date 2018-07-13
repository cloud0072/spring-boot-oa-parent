package com.caolei.system.pojo;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.constant.TableConstant;
import com.caolei.system.po.ColumnEntiy;

import javax.persistence.*;
import java.util.ArrayList;

/**
 * @ClassName: DictCatalog
 * @Description: TODO
 * @author caolei
 * @date 2018/7/13 18:42
 */
@Entity
@Table
public class DictCatalog extends BaseEntity {

    @Column
    private String name;
    @Column
    private DictCatalog parent;
    @Column
    private Boolean root;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "dict_catalog_id")
    private ArrayList<ColumnEntiy> columnEntities;

    @Override
    public String tableName() {
        return TableConstant.DICT_CATALOG;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DictCatalog getParent() {
        return parent;
    }

    public void setParent(DictCatalog parent) {
        this.parent = parent;
    }

    public boolean isRoot() {
        return root == null ? false : root;
    }

    public Boolean getRoot() {
        return root;
    }

    public void setRoot(Boolean root) {
        this.root = root;
    }

    public ArrayList<ColumnEntiy> getColumnEntities() {
        return columnEntities;
    }

    public void setColumnEntities(ArrayList<ColumnEntiy> columnEntities) {
        this.columnEntities = columnEntities;
    }
}
