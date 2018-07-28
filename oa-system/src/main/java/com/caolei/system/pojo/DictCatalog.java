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
    /**
     * 字典名
     */
    @Column
    private String name;
    /**
     * 详细信息
     */
    @Column
    private String description;
    /**
     * 成员属性配置
     */
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "dict_catalog_id")
    private List<ColumnEntity> columnConfig;

//    /**
//     * 分类的根元素
//     */
//    @Column
//    private DictEntity root;

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

    public List<ColumnEntity> getColumnConfig() {
        return columnConfig;
    }

    public void setColumnConfig(List<ColumnEntity> columnConfig) {
        this.columnConfig = columnConfig;
    }

}
