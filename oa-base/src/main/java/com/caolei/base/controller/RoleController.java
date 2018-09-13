package com.caolei.base.controller;

import com.caolei.base.pojo.Permission;
import com.caolei.base.pojo.Role;
import com.caolei.base.pojo.User;
import com.caolei.base.service.PermissionService;
import com.caolei.base.service.RoleService;
import com.caolei.base.service.UserService;
import com.caolei.base.util.UserUtils;
import com.caolei.base.service.BaseCrudService;
import com.caolei.base.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.caolei.common.constant.Constants.*;

@RequestMapping("/base/role")
@Controller
public class RoleController
        extends BaseCrudController<Role> {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    public RoleController(UserService userService, RoleService roleService, PermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @Override
    protected void modelAdvice(Model model, String operation,  Role role) {
        User currentUser = UserUtils.getCurrentUser();
        switch (operation) {
            case OP_DELETE:
            case OP_UPDATE:
            case OP_CREATE:
                List<Permission> hasPermissions = EntityUtils.orNull(role.getPermissions(), new ArrayList<>());
                List<Permission> allPermissions = new ArrayList<>();

                //如果是超级用户可以 授权所有角色 否则只能赋予自身拥有的角色
                if (currentUser.getSuperUser()) {
                    allPermissions.addAll(permissionService.findAll());
                } else {
                    List<Permission> currentUserPermissions = new ArrayList<>(currentUser.getPermissions());
                    currentUser.getRoles().forEach(r -> currentUserPermissions.addAll(r.getPermissions()));
                    allPermissions.addAll(currentUserPermissions);
                }
                model.addAttribute("permissions", EntityUtils.getCheckedList(allPermissions, hasPermissions));
                break;
            case OP_FIND:
                hasPermissions = role.getPermissions();
                model.addAttribute("permissions", EntityUtils.getCheckedList(hasPermissions, hasPermissions));
                break;
            case OP_LIST:
                break;
        }
    }

    @Override
    public BaseCrudService<Role> service() {
        return roleService;
    }

    @RequestMapping(value = "/clearRoleCache", method = RequestMethod.GET)
    public ResponseEntity<String> clearRoleCache() {
        roleService.clearRoleCache();
        return ResponseEntity.ok("重新加载权限成功");
    }
}
