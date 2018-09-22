package com.github.cloud0072.common.annotation;

import java.lang.annotation.*;

/**
 * 配置实体类的基础信息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ModuleInfo {
    /**
     * 模块名称
     *
     * @return
     */
    String moduleName();

    /**
     * 模块路径
     *
     * @return
     */
    String modulePath();
}
