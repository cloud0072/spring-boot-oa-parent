package com.caolei.system.controller;

import com.caolei.common.api.controller.BaseCrudController;
import com.caolei.common.api.service.BaseCrudService;
import com.caolei.common.util.SecurityUtils;
import com.caolei.system.pojo.Permission;
import com.caolei.system.pojo.Role;
import com.caolei.system.pojo.User;
import com.caolei.system.service.PermissionService;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

/**
 * 使用RequiresRoles时需要使用open class 不然会报错 final 类不能 subclass
 */
@RequestMapping("/base/permission")
@Controller
public class PermissionController
        extends BaseCrudController<Permission> {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public BaseCrudService<Permission> service() {
        return permissionService;
    }

    /**
     * 查询用户自身的权限集合
     */
    @ApiOperation(value = "获取用户的权限集合", notes = "传入userId,然后返回JSON类型的数据")
    @ApiImplicitParam(name = "userId", value = "用户Id", paramType = "String")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Permission>> findUserPermission(@PathVariable(name = "userId") String userId,
                                                               Model model, Permission permission) {
        User user = userService.findById(userId);
        permission.setUsers(Collections.singletonList(user));
        return ResponseEntity.ok(permissionService.findAll(Example.of(permission)));
    }

    /**
     * 查询角色的权限集合
     */
    @RequestMapping(value = "/role/{roleId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Permission>> findRolePermission(@PathVariable(name = "roleId") String roleId,
                                                               Model model, Permission permission) {
        //检查权限
        SecurityUtils.checkAnyRole("superuser");
        Role role = roleService.findById(roleId);
        permission.setRoles(Collections.singletonList(role));
        return ResponseEntity.ok(permissionService.findAll(Example.of(permission)));
    }
}
