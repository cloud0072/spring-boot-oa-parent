package com.caolei.system.repository;

import com.caolei.system.api.BaseRepository;
import com.caolei.system.pojo.User;
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
