package com.caolei.base.repository;

import com.caolei.base.pojo.User;
import com.caolei.common.api.repository.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cloud0072
 */
@Repository
public interface UserRepository
        extends BaseRepository<User, String> {

    /**
     * 查询
     *
     * @param account
     * @return
     */
    User findUserByAccount(String account);

}
