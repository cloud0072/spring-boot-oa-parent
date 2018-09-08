package com.caolei.system.controller;

import com.caolei.common.api.controller.BaseCrudController;
import com.caolei.common.api.service.BaseCrudService;
import com.caolei.common.constant.FileType;
import com.caolei.common.util.EntityUtils;
import com.caolei.common.util.StringUtils;
import com.caolei.system.pojo.FileComponent;
import com.caolei.system.pojo.Role;
import com.caolei.system.pojo.User;
import com.caolei.system.service.FileComponentService;
import com.caolei.system.service.PermissionService;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.UserService;
import com.caolei.system.util.UserUtils;
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
        User user = (User) map.get(className);
        String operation = (String) map.get("op");

        User currentUser = UserUtils.getCurrentUser();
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
        user.setDefaultValue();

        List<String> roleIds = Arrays.asList(request.getParameterValues("role-checked"));
        List<Role> roles = new ArrayList<>();
        roleIds.forEach(roleId -> roles.add(roleService.findById(roleId)));
        user.getRoles().addAll(roles);

        String fileId = request.getParameter("head_photo_id");
        if (!StringUtils.isEmpty(fileId)) {
            FileComponent headPhoto = fileComponentService.findById(fileId);
            if (headPhoto.getCategory() == FileType.PORTRAIT) {
                user.getExtend().setHeadPhoto(headPhoto);
            }
        }

        userService.register(user);
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return REDIRECT_TO + "/" + modulePath() + "/" + entityPath() + "/list";
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

        List<String> roleIds = Arrays.asList(request.getParameterValues("role-checked"));
        List<Role> roles = new ArrayList<>();
        roleIds.forEach(roleId -> roles.add(roleService.findById(roleId)));
        user.getRoles().addAll(roles);

        String fileId = request.getParameter("head_photo_id");
        if (!StringUtils.isEmpty(fileId)) {
            FileComponent headPhoto = fileComponentService.findById(fileId);
            if (headPhoto.getCategory() == FileType.PORTRAIT) {
                user.getExtend().setHeadPhoto(headPhoto);
            }
        }

        user = service().updateById(user.getId(), user);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return REDIRECT_TO + "/" + modulePath() + "/" + entityPath() + "/find/" + user.getId();
    }

    /**
     * 跳转个人信息查看页面
     */
    @RequestMapping(value = "/find/self", method = RequestMethod.GET)
    public String findSelf(Model model) {
        String id = UserUtils.getCurrentUser().getId();
        User user = service().findById(id);
        putModel(model, OP_FIND, user, TY_SELF);
        return modulePath() + entityPath() + entityPath() + "_view";
    }

    /**
     * 跳转个人信息修改页面
     */
    @RequestMapping(value = "/update/self", method = RequestMethod.GET)
    public String showUpdateSelfForm(Model model) {
        String id = UserUtils.getCurrentUser().getId();
        model.addAttribute("op", OP_UPDATE);
        model.addAttribute("type", TY_SELF);
        model.addAttribute(entityPath(), service().findById(id));
        return modulePath() + entityPath() + entityPath() + "_edit";
    }

    /**
     * 提交个人信息修改
     */
    @RequestMapping(value = "/update/self", method = RequestMethod.POST)
    public String updateSelf(HttpServletRequest request, HttpServletResponse response,
                             User user, RedirectAttributes redirectAttributes) {
        String id = UserUtils.getCurrentUser().getId();
        if (id.equals(user.getId())) {

            String fileId = request.getParameter("head_photo_id");
            if (!StringUtils.isEmpty(fileId)) {
                FileComponent headPhoto = fileComponentService.findById(fileId);
                if (headPhoto.getCategory() == FileType.PORTRAIT) {
                    user.getExtend().setHeadPhoto(headPhoto);
                }
            }

            userService.updateById(id, user);
            redirectAttributes.addFlashAttribute("message", "修改成功");
        } else {
            redirectAttributes.addFlashAttribute("message", "修改失败");
        }
        return REDIRECT_TO + "/" + modulePath() + "/" + entityPath() + "/find/" + id;
    }

}
