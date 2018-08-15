package com.caolei.system.controller;

import com.caolei.common.util.EntityUtils;
import com.caolei.system.pojo.Role;
import com.caolei.system.pojo.User;
import com.caolei.system.service.PermissionService;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.UserService;
import com.caolei.system.util.SecurityUtils;
import com.caolei.system.web.BaseCrudController;
import com.caolei.system.web.BaseCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.caolei.common.constant.Constants.*;

/**
 * 使用RequiresRoles时需要使用open class 不然会报错 final 类不能 subclass
 */
@RequestMapping("/system/user")
@Controller
public class UserController
        implements BaseCrudController<User> {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;


    @Override
    public BaseCrudService<User> service() {
        return userService;
    }

    /**
     * 回调函数
     * 增强默认controller的参数集合
     *
     * @param model
     */
    @Override
    public void modelAdvice(Model model) {
        Map<String, Object> map = model.asMap();
        User user = (User) map.get(entityName());
        String operation = (String) map.get("op");

        User currentUser = SecurityUtils.getCurrentUser();
        switch (operation) {
            case OP_UPDATE:
            case OP_DELETE:
            case OP_CREATE:
                Set<Role> hasRoles = EntityUtils.orNull(user.getRoles(), new LinkedHashSet<>());
                Set<Role> allRoles = currentUser.getRoles();
                //如果是超级用户可以 授权所有角色 否则只能赋予自身拥有的角色
                if (currentUser.getSuperUser()) {
                    allRoles = new LinkedHashSet<>(roleService.findAll());
                }
                model.addAttribute("roles", EntityUtils.getCheckedList(allRoles, hasRoles));
                break;
            case OP_FIND:
                hasRoles = user.getRoles();
                model.addAttribute("roles", EntityUtils.getCheckedList(hasRoles, hasRoles));
                break;
            case OP_LIST:
                break;
        }
    }

    /**
     * 重写 post 提交方法
     *
     * @param request
     * @param response
     * @param user
     * @param redirectAttributes
     * @return
     */
    @Override
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(HttpServletRequest request, HttpServletResponse response, User user, RedirectAttributes redirectAttributes) {
        SecurityUtils.checkOperation(instance(), OP_CREATE);
        List<String> roleIds = Arrays.asList(request.getParameterValues("role-checked"));
        List<Role> roles = new ArrayList<>();
        roleIds.forEach(roleId -> roles.add(roleService.findById(roleId)));
        user.getRoles().addAll(roles);
        userService.register(user.setDefaultValue());
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/list";
    }

    /**
     * 重写 post 提交方法
     *
     * @param request
     * @param response
     * @param user
     * @param redirectAttributes
     * @return
     */
    @Override
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(HttpServletRequest request, HttpServletResponse response,
                         User user, RedirectAttributes redirectAttributes) {
        SecurityUtils.checkOperation(user, OP_UPDATE);
        List<String> roleIds = Arrays.asList(request.getParameterValues("role-checked"));
        List<Role> roles = new ArrayList<>();
        roleIds.forEach(roleId -> roles.add(roleService.findById(roleId)));
        user.getRoles().addAll(roles);
        user = service().updateById(user.getId(), user);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/find/" + user.getId();
    }

    /**
     * 跳转个人信息查看页面
     */
    @RequestMapping(value = "/find/self", method = RequestMethod.GET)
    public String findSelf(Model model) {
        String id = SecurityUtils.getCurrentUser().getId();
        User user = service().findById(id);
        SecurityUtils.checkOperation(user, OP_FIND);
        putModel(model, OP_FIND, user, TY_SELF);
        return "/" + moduleName() + "/" + entityName() + "/" + entityName() + "_view";
    }

    /**
     * 跳转个人信息修改页面
     */
    @RequestMapping(value = "/update/self", method = RequestMethod.GET)
    public String showUpdateSelfForm(Model model) {
        String id = SecurityUtils.getCurrentUser().getId();
        model.addAttribute("op", OP_UPDATE);
        model.addAttribute("type", TY_SELF);
        model.addAttribute(entityName(), service().findById(id));
        return "/" + moduleName() + "/" + entityName() + "/" + entityName() + "_edit";
    }

    /**
     * 提交个人信息修改
     */
    @RequestMapping(value = "/update/self", method = RequestMethod.POST)
    public String updateSelf(User user, RedirectAttributes redirectAttributes) {
        String id = SecurityUtils.getCurrentUser().getId();
        if (id.equals(user.getId())) {
            userService.updateById(id, user);
            redirectAttributes.addFlashAttribute("message", "修改成功");
        } else {
            redirectAttributes.addFlashAttribute("message", "修改失败");
        }
        return REDIRECT_TO + "/" + moduleName() + "/" + entityName() + "/find/" + id;
    }

}
