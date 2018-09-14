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
     * 实体名称 一般使用class名 首字母小写
     *
     * @return
     */
    String entityName();

    /**
     * 实体路径 一般使用 '/' + entityName
     *
     * @return
     */
    String entityPath();

    /**
     * 实体描述信息 中文描述信息
     * @return
     */
    String description() default "";

}
