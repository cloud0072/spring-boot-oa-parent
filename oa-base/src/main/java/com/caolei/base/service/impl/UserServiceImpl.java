package com.caolei.base.service.impl;

import com.caolei.base.pojo.FileComponent;
import com.caolei.base.pojo.Role;
import com.caolei.base.pojo.User;
import com.caolei.base.repository.UserRepository;
import com.caolei.base.service.FileComponentService;
import com.caolei.base.service.PermissionService;
import com.caolei.base.service.RoleService;
import com.caolei.base.service.UserService;
import com.caolei.base.util.UserUtils;
import com.caolei.common.constant.FileType;
import com.caolei.common.util.SecurityUtils;
import com.caolei.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cloud0072
 */
@Slf4j
@Service
public class UserServiceImpl
        implements UserService {
    /**
     * 不要使用 构造器方式注入 否则会产生循环注入的问题
     */
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private FileComponentService fileComponentService;

    @Override
    public JpaRepository<User, String> repository() {
        return userRepository;
    }

    @Override
    public User update(User input, HttpServletRequest request, HttpServletResponse response) {

        User user = findById(input.getId());

        user.setUserName(input.getUserName());
        user.setAccount(input.getAccount());
        user.setEmail(input.getEmail());
        user.setPhone(input.getPhone());

        if (!StringUtils.isEmpty(input.getPassword())) {
            user.setPassword(input.getPassword());
            UserUtils.encrypt(user);
        }

        updateUserAdvice(user, request);

        return repository().save(user);
    }

    /**
     * 调用此方法前请先对密码进行加密
     *
     * @param user
     * @param request
     * @param response
     * @return
     */
    @Override
    public User save(User user, HttpServletRequest request, HttpServletResponse response) {
        //调用save默认执行加密 如果有其他需求 置为false
        return this.save(user, request, response, true);
    }

    @Override
    public User save(User user, HttpServletRequest request, HttpServletResponse response,
                     boolean encrypt) {

        user.setDefaultValue();
        if (encrypt) {
            user = UserUtils.encrypt(user);
        }

        updateUserAdvice(user, request);

        return repository().save(user);
    }

    @Override
    public boolean login(User user) {
        //subject理解成权限对象。类似user
        Subject subject = SecurityUtils.getSubject();
        //创建用户名和密码的令牌
        UsernamePasswordToken token = new UsernamePasswordToken(user.getAccount(), user.getPassword());
        //记录该令牌，如果不记录则类似购物车功能不能使用。
        token.setRememberMe(false);
        //统一处理登录异常信息
        subject.login(token);
        //验证是否成功登录的方法
        if (subject.isAuthenticated()) {
            log.info(user.getAccount() + " 登陆成功...");
            return true;
        } else {
            UserUtils.setCurrentUser(null);
        }
        return false;
    }

    @Override
    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    @Override
    public User findUserByAccount(String account) {
        return userRepository.findUserByAccount(account);
    }

    /**
     * 虽然可以用Example查询但是如果想使用事物必须使用repository的查询
     *
     * @param account
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public User findUserWithLogsByAccount(String account) {
        User user = userRepository.findUserByAccount(account);
        user.getExtend().getLogs().size();
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User findAuthorInfoByAccount(String account) {
        User user = userRepository.findUserByAccount(account);
        user.getRoles().forEach(role -> role.getPermissions().size());
        user.getPermissions().size();
        return user;
    }

    private void updateUserAdvice(User user, HttpServletRequest request) {
        if (request != null) {

            String[] roleIdArrays = request.getParameterValues("role-checked");
            if (roleIdArrays != null) {
                List<String> roleIds = Arrays.asList(roleIdArrays);
                List<Role> roles = new ArrayList<>();
                roleIds.forEach(roleId -> roles.add(roleService.findById(roleId)));

                user.getRoles().addAll(roles);
            }

            String fileId = request.getParameter("head_photo_id");
            if (!StringUtils.isEmpty(fileId)) {
                FileComponent headPhoto = fileComponentService.findById(fileId);
                if (headPhoto.getCategory() == FileType.PORTRAIT) {
                    user.getExtend().setHeadPhoto(headPhoto);
                }
            }

        }

    }

}
