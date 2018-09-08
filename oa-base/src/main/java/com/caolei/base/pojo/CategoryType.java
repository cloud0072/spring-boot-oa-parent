package com.caolei.base.pojo;

import com.caolei.common.annotation.EntityInfo;
import com.caolei.common.api.entity.BaseEntity;
import com.caolei.common.api.entity.NamedEntity;
import com.caolei.common.api.module.BaseModuleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

/**
 * 分类类型
 * 用户可以自定义
 */
@EntityInfo(entityName = "分类类型",entityPath = "/categoryType")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class CategoryType
        extends BaseEntity
        implements NamedEntity, BaseModuleEntity {

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

    public String getName() {
        return name;
    }

}
