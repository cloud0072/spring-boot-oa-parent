package com.caolei.base.service.impl;

import com.caolei.base.model.Permission;
import com.caolei.base.repository.EntityResourceRepository;
import com.caolei.base.repository.PermissionRepository;
import com.caolei.base.service.PermissionService;
import com.caolei.base.service.RoleService;
import com.caolei.common.constant.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import com.caolei.base.repository.BaseRepository;
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
        return null;
    }


    @Override
    public List<Permission> findPermissionsByResourceCodesAndOperationLess(List<String> codes, String operation) {
        return permissionRepository.findPermissionsByEntityResource_CodeInAndOperationLessThanEqual(
                codes, Operation.valueOf(operation));
    }

}
