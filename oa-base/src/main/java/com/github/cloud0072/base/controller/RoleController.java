package com.github.cloud0072.base.controller;

import com.github.cloud0072.base.model.Permission;
import com.github.cloud0072.base.model.Role;
import com.github.cloud0072.base.model.User;
import com.github.cloud0072.base.service.PermissionService;
import com.github.cloud0072.base.service.RoleService;
import com.github.cloud0072.base.service.UserService;
import com.github.cloud0072.base.util.UserUtils;
import com.github.cloud0072.base.service.BaseCrudService;
import com.github.cloud0072.base.util.EntityUtils;
import com.github.cloud0072.common.constant.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import static com.github.cloud0072.common.constant.Constants.*;

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

    @Override
    protected void modelAdvice(Model model, Operation operation, Role role) {
        User currentUser = UserUtils.getCurrentUser();
        switch (operation) {
            case DELETE:
            case PUT:
            case POST:
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
            case GET:
                hasPermissions = role.getPermissions();
                if (hasPermissions!=null){
                    model.addAttribute("permissions", EntityUtils.getCheckedList(hasPermissions, hasPermissions));
                }
                break;
        }
    }

    @Override
    public BaseCrudService<Role> service() {
        return roleService;
    }

    /**
     * 清空权限缓存
     * @return
     */
    @DeleteMapping("/clearRoleCache")
    public ResponseEntity<String> clearRoleCache() {
        roleService.clearRoleCache();
        return ResponseEntity.ok("重新加载权限成功");
    }
}
