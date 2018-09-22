package com.github.cloud0072.base.model;

/**
 * @author caolei
 * @ClassName: SystemEntity
 * @Description: TODO
 * @date 2018/7/13 18:49
 */
public interface SystemEntity {

    default boolean isSystemEntity() {
        return getSystemEntity() == null ? false : getSystemEntity();
    }

    Boolean getSystemEntity();

}
