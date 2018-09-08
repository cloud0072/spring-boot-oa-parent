package com.caolei.base.controller;

import com.caolei.base.pojo.Permission;
import com.caolei.base.pojo.Role;
import com.caolei.base.pojo.User;
import com.caolei.base.service.PermissionService;
import com.caolei.base.service.RoleService;
import com.caolei.base.service.UserService;
import com.caolei.base.util.UserUtils;
import com.caolei.common.api.controller.BaseCrudController;
import com.caolei.common.api.service.BaseCrudService;
import com.caolei.common.util.EntityUtils;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.caolei.common.constant.Constants.*;

/**
 * 使用RequiresRoles时需要使用open class 不然会报错 final 类不能 subclass
 */
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
    public BaseCrudService<Role> service() {
        return roleService;
    }

    @Override
    public void modelAdvice(Model model) {
        Map<String, Object> map = model.asMap();
        Role role = (Role) map.get(className);
        String operation = (String) map.get("op");

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

    @ApiOperation("提交创建对象")
    @PostMapping
    @ResponseBody
    @Override
    public ResponseEntity create(Role role,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

//        SecurityUtils.checkOperation(t, OP_CREATE);

        List<String> permissionIds = Arrays.asList(request.getParameterValues("permission-select[]"));
        List<Permission> permissions = new ArrayList<>();
        permissionIds.forEach(permissionId -> permissions.add(permissionService.findById(permissionId)));
        role.setPermissions(permissions);
        service().save(role, request, response);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "新增成功");
        map.put("url", modulePath() + entityPath() + "/view/" + role.getId());
        return ResponseEntity.ok(map);
    }

    @ApiOperation("提交更新对象")
    @PutMapping("/{id}")
    @ResponseBody
    @Override
    public ResponseEntity update(@PathVariable("id") @NonNull String id,
                                 Role role,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
//        SecurityUtils.checkOperation(t, OP_UPDATE);

        List<String> permissionIds = Arrays.asList(request.getParameterValues("permission-select[]"));
        List<Permission> permissions = new ArrayList<>();
        permissionIds.forEach(permissionId -> permissions.add(permissionService.findById(permissionId)));
        role.setPermissions(permissions);
        role = service().update(role, request, response);

        Map<String, Object> map = new HashMap<>();
        map.put("message", "修改成功");
        map.put("url", modulePath() + entityPath() + "/view/" + role.getId());
        return ResponseEntity.ok(map);
    }

    @RequestMapping(value = "/clearRoleCache", method = RequestMethod.GET)
    public ResponseEntity<String> clearRoleCache() {
        roleService.clearRoleCache();
        return ResponseEntity.ok("重新加载权限成功");
    }
}
