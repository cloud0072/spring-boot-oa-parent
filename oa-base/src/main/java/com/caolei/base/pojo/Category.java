package com.caolei.base.pojo;

import com.caolei.base.entity.BaseEntity;
import com.caolei.base.entity.NamedEntity;
import com.caolei.base.entity.SystemEntity;
import com.caolei.common.annotation.EntityInfo;
import com.caolei.common.module.BaseModuleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

/**
 * 分类
 */
@EntityInfo(description="分类",entityName = "category", entityPath = "/category")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "parent_id"})})
public class Category
        extends BaseEntity
        implements SystemEntity, NamedEntity, BaseModuleEntity {
    /**
     * 名称
     */
    @Column(name = "name", nullable = false)
    private String name;
//    经过推算， 其实分组类型就是父节点名称 ，并没有什么意义
//    当需要大类的时候只需要parent为null的对象为顶级分类就可以了
//    /**
//     * 分类类型
//     */
//    @ManyToOne
//    @JoinColumn(name = "category_type_id")
//    private CategoryType categoryType;
    /**
     * 是否是系统自带的实体
     */
    @Column
    private Boolean systemEntity;
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
    /**
     * 根节点 (可以作为大类来使用)
     */
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "root_id")
    private Category root;

    public Category() {
    }


}
