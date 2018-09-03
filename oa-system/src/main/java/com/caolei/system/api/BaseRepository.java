package com.caolei.system.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName: BaseRepository
 * @Description: TODO
 * @author caolei
 * @date 2018/8/31 17:34
 */
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    @Query("from #{#entityName} where name= ?1 ")
    List<T> find(String name);

}
