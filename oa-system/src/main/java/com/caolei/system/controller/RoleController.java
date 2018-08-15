package com.caolei.system.controller;

import com.caolei.common.util.EntityUtils;
import com.caolei.system.pojo.Permission;
import com.caolei.system.pojo.Role;
import com.caolei.system.pojo.User;
import com.caolei.system.service.PermissionService;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.UserService;
import com.caolei.system.util.SecurityUtils;
import com.caolei.system.web.BaseCrudController;
import com.caolei.system.web.BaseCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.caolei.common.constant.Constants.*;

/**
 * 使用RequiresRoles时需要使用open class 不然会报错 final 类不能 subclass
 */
@RequestMapping("/system/role")
@Controller
public class RoleController
        implements BaseCrudController<Role> {

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
    public BaseCrudService<Role> service() {
        return roleService;
    }

    @Override
    public void modelAdvice(Model model) {
        Map<String, Object> map = model.asMap();
        Role role = (Role) map.get(entityName());
        String operation = (String) map.get("op");

        User currentUser = SecurityUtils.getCurrentUser();
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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Override
    public String create(HttpServletRequest request, HttpServletResponse response,
                         Role role, RedirectAttributes redirectAttributes) {
        SecurityUtils.checkOperation(role, OP_CREATE);
        List<String> permissionIds = Arrays.asList(request.getParameterValues("permission-select[]"));
        List<Permission> permissions = new ArrayList<>();
        permissionIds.forEach(permissionId -> permissions.add(permissionService.findById(permissionId)));
        role.setPermissions(permissions);
        service().save(role);
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/list";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @Override
    public String update(HttpServletRequest request, HttpServletResponse response,
                         Role role, RedirectAttributes redirectAttributes) {
        SecurityUtils.checkOperation(role, OP_UPDATE);
        List<String> permissionIds = Arrays.asList(request.getParameterValues("permission-select[]"));
        List<Permission> permissions = new ArrayList<>();
        permissionIds.forEach(permissionId -> permissions.add(permissionService.findById(permissionId)));
        role.setPermissions(permissions);
        role = service().updateById(role.getId(), role);
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/find/" + role.getId();
    }

    @RequestMapping(value = "/clearRoleCache", method = RequestMethod.GET)
    public ResponseEntity<String> clearRoleCache() {
        roleService.clearRoleCache();
        return ResponseEntity.ok("重新加载权限成功");
    }
}
