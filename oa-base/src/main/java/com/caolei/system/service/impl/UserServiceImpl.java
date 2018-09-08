package com.caolei.system.service.impl;

import com.caolei.common.util.SecurityUtils;
import com.caolei.common.util.StringUtils;
import com.caolei.system.extend.UserExtend;
import com.caolei.system.pojo.User;
import com.caolei.system.repository.UserRepository;
import com.caolei.system.service.PermissionService;
import com.caolei.system.service.RoleService;
import com.caolei.system.service.UserService;
import com.caolei.system.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    @Override
    public JpaRepository<User, String> repository() {
        return userRepository;
    }

    @Override
    public User updateById(String id, User input) {
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
            UserUtils.encrypt(user);
        }

        if (input.getExtend() != null) {
            UserExtend extend = input.getExtend();
            if (extend.getHeadPhoto() != null) {
                user.getExtend().setHeadPhoto(extend.getHeadPhoto());
            }
            if (extend.getBirthday() != null) {
                user.getExtend().setBirthday(extend.getBirthday());
            }
        }

        return save(user);
    }

    @Override
    public User register(User user) {
        return save(UserUtils.encrypt(user));
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
            User u = UserUtils.getCurrentUser();
            u.setLastLoginTime(new Date());
            save(u);
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

}
