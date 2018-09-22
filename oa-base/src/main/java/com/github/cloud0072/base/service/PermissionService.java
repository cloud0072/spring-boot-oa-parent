package com.github.cloud0072.base.service;

import com.github.cloud0072.base.model.Permission;

import java.util.List;

/**
 * @author cloud0072
 */
public interface PermissionService
        extends BaseCrudService<Permission> {

    /**
     * 获取 EntityResource 的权限
     *
     * @param codes
     * @param operation
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:43
     */
    List<Permission> findPermissionsByResourceCodesAndOperationLess(List<String> codes, String operation);

}