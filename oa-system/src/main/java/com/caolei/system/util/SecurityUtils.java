package com.caolei.system.util;

import com.caolei.common.api.BaseEntity;
import com.caolei.common.api.SystemEntity;
import com.caolei.common.constant.Constants;
import com.caolei.common.constant.Operation;
import com.caolei.common.util.HttpUtils;
import com.caolei.system.pojo.User;
import com.caolei.common.api.BaseLogger;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 加密工具
 *
 * @author cloud0072
 * @date 2018/6/12 22:37
 */
@Component
public class SecurityUtils extends org.apache.shiro.SecurityUtils implements BaseLogger {

    private static Integer HASH_ITERATIONS;
    private static Logger logger;

    private SecurityUtils() {
        logger = logger();
    }

    /**
     * 判断shiro是否可用
     *
     * @return
     */
    public static boolean isSubjectAvailable() {
        try {
            org.apache.shiro.SecurityUtils.getSubject();
        } catch (Exception e) {
            logger.error("Subject is not available");
            return false;
        }
        return true;
    }

    /**
     * 检查当前用户 是否包含其中一个角色
     *
     * @param roles
     */
    public static void checkAnyRole(String... roles) {
        List<Boolean> flag = new ArrayList<>();
        Stream.of(roles).forEach(role -> flag.add(getSubject().hasRole(role.toLowerCase())));
        if (!flag.contains(true)) {
            String name = getSubject().getPrincipal().toString();
            throw new AuthorizationException(name + " do not has role " + Arrays.asList(roles));
        }
    }

    /**
     * 检查当前用户 是否包含其中一个权限
     *
     * @param permissions
     */
    public static void checkAnyPermission(String... permissions) {
        List<Boolean> flag = new ArrayList<>();
        Stream.of(permissions).forEach(permission -> flag.add(getSubject().isPermitted(permission.toLowerCase())));
        if (!flag.contains(true)) {
            String name = getSubject().getPrincipal().toString();
            throw new AuthorizationException(name + " do not has permission " + Arrays.asList(permissions));
        }
    }

    /**
     * 判断当前用户是否有权限进行操作 OP_LIST 需要 FIND_ALL 权限
     * FIXME: 另一种策略是 有多少权限就能查到多少元素，待完善
     *
     * @param entityName
     * @param operation
     * @param resourceId
     */
    public static void checkOperation(String entityName, String operation, String resourceId) {
        String op = Operation.of(operation).name();
        String id = resourceId == null ? "*" : resourceId;
        String permission = entityName + ":" + op + ":" + id;
        checkAnyPermission(permission);
    }

    /**
     * 判断当前用户是否有权限进行操作 默认为所有元素操作
     * 适用于 create 和 list
     *
     * @param entityName
     * @param operation
     */
    public static void checkOperation(String entityName, String operation) {
        checkOperation(entityName, operation, null);
    }

    public static void checkOperation(BaseEntity entity, String operation) {
        if (entity instanceof SystemEntity) {
            SystemEntity systemEntity = (SystemEntity) entity;
            if (systemEntity.isSystemEntity() && !getCurrentUser().isSuperUser()) {
                throw new UnsupportedOperationException("您无权操作系统对象");
            }
        }
        checkOperation(entity.entityName(), operation, entity.getId());
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public static User getCurrentUser() {
        return (User) HttpUtils.httpSession().getAttribute(Constants.USER_INFO);
    }

    /**
     * 重新设置当前用户属性
     *
     * @param user
     */
    public static void setCurrentUser(User user) {
        if (user == null) {
            HttpUtils.httpSession().removeAttribute(Constants.USER_INFO);
        }
        HttpUtils.httpSession().setAttribute(Constants.USER_INFO, user);
    }

    /**
     * 加密
     *
     * @param user
     * @author cloud0072
     * @date 2018/6/12 22:37
     */
    public static User encrypt(User user) {
        if (StringUtils.isEmpty(user.getSalt())) {
            throw new NullPointerException("用户的加密信息缺失,请确认后重试");
        }
        user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt(), HASH_ITERATIONS).toString());
        return user;
    }

    @Value("${shiro.hash.iterations}")
    private void setHashIterations(Integer hashIterations) {
        HASH_ITERATIONS = hashIterations;
    }

}
