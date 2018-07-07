package com.caolei.system.service.impl;

import com.caolei.system.constant.Constants;
import com.caolei.system.pojo.User;
import com.caolei.system.repository.UserRepository;
import com.caolei.system.service.PermissionService;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.UserService;
import com.caolei.system.utils.EncryptUtils;
import com.caolei.system.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cloud0072
 */
@Service
public class UserServiceImpl
        implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Override
    public JpaRepository<User, String> getRepository() {
        return userRepository;
    }

    @Override
    public User register(User user) {
        return userRepository.save(EncryptUtils.getInstance().encrypt(user));
    }

    @Override
    public boolean login(User user) {
        //subject理解成权限对象。类似user
        Subject subject = SecurityUtils.getSubject();
        //创建用户名和密码的令牌
        UsernamePasswordToken token = new UsernamePasswordToken(user.getAccount(), user.getPassword());
        //记录该令牌，如果不记录则类似购物车功能不能使用。
        token.setRememberMe(false);
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            getLogger().error("用户不存在");
        } catch (IncorrectCredentialsException e) {
            getLogger().error("用户名密码不匹配。");
        } catch (AuthenticationException e) {
            getLogger().error("认证失败");
        }
        //验证是否成功登录的方法
        if (subject.isAuthenticated()) {
            getLogger().info(user.getAccount() + " 登陆成功...");
            return true;
        } else {
            SecurityUtils.getSubject().getSession().removeAttribute(Constants.USER_INFO);
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User findUserByAccountFetchLogs(String account) {
        User user = userRepository.findUserByAccount(account);
        user.getLogs().size();
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

    @Override
    public User update(String id, User input) {
        User user = findById(id);
        user.setUserName(input.getUserName());
        user.setAccount(input.getAccount());
        user.setEmail(input.getEmail());
        user.setPhone(input.getPhone());

        if (input.getRoles() != null) {
            user.setRoles(input.getRoles());
        }
        if (input.getPermissions() != null) {
            user.setPermissions(input.getPermissions());
        }
        if (!StringUtils.isEmpty(input.getPassword())) {
            user.setPassword(input.getPassword());
            EncryptUtils.getInstance().encrypt(user);
        }
        return save(user);
    }
}
