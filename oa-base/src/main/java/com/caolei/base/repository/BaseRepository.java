package com.caolei.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author caolei
 * @ClassName: BaseRepository
 * @Description: TODO
 * @date 2018/8/31 17:34
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * 批量查询
     * @param ids
     * @return
     */
    @Query("from #{#entityName} where id in (?1)")
    List<T> findAllByIds(Iterable<ID> ids);

    /**
     * 批量删除
     * - @Query 在使用 update 和 delete 语句时需要
     * - @Transactional
     * - @Modifying
     *
     * @param ids
     */
    @Transactional
    @Modifying
    @Query("delete from #{#entityName} where id in (?1)")
    void deleteAllByIds(Iterable<ID> ids);

}
