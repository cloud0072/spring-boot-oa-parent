package com.caolei.system.pojo;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.model.ColumnConfig;
import com.caolei.system.model.DictEntity;

import javax.persistence.*;
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
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "dictCatalog", fetch = FetchType.LAZY)
    private Set<ColumnConfig> configs;
    /**
     * 成员属性配置
     */
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "dictCatalog", fetch = FetchType.LAZY)
    private Set<DictEntity> entities;

    @Override
    protected String getTableName() {
        return "字典目录";
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ColumnConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(Set<ColumnConfig> configs) {
        this.configs = configs;
    }

    public Set<DictEntity> getEntities() {
        return entities;
    }

    public void setEntities(Set<DictEntity> entities) {
        this.entities = entities;
    }
}
