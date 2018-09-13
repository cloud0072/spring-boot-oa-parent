package com.caolei.base.controller;

import com.caolei.base.pojo.Role;
import com.caolei.base.pojo.User;
import com.caolei.base.service.*;
import com.caolei.base.util.EntityUtils;
import com.caolei.base.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.caolei.common.constant.Constants.*;

@RequestMapping("/base/user")
@Controller
public class UserController
        extends BaseCrudController<User> {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 回调函数
     * 增强默认controller的参数集合
     *
     * @param model
     */
    @Override
    protected void modelAdvice(Model model, String operation, User user) {
        User currentUser = UserUtils.getCurrentUser();
        //修改自己的个人信息
        if (UserUtils.getCurrentUser().getId().equals(user.getId())) {
            model.addAttribute("type", TY_SELF);
        }
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

    @Override
    public BaseCrudService<User> service() {
        return userService;
    }

}
