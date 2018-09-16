package com.caolei.base.entity;

import com.caolei.common.annotation.EntityInfo;
import com.caolei.common.module.BaseModuleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

/**
 * 分类
 */
@EntityInfo(description = "分类", entityName = "category", entityPath = "/category")
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"parent_id", "name"}),
        @UniqueConstraint(columnNames = {"parent_id", "categoryOrder"})
})
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
     * 顺序
     */
    @Column(name = "categoryOrder", nullable = false)
    private Integer categoryOrder;
    /**
     * 图标 ---选填
     */
    @Column
    private String icon;
    /**
     * 链接 ---选填
     */
    @Column
    private String url;
    /**
     * 是否是系统自带的实体
     */
    @Column
    private Boolean systemEntity;
    /**
     * 父节点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    /**
     * 子节点
     */
    //FIXME: List换成Set会抛StackOverflowError 原因未知
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> children;
    /**
     * 根节点 (可以作为大类来使用)
     */
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "root_id")
    private Category root;

    public Category() {
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", categoryOrder=" + categoryOrder +
                ", icon='" + icon + '\'' +
                ", url='" + url + '\'' +
                ", systemEntity=" + systemEntity +
                '}';
    }
}