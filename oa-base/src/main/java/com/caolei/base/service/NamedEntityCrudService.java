package com.caolei.base.service;

import com.caolei.base.model.BaseEntity;
import com.caolei.base.repository.NamedEntityRepository;

import java.util.List;

/**
 * namedEntity 基础服务接口
 *
 * @param <T>
 */
public interface NamedEntityCrudService<T extends BaseEntity>
        extends BaseCrudService<T> {

    /**
     * 数据源
     *
     * @return
     */
    NamedEntityRepository<T, String> repository();

    default T findByName(String name) {
        return repository().findByName(name);
    }

    default List<T> findAllByNameLike(String name) {
        return repository().findAllByNameLike(name);
    }

    default List<T> findAllByNameIn(Iterable<String> names) {
        return repository().findAllByNameIn(names);
    }

    default void deleteByName(String name) {
        repository().deleteByName(name);
    }

}
