package com.caolei.system.pojo;

import com.caolei.common.api.BaseEntity;
import com.caolei.system.model.CategoryType;

import javax.persistence.*;
import java.util.Set;

/**
 * 层级分类
 */
@Entity
@Table
public class Category extends BaseEntity {
    /**
     * 名称
     */
    @Column(nullable = false)
    private String name;
    /**
     * 分类类型
     */
    @ManyToOne
    @JoinColumn(name = "category_type_id")
    private CategoryType categoryType;
    /**
     * 父节点
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;
    /**
     * 子节点
     */
    @OneToMany
    private Set<Category> children;

    public Category() {
    }

    public Category(String name, CategoryType categoryType, Category parent) {
        this.name = name;
        this.categoryType = categoryType;
        this.parent = parent;
    }

    @Override
    protected String getTableName() {
        return "层级分类";
    }

    @Override
    protected String getModuleName() {
        return "system";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }
}
