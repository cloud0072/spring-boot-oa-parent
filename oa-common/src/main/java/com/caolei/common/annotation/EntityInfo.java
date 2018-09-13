package com.caolei.common.annotation;

import java.lang.annotation.*;

/**
 * 配置实体类的基础信息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface EntityInfo {

    /**
     * 实体名称
     *
     * @return
     */
    String entityName();

    /**
     * 实体路径
     *
     * @return
     */
    String entityPath();

    /**
     * 表名称
     *
     * @return
     */
    String tableName() default "";

}
