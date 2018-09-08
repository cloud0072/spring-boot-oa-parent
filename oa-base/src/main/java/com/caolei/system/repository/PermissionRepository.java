package com.caolei.system.repository;

import com.caolei.common.api.repository.BaseRepository;
import com.caolei.common.constant.Operation;
import com.caolei.system.pojo.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cloud0072
 */
@Repository
public interface PermissionRepository
        extends BaseRepository<Permission, String> {

    /**
     * 获取 EntityResource 的权限
     *
     * @param codes
     * @param operation
     * @return
     */
    List<Permission> findPermissionsByEntityResource_CodeInAndOperationLessThanEqual(List<String> codes, Operation operation);

    List<Permission> findPermissionsByRoles_Users_AccountEquals(String name);
}
