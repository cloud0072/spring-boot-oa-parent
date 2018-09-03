package com.caolei.system.pojo;

import com.caolei.common.api.BaseEntity;
import com.caolei.common.api.NamedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

/**
 * 分类类型
 * 用户可以自定义
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class CategoryType extends BaseEntity implements NamedEntity {
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

}
