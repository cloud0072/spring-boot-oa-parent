package com.caolei.system.service.impl;

import com.caolei.system.pojo.Role;
import com.caolei.system.repository.RoleRepository;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.PermissionService;
import com.caolei.system.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

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
    public JpaRepository<Role, String> repository() {
        return roleRepository;
    }

    @Override
    public Role updateById(String id, Role input) {
        Role role = findById(id);
        role.setName(input.getName());
        role.setCode(input.getCode());
        role.setDescription(input.getDescription());
        if (input.getPermissions() != null) {
            role.setPermissions(input.getPermissions());
        }
        save(role);
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
