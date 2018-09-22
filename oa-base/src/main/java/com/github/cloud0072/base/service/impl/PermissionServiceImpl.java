package com.github.cloud0072.base.service.impl;

import com.github.cloud0072.base.model.Permission;
import com.github.cloud0072.base.repository.BaseRepository;
import com.github.cloud0072.base.repository.EntityResourceRepository;
import com.github.cloud0072.base.repository.PermissionRepository;
import com.github.cloud0072.base.service.PermissionService;
import com.github.cloud0072.base.service.RoleService;
import com.github.cloud0072.common.constant.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public BaseRepository<Permission, String> repository() {
        return permissionRepository;
    }

    @Override
    public Permission update(Permission input,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        Permission permission = findById(input.getId());
        permission.setName(input.getName());
        permission.setCode(input.getCode());
        permission.setOperation(input.getOperation());
        permission.setResourceId(input.getResourceId());
        return repository().save(permission);
    }


    @Override
    public List<Permission> findPermissionsByResourceCodesAndOperationLess(List<String> codes, String operation) {
        return permissionRepository.findPermissionsByEntityResource_CodeInAndOperationLessThanEqual(
                codes, Operation.valueOf(operation));
    }

}
