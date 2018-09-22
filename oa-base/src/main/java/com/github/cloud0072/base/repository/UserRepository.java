package com.github.cloud0072.base.repository;

import com.github.cloud0072.base.model.User;
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
