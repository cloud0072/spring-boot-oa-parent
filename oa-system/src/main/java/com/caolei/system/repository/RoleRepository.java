package com.caolei.system.repository;

import com.caolei.system.pojo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cloud0072
 */
@Repository
public interface RoleRepository
        extends JpaRepository<Role, String> {

    List<Role> findByUsers_AccountEquals(String userName);
}
