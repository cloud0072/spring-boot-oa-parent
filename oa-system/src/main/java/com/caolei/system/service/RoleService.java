package com.caolei.system.service;

import com.caolei.system.api.CrudService;
import com.caolei.system.pojo.Role;

/**
 * @author cloud0072
 * @date 2018/6/12 22:42
 */
public interface RoleService
        extends CrudService<Role, String> {

    /**
     * 查询
     * @author cloud0072
     * @date 2018/6/12 22:47
     * @param name
     * @return
     */
    Role findRoleByName(String name);

    Role findRoleByCode(String code);


}
