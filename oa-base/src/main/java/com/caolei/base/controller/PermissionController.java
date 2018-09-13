package com.caolei.base.controller;

import com.caolei.base.pojo.Permission;
import com.caolei.base.pojo.Role;
import com.caolei.base.pojo.User;
import com.caolei.base.service.PermissionService;
import com.caolei.base.service.RoleService;
import com.caolei.base.service.UserService;
import com.caolei.base.service.BaseCrudService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

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
    @ApiOperation(value = "查询用户的权限集合", notes = "传入userId,然后返回JSON类型的数据")
    @ApiImplicitParam(name = "userId", value = "用户Id", paramType = "String")
    @GetMapping(value = "/user/{userId}")
    @ResponseBody
    public ResponseEntity<List<Permission>> findUserPermission(
            @PathVariable(name = "userId") String userId, Permission permission, Model model) {
        User user = userService.findById(userId);
        permission.setUsers(Collections.singletonList(user));
        return ResponseEntity.ok(permissionService.findAll(Example.of(permission)));
    }

    @ApiOperation(value = "查询角色的权限集合", notes = "传入roleId,然后返回JSON类型的数据")
    @ApiImplicitParam(name = "roleId", value = "角色Id", paramType = "String")
    @GetMapping(value = "/role/{roleId}")
    @ResponseBody
    public ResponseEntity<List<Permission>> findRolePermission(
            @PathVariable(name = "roleId") String roleId, Permission permission, Model model) {
        Role role = roleService.findById(roleId);
        permission.setRoles(Collections.singletonList(role));
        return ResponseEntity.ok(permissionService.findAll(Example.of(permission)));
    }
}
