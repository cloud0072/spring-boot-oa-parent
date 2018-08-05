package com.caolei.system.api;

import com.caolei.system.util.StringUtils;
import com.caolei.system.web.BaseLogger;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Objects;

/**
 * 供序列化接口和Id用于继承
 * 子类可以直接使用该类的hashCode 和 equals 方法
 *
 * @author cloud0072
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable, BaseLogger {

    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;

    @Transient
    private String tableName;
    @Transient
    private String moduleName;
    @Transient
    private String entityName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseEntity)) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id);
    }

    /**
     * 获取表的中文名
     *
     * @return
     */
    public final String tableName() {
        if (tableName == null) {
            tableName = getTableName();
        }
        return tableName;
    }

    /**
     * 获取模块路径
     *
     * @return
     */
    public final String moduleName() {
        if (moduleName == null) {
            moduleName = getModuleName();
        }
        return moduleName;
    }

    /**
     * 获取实体名
     *
     * @return
     */
    public final String entityName() {
        if (entityName == null) {
            entityName = StringUtils.toLowerCaseFirstOne(getClass().getSimpleName());
        }
        return entityName;
    }

    protected abstract String getTableName();

    protected abstract String getModuleName();

}
