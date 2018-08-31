package com.caolei.system.pojo;

import com.caolei.common.api.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * 分类类型
 * 用户可以自定义
 */
@Entity
@Table
public class CategoryType extends BaseEntity {
    /**
     * 分类类型
     */
    @Column
    private String name;
    /**
     * 所有分类
     */
    @OneToMany(mappedBy = "categoryType", fetch = FetchType.LAZY)
    private Set<Category> categories;

    public CategoryType() {
    }

    @Override
    protected String getTableName() {
        return "分类类型";
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
