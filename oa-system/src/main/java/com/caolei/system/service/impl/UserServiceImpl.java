package com.caolei.system.service.impl;

import com.caolei.system.pojo.User;
import com.caolei.system.repository.UserRepository;
import com.caolei.system.service.PermissionService;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.UserService;
import com.caolei.system.utils.EncryptUtils;
import com.caolei.system.utils.RequestUtils;
import com.caolei.system.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
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
    public User save(User user) {
        getLogger().info("save " + user.tableName() + "\t" + user.getId());
        return getRepository().save(user);
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
            EncryptUtils.encrypt(user);
        }
        return save(user);
    }

    @Override
    public User register(User user) {
        return save(EncryptUtils.encrypt(user));
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
            getLogger().info(user.getAccount() + " 登陆成功...");
            return true;
        } else {
            RequestUtils.setCurrentUser(null);
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
    public User findUserWithLogsByAccount(String account) {
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

}
