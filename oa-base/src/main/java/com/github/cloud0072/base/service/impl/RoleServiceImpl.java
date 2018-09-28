package com.github.cloud0072.base.service.impl;

import com.github.cloud0072.base.model.Permission;
import com.github.cloud0072.base.model.Role;
import com.github.cloud0072.base.repository.BaseRepository;
import com.github.cloud0072.base.repository.RoleRepository;
import com.github.cloud0072.base.service.PermissionService;
import com.github.cloud0072.base.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cloud0072
 */
@Service
public class RoleServiceImpl
        implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionService permissionService;

    @Override
    public BaseRepository<Role, String> repository() {
        return roleRepository;
    }

    @Override
    public Role save(Role role, HttpServletRequest request, HttpServletResponse response) {

        if (request != null) {
            List<String> permissionIds = Arrays.asList(request.getParameterValues("permission-select[]"));
            List<Permission> permissions = new ArrayList<>();
            permissionIds.forEach(permissionId -> permissions.add(permissionService.findById(permissionId)));
            role.setPermissions(permissions);
        }

        return repository().save(role);
    }

    @Override
    public Role update(Role input,
                       HttpServletRequest request,
                       HttpServletResponse response) {

        Role role = findById(input.getId());
        role.setName(input.getName());
        role.setCode(input.getCode());
        role.setDescription(input.getDescription());

        save(role, request, response);

        return role;
    }

    @Override
    public Role findRoleByName(String name) {
        Role role = new Role();
        role.setName(name);
        return find(Example.of(role));
    }

    @Override
    public Role findRoleByCode(String code) {
        Role role = new Role();
        role.setCode(code);
        return find(Example.of(role));
    }

}
