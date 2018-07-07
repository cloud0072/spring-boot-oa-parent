package com.caolei.system.repository;

import com.caolei.system.constant.Operation;
import com.caolei.system.pojo.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cloud0072
 */
@Repository
public interface PermissionRepository
        extends JpaRepository<Permission, String> {

    /**
     * 获取 EntityResource 的权限
     *
     * @param codes
     * @param operation
     * @return
     */
    List<Permission> findPermissionsByEntityResource_CodeInAndOperationLessThanEqual(List<String> codes,Operation operation);
}
