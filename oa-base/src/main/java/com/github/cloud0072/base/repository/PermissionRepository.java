package com.github.cloud0072.base.repository;

import com.github.cloud0072.base.model.Permission;
import com.github.cloud0072.common.constant.Operation;
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

    /**
     * 使用 _ 可以查询关联实体的属性
     * @param name
     * @return
     */
    List<Permission> findPermissionsByRoles_Users_AccountEquals(String name);
}
