package com.github.cloud0072.base.config.shiro;


import com.github.cloud0072.base.model.User;
import com.github.cloud0072.base.service.UserService;
import com.github.cloud0072.base.util.UserUtils;
import com.github.cloud0072.common.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Date;

/**
 * 自定义认证组件
 *
 * @author cloud0072
 * @date 2018/6/12 22:43
 */
@Slf4j
public class DefaultRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    /**
     * 认证.登录
     *
     * @param token
     * @return
     * @throws AuthenticationException
     * @author cloud0072
     * @date 2018/6/12 22:43
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        if (token.getPrincipal() == null) {
            return null;
        }
        // 1.获取用户输入的用户名
        String account = token.getPrincipal().toString();
        log.info(account + " 正在验证身份...");

        //2.调用userService，根据用户名，查寻出对应的用户
        User user = userService.findUserByAccount(account);
        if (null == user) {
            throw new UnknownAccountException("账号不存在");
        } else {
            user.setLastLoginTime(new Date());
            userService.repository().save(user);

            UserUtils.setCurrentUser(user);
            return new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(),
                    ByteSource.Util.bytes(user.getSalt()), super.getName());
        }
    }

    /**
     * 授权
     *
     * @param principal
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:43
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //获取session中的用户
        User user = userService.findAuthorInfoByAccount((String) SecurityUtils.getSubject().getPrincipal());
        //获取自身权限
        user.getPermissions().forEach(permission -> info.addStringPermission(permission.getCode()));
        //获取自身角色 和附带的权限
        user.getRoles().forEach(role -> {
            info.addRole(role.getCode());
            role.getPermissions().forEach(permission -> info.addStringPermission(permission.getCode()));
        });
        return info;
    }



}