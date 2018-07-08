package com.caolei.system.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @param <ID> entity id
 * @author cloud0072
 * @date 2018/6/12 22:48
 */
public interface CrudService<T extends BaseEntity, ID extends Serializable>
        extends BaseService {

    /**
     * 创建
     *
     * @param t
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default T save(T t) {
        getLogger().info("save " + t.tableName() + "\t" + t.getId());
        return getRepository().save(t);
    }

    /**
     * 保存多个
     *
     * @param var
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default void saveAll(List<T> var) {
        getRepository().saveAll(var);
    }

    /**
     * 删除
     *
     * @param t
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default void delete(T t) {
        getRepository().delete(t);
    }

    /**
     * 删除
     *
     * @param id
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    /**
     * 删除所有
     *
     * @param list
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default void deleteAll(List<T> list) {
        getRepository().deleteAll(list);
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
        return getRepository().findOne(example).orElse(null);
    }

    /**
     * 查询
     *
     * @param id
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default T findById(ID id) {
        return getRepository().findById(id).orElse(null);
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
        return getRepository().findAll(example, pageable);
    }

    /**
     * 查询所有
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default List<T> findAll(Example<T> example) {
        return getRepository().findAll(example);
    }

    /**
     * 查询所有
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default List<T> findAll() {
        return getRepository().findAll();
    }

    /**
     * 获取类名
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 23:02
     */
    default String getClassName() {
        return this.getClass().getName();
    }

    /**
     * 修改
     *
     * @param id
     * @param input
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    T update(ID id, T input);

    /**
     * 返回数据源
     *
     * @return
     * @author cloud0072
     * @date 2018/6/12 21:50
     */
    JpaRepository<T, ID> getRepository();
}
