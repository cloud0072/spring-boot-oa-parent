package com.caolei.system.service;

import com.caolei.system.api.BaseCrudService;
import com.caolei.system.pojo.Permission;

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