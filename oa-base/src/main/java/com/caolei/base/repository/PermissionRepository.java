package com.caolei.base.repository;

import com.caolei.base.pojo.Permission;
import com.caolei.common.api.repository.BaseRepository;
import com.caolei.common.constant.Operation;
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
