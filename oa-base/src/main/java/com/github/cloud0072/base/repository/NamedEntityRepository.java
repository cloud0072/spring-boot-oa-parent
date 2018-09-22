package com.github.cloud0072.base.repository;

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
public interface NamedEntityRepository<T, ID>
        extends BaseRepository<T, ID> {

    @Query("from #{#entityName} where name = ?1 order by name ")
    T findByName(String name);

    @Query("from #{#entityName} where name in (?1) order by name")
    List<T> findAllByNameIn(Iterable<String> names);

    @Query("from #{#entityName} where name like %?1% order by name ")
    List<T> findAllByNameLike(String name);

    @Transactional
    @Modifying
    @Query("delete from #{#entityName} where name = ?1 ")
    void deleteByName(String name);

}
