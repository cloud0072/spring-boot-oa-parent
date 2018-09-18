package com.caolei.base.controller;

import com.caolei.base.exception.AjaxException;
import com.caolei.base.model.Role;
import com.caolei.base.model.User;
import com.caolei.base.service.BaseCrudService;
import com.caolei.base.service.PermissionService;
import com.caolei.base.service.RoleService;
import com.caolei.base.service.UserService;
import com.caolei.base.util.EntityUtils;
import com.caolei.base.util.UserUtils;
import lombok.NonNull;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
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
            case OP_DELETE:
            case OP_UPDATE:
            case OP_CREATE:
                Set<Role> hasRoles = EntityUtils.orNull(user.getRoles(), new LinkedHashSet<>());
                Set<Role> allRoles = userService.findById(currentUser.getId()).getRoles();
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

    /**
     * 进入修改密码页面
     *
     * @param userId
     * @param model
     * @return
     */
    @GetMapping("/resetpwd/{id}")
    public String showResetpwd(@PathVariable("id") String userId, Model model) {
        User u = UserUtils.getCurrentUser();
        if (u.getId().equals(userId) || u.isSuperUser()) {
            putModel(model, OP_UPDATE, u);
            return modulePath + entityPath + entityPath + "_resetpwd";
        }
        throw new UnauthorizedException("您没有权限进行此操作！");
    }

    /**
     * 重置密码
     *
     * @param userId
     * @param password
     * @param newpassword
     * @return
     */
    @PutMapping("/resetpwd/{id}")
    public ResponseEntity resetpwd(@PathVariable("id") String userId,
                                   @NonNull String password,
                                   @NonNull String newpassword) {
        try {
            User u = UserUtils.getCurrentUser();
            if (u.getId().equals(userId) || u.isSuperUser()) {
                String url = userService.resetpwd(userId, password, newpassword);

                Map<String, Object> map = new HashMap<>();
                map.put("message", "修改成功! 请重新登陆");
                map.put("url", url);
                return ResponseEntity.ok(map);
            }
        } catch (Exception e) {
            throw new AjaxException(e);
        }
        throw new UnauthorizedException("您没有权限进行此操作！");
    }


}
