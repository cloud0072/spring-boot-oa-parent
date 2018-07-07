package com.caolei.system.shiro;


import com.caolei.system.constant.Constants;
import com.caolei.system.pojo.User;
import com.caolei.system.utils.RequestUtils;
import com.caolei.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * 自定义认证组件
 *
 * @author cloud0072
 * @date 2018/6/12 22:43
 */
public class DefaultRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(AuthorizingRealm.class);

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
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token.getPrincipal() == null) {
            return null;
        }
        // 1.获取用户输入的用户名
        String account = token.getPrincipal().toString();
        log.info(account + " 正在验证身份...");

        //2.调用userService，根据用户名，查寻出对应的用户
        User user = userService.findAuthorInfoByAccount(account);
        if (null == user) {
            throw new UnknownAccountException("账号不存在");
        } else {
            SecurityUtils.getSubject().getSession().setAttribute(Constants.USER_INFO, user);
            return new SimpleAuthenticationInfo(
                    user.getUserName(),
                    user.getPassword(),
                    ByteSource.Util.bytes(user.getSalt()),
                    super.getName());
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
        User user = RequestUtils.getCurrentUser();
        user = userService.findAuthorInfoByAccount(user.getAccount());
        //获取自身角色
        user.getRoles().forEach(role -> info.addRole(role.getCode()));

        //获取自身权限
        user.getPermissions().forEach(permission -> info.addStringPermission(permission.getCode()));
        //获取角色附带的权限
        user.getRoles().forEach(role ->
                role.getPermissions().forEach(permission -> info.addStringPermission(permission.getCode())));
        return info;
    }

}