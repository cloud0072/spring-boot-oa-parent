package com.github.cloud0072.base.repository;

import com.github.cloud0072.base.model.User;
import org.springframework.data.jpa.repository.Query;
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

    @Query("select u from User u join fetch u.roles join fetch u.permissions where account=?1")
    User findUserWithRolesAndPermissionsByAccount(String account);
}
