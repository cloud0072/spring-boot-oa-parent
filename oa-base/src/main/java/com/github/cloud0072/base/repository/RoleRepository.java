package com.github.cloud0072.base.repository;

import com.github.cloud0072.base.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cloud0072
 */
@Repository
public interface RoleRepository
        extends BaseRepository<Role, String> {
    /**
     * 查询成员变量中的某个值 用 _ 代表成员变量
     * findByUsers_AccountEquals
     *
     * @param username
     * @return
     */
    List<Role> findByUsers_UsernameEquals(String username);

}
