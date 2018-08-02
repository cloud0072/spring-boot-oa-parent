package com.caolei.system.controller;

import com.caolei.system.util.BaseCrudController;
import com.caolei.system.util.BaseCrudService;
import com.caolei.system.pojo.Permission;
import com.caolei.system.pojo.Role;
import com.caolei.system.pojo.User;
import com.caolei.system.service.PermissionService;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.UserService;
import com.caolei.system.util.RequestUtils;
import com.caolei.system.util.RequestUtils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;

/**
 * 使用RequiresRoles时需要使用open class 不然会报错 final 类不能 subclass
 */
@RequestMapping("/system/permission")
@Controller
public class PermissionController
        implements BaseCrudController<Permission> {

    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Autowired
    public PermissionController(UserService userService, RoleService roleService, PermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @Override
    public BaseCrudService<Permission> service() {
        return permissionService;
    }

    /**
     * 查询用户自身的权限集合
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public Result findUserPermission(@PathVariable(name = "userId") String userId,
                                     Model model, Permission permission) {
        User user = userService.findById(userId);
        permission.setUsers(Collections.singletonList(user));
        return RequestUtils.success(permissionService.findAll(Example.of(permission)));
    }

    /**
     * 查询角色的权限集合
     */
    @RequestMapping(value = "/role/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public Result findRolePermission(@PathVariable(name = "roleId") String roleId,
                                     Model model, Permission permission) {
        //检查权限
        RequestUtils.checkAnyRole("superuser");
        Role role = roleService.findById(roleId);
        permission.setRoles(Collections.singletonList(role));
        return RequestUtils.success(permissionService.findAll(Example.of(permission)));
    }
}
