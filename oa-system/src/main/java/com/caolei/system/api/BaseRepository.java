package com.caolei.system.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @ClassName: BaseRepository
 * @Description: TODO
 * @author caolei
 * @date 2018/8/24 13:35
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

}
