package com.caolei.system.pojo;

import com.caolei.common.api.BaseEntity;
import com.caolei.common.api.NamedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

/**
 * 层级分类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Category extends BaseEntity implements NamedEntity {
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
    @OneToMany(mappedBy = "parent")
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

}
