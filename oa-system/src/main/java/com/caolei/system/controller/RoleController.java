package com.caolei.system.controller;

import com.caolei.system.api.CrudController;
import com.caolei.system.api.CrudService;
import com.caolei.system.constant.Constants;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public void modelAdvice(String operation, String id, Model model) {
        Map map = model.asMap();
        Role role = (Role) map.get(getEntityName());
        User currentUser = RequestUtils.getCurrentUser();
        switch (operation) {
            case OP_DELETE:
            case OP_UPDATE:
                if (role.isSystemRole() && !currentUser.getSuperUser()) {
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
                model.addAttribute("permissions", RequestUtils.getCheckedList(allPermissions, hasPermissions));
                break;
            case OP_FIND:
                hasPermissions = role.getPermissions();
                model.addAttribute("permissions", RequestUtils.getCheckedList(hasPermissions, hasPermissions));
                break;
            case OP_LIST:
                break;
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @Override
    public String create(HttpServletRequest request, HttpServletResponse response,
                         Role role, RedirectAttributes redirectAttributes) {
        RequestUtils.checkOperation(getEntityName(), OP_CREATE);
        List<String> permissionIds = Arrays.asList(request.getParameterValues("permission-select[]"));
        List<Permission> permissions = new ArrayList<>();
        permissionIds.forEach(permissionId -> permissions.add(permissionService.findById(permissionId)));
        role.setPermissions(permissions);
        getService().save(role);
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return Constants.REDIRECT_TO + getModulePath() + "/" + getEntityName() + "/list";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @Override
    public String update(HttpServletRequest request, HttpServletResponse response,
                         @PathVariable(name = "id") String id, Role role, RedirectAttributes redirectAttributes) {
        RequestUtils.checkOperation(getEntityName(), OP_UPDATE, id);
        List<String> permissionIds = Arrays.asList(request.getParameterValues("permission-select[]"));
        List<Permission> permissions = new ArrayList<>();
        permissionIds.forEach(permissionId -> permissions.add(permissionService.findById(permissionId)));
        role.setPermissions(permissions);
        role = getService().updateById(id, role);
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return Constants.REDIRECT_TO + getModulePath() + "/" + getEntityName() + "/find/" + role.getId();
    }
}
