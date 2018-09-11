package com.caolei.common.api.service;


import com.caolei.common.api.entity.BaseEntity;
import com.caolei.common.api.entity.BaseService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * crud服务接口
 *
 * @param <T> entity class
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
     * @param input
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    T update(T input,
             HttpServletRequest request,
             HttpServletResponse response);

    default T update(T input) {
        return update(input, null, null);
    }

    /**
     * 创建
     *
     * @param t
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default T save(T t,
                   HttpServletRequest request,
                   HttpServletResponse response) {
        return repository().save(t);
    }

    /**
     * 保存多个
     *
     * @param var
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default void saveAll(Collection<? extends T> var,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        repository().saveAll(var);
    }

    default void saveAll(Collection<? extends T> var) {
        saveAll(var, null, null);
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
    default void deleteAll(Collection<? extends T> list) {
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
