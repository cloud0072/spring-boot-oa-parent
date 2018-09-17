package com.caolei.base.service;

import com.caolei.base.model.BaseEntity;
import com.caolei.base.repository.BaseRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * crud服务接口
 *
 * @param <T> model class
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
    BaseRepository<T, String> repository();

    /**
     * 创建
     *
     * @param t
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default T save(T t, HttpServletRequest request, HttpServletResponse response) {
        return repository().save(t);
    }

    /**
     * 保存多个
     *
     * @param var
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    default void saveAll(Iterable<? extends T> var) {
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
    default void deleteAll(Iterable<? extends T> list) {
        repository().deleteAll(list);
    }

    /**
     * 删除所有
     * 通过id
     *
     * @param ids
     */
    default void deleteAllByIds(Iterable<String> ids) {
        repository().deleteAllByIds(ids);
    }

    /**
     * 修改
     *
     * @param input
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:49
     */
    T update(T input, HttpServletRequest request, HttpServletResponse response);

    default T update(T input) {
        return update(input, null, null);
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

    default Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return repository().findAll(spec, pageable);
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

    /**
     * 查询所有id
     *
     * @param ids
     * @return
     */
    default List<T> findAllByIds(Iterable<String> ids) {
        return repository().findAllByIds(ids);
    }

}
