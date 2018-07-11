package com.caolei.system.controller;

import com.caolei.system.api.CrudController;
import com.caolei.system.api.CrudService;
import com.caolei.system.constant.Constants;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.caolei.system.constant.Constants.*;

/**
 * 使用RequiresRoles时需要使用open class 不然会报错 final 类不能 subclass
 */
@RequestMapping("/system/user")
@Controller
public class UserController
        implements CrudController<User, String> {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public CrudService<User, String> getService() {
        return userService;
    }

    @Override
    public String getModulePath() {
        return "/system";
    }

    @Override
    public String getEntityName() {
        return "user";
    }

    /**
     * 回调函数
     * 增强默认controller的参数集合
     *
     * @param operation
     * @param id
     * @param model
     */
    @Override
    public void modelAdvice(String operation, String id, Model model) {
        Map map = model.asMap();
        User user = (User) map.get(getEntityName());
        User currentUser = RequestUtils.getCurrentUser();
        switch (operation) {
            case OP_UPDATE:
            case OP_DELETE:
                if (user.isSystemUser() && !currentUser.getSuperUser()) {
                    throw new UnsupportedOperationException("您无权操作系统用户");
                }
            case OP_CREATE:
                List<Role> hasRoles = ObjectUtils.nvl(user.getRoles(), new ArrayList<>());
                List<Role> allRoles = currentUser.getRoles();
                //如果是超级用户可以 授权所有角色 否则只能赋予自身拥有的角色
                if (currentUser.getSuperUser()) {
                    allRoles = roleService.findAll();
                }
                model.addAttribute("roles", RequestUtils.getCheckedList(allRoles, hasRoles));
                break;
            case OP_FIND:
                hasRoles = user.getRoles();
                model.addAttribute("roles", RequestUtils.getCheckedList(hasRoles, hasRoles));
                break;
            case OP_LIST:
                break;
        }
    }

    /**
     * 跳转个人信息查看页面
     */
    @RequestMapping(value = "/find/self", method = RequestMethod.GET)
    public String findSelf(Model model) {
        String id = RequestUtils.getCurrentUser().getId();
        model.addAttribute("op", OP_FIND);
        model.addAttribute("type", TY_SELF);
        model.addAttribute(getEntityName(), userService.findById(id));
        modelAdvice(OP_FIND, id, model);
        return getModulePath() + "/" + getEntityName() + "/" + getEntityName() + "_view";
    }

    /**
     * 跳转个人信息修改页面
     */
    @RequestMapping(value = "/update/self", method = RequestMethod.GET)
    public String showUpdateSelfForm(Model model) {
        String id = RequestUtils.getCurrentUser().getId();
        model.addAttribute("op", OP_UPDATE);
        model.addAttribute("type", TY_SELF);
        model.addAttribute(getEntityName(), userService.findById(id));
        return getModulePath() + "/" + getEntityName() + "/" + getEntityName() + "_edit";
    }

    /**
     * 提交个人信息修改
     */
    @RequestMapping(value = "/update/self", method = RequestMethod.POST)
    public String updateSelf(User user, RedirectAttributes redirectAttributes) {
        String id = RequestUtils.getCurrentUser().getId();
        if (id.equals(user.getId())) {
            userService.updateById(id, user);
            redirectAttributes.addFlashAttribute("message", "修改成功");
        } else {
            redirectAttributes.addFlashAttribute("message", "修改失败");
        }
        return Constants.REDIRECT_TO + getModulePath() + "/" + getEntityName() + "/find/" + id;
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
        RequestUtils.checkOperation(getEntityName(), OP_CREATE);
        List<String> roleIds = Arrays.asList(request.getParameterValues("role-checked"));
        List<Role> roles = new ArrayList<>();
        roleIds.forEach(roleId -> roles.add(roleService.findById(roleId)));
        user.setRoles(roles);
        userService.register(user.setDefaultValue());
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return Constants.REDIRECT_TO + getModulePath() + "/" + getEntityName() + "/list";
    }

    /**
     * 重写 post 提交方法
     *
     * @param request
     * @param response
     * @param id
     * @param user
     * @param redirectAttributes
     * @return
     */
    @Override
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String update(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id,
                         User user, RedirectAttributes redirectAttributes) {
        RequestUtils.checkOperation(getEntityName(), OP_UPDATE, id);
        List<String> roleIds = Arrays.asList(request.getParameterValues("role-checked"));
        List<Role> roles = new ArrayList<>();
        roleIds.forEach(roleId -> roles.add(roleService.findById(roleId)));
        user.setRoles(roles);
        user = getService().updateById(id, user);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return Constants.REDIRECT_TO + getModulePath() + "/" + getEntityName() + "/find/" + user.getId();
    }
}
