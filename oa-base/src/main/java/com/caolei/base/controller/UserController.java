package com.caolei.base.controller;

import com.caolei.base.pojo.Role;
import com.caolei.base.pojo.User;
import com.caolei.base.service.FileComponentService;
import com.caolei.base.service.PermissionService;
import com.caolei.base.service.RoleService;
import com.caolei.base.service.UserService;
import com.caolei.base.util.UserUtils;
import com.caolei.common.api.controller.BaseCrudController;
import com.caolei.common.api.service.BaseCrudService;
import com.caolei.common.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static com.caolei.common.constant.Constants.*;

/**
 * 使用RequiresRoles时需要使用open class 不然会报错 final 类不能 subclass
 */
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
    @Autowired
    private FileComponentService fileComponentService;

    /**
     * 回调函数
     * 增强默认controller的参数集合
     *
     * @param model
     */
    @Override
    public void modelAdvice(Model model) {
        Map<String, Object> map = model.asMap();
        User user = (User) map.get(className);
        String operation = (String) map.get("op");

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
