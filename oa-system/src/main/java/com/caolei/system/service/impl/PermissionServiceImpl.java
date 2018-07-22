package com.caolei.system.service.impl;

import com.caolei.system.constant.Operation;
import com.caolei.system.pojo.Permission;
import com.caolei.system.repository.PermissionRepository;
import com.caolei.system.repository.EntityResourceRepository;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cloud0072
 * @date 2018/6/12 22:47
 */
@Service
public class PermissionServiceImpl
        implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private EntityResourceRepository entityResourceRepository;
    @Autowired
    private RoleService roleService;

    @Override
    public Permission updateById(String id, Permission input) {
        return null;
    }

    @Override
    public JpaRepository<Permission, String> repository() {
        return permissionRepository;
    }

    @Override
    public List<Permission> findPermissionsByResourceCodesAndOperationLess(List<String> codes, String operation) {
        return permissionRepository.findPermissionsByEntityResource_CodeInAndOperationLessThanEqual(codes,Operation.valueOf(operation));
    }

}
