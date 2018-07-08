package com.caolei.system.controller;

import com.caolei.system.api.CrudController;
import com.caolei.system.api.CrudService;
import com.caolei.system.pojo.Permission;
import com.caolei.system.pojo.Role;
import com.caolei.system.pojo.User;
import com.caolei.system.service.PermissionService;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.UserService;
import com.caolei.system.utils.ObjectUtils;
import com.caolei.system.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

import static com.caolei.system.constant.Constants.*;

/**
 * 使用RequiresRoles时需要使用open class 不然会报错 final 类不能 subclass
 */
@RequestMapping("/system/role")
@Controller
public class RoleController
        implements CrudController<Role, String> {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public CrudService<Role, String> getService() {
        return roleService;
    }

    @Override
    public String getModulePath() {
        return "/system";
    }

    @Override
    public String getEntityName() {
        return "role";
    }

    @Override
    public void methodAdvice(String operation, String id, Model model) {
        Map map = model.asMap();
        Role role = (Role) map.get(getEntityName());
        User currentUser = RequestUtils.getCurrentUser();
        switch (operation) {
            case OP_DELETE:
            case OP_UPDATE:
                if (role.getSystemRole() && !currentUser.getSuperUser()) {
                    throw new UnsupportedOperationException("您无权操作系统角色");
                }
            case OP_CREATE:
                List<Permission> hasPermissions = ObjectUtils.nvl(role.getPermissions(), new ArrayList<>());
                List<Permission> allPermissions = new ArrayList<>();

                //如果是超级用户可以 授权所有角色 否则只能赋予自身拥有的角色
                if (currentUser.getSuperUser()) {
                    allPermissions.addAll(permissionService.findAll());
                } else {
                    List<Permission> currentUserPermissions = new ArrayList<>(currentUser.getPermissions());
                    currentUser.getRoles().forEach(r -> currentUserPermissions.addAll(r.getPermissions()));
                    allPermissions.addAll(currentUserPermissions);
                }
                model.addAttribute("permissions", RequestUtils.getCheckedList(allPermissions,hasPermissions));
                break;
            case OP_FIND:
                hasPermissions = role.getPermissions();
                model.addAttribute("permissions", RequestUtils.getCheckedList(hasPermissions,hasPermissions));
                break;
            case OP_LIST:
                break;
        }
    }
}
