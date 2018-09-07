package com.caolei.system.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @ClassName: BaseRepository
 * @Description: TODO
 * @author caolei
 * @date 2018/8/31 17:34
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>,JpaSpecificationExecutor<T> {



}
