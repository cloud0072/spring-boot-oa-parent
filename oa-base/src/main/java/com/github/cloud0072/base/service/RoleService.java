package com.github.cloud0072.base.service;

import com.github.cloud0072.base.model.Role;

/**
 * @author cloud0072
 * @date 2018/6/12 22:42
 */
public interface RoleService
        extends BaseCrudService<Role> {

    /**
     * 查询
     *
     * @param name
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:47
     */
    Role findRoleByName(String name);

    Role findRoleByCode(String code);

}
