package com.caolei.system.api;


import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

/**
 * crud服务接口
 *
 * @param <T>  entity class
 * @author cloud0072
 * @date 2018/6/12 22:48
 */
public interface BaseCrudService<T extends BaseEntity>
        extends BaseService {

    /**
     * 返回数据源
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 21:50
     */
    JpaRepository<T, String> repository();

    /**
     * 修改
     *
     * @param id
     * @param input
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    T updateById(String id, T input);

    /**
     * 创建
     *
     * @param t
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default T save(T t) {
        logger().info("save " + t.tableName() + "\t" + t.getId());
        return repository().save(t);
    }

    /**
     * 保存多个
     *
     * @param var
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default void saveAll(List<T> var) {
        repository().saveAll(var);
    }

    /**
     * 删除
     *
     * @param t
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default void delete(T t) {
        repository().delete(t);
    }

    /**
     * 删除
     *
     * @param id
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default void deleteById(String id) {
        repository().deleteById(id);
    }

    /**
     * 删除所有
     *
     * @param list
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default void deleteAll(List<T> list) {
        repository().deleteAll(list);
    }

    /**
     * 查询
     *
     * @param example
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default T find(Example<T> example) {
        return repository().findOne(example).orElse(null);
    }

    /**
     * 查询
     *
     * @param id
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default T findById(String id) {
        return repository().findById(id).orElse(null);
    }

    /**
     * 查询所有
     *
     * @param example
     * @param pageable
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default Page<T> findAll(Example<T> example, Pageable pageable) {
        return repository().findAll(example, pageable);
    }

    /**
     * 查询所有
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default List<T> findAll(Example<T> example) {
        return repository().findAll(example);
    }

    /**
     * 查询所有
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default List<T> findAll() {
        return repository().findAll();
    }

}
