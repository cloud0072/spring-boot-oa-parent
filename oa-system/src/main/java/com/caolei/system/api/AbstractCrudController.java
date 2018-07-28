package com.caolei.system.api;

import com.caolei.system.utils.ReflectUtils;

public abstract class AbstractCrudController<T extends BaseEntity>
        implements BaseCrudController<T> {

    private T entity;

    /**
     * 返回一个空对象实例
     * 用于查询和调用实例内的方法
     *
     * @return
     */
    public T instance() {
        if (entity == null) {
            try {
                entity = (T) ReflectUtils.getClassGenericType(getClass(), 0).newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                error(e.getMessage());
                throw new UnsupportedOperationException("泛型读取错误,请检查" + getClass().getName());
            }
        }
        return entity;
    }

}
