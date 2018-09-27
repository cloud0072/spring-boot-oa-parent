package com.github.cloud0072.common.util;

import com.github.cloud0072.common.constant.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 加密工具
 *
 * @author cloud0072
 * @date 2018/6/12 22:37
 */
@Component
@Slf4j
public class MySecurityUtils extends org.apache.shiro.SecurityUtils {

    private MySecurityUtils() {
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
            log.error("Subject is not available");
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
        boolean[] ret = hasRoles(roles);
        boolean flag = false;
        for (boolean b : ret) {
            if (b) {
                flag = true;
            }
        }
        if (!flag) {
            String name = getSubject().getPrincipal().toString();
            throw new UnauthorizedException(name + " do not has role " + Arrays.asList(roles));
        }
    }

    public static void checkRole(String role) {
        getSubject().checkRole(role);
    }

    public static void checkAllRoles(String... roles) {
        getSubject().checkRoles(roles);
    }

    public static boolean hasRole(String role) {
        return getSubject().hasRole(role);
    }

    public static boolean[] hasRoles(String... roles) {
        return getSubject().hasRoles(Arrays.asList(roles));
    }

    public static boolean hasAllRoles(String... roles) {
        return getSubject().hasAllRoles(Arrays.asList(roles));
    }

    /**
     * 检查当前用户 是否包含其中一个权限
     *
     * @param permissions
     */
    public static void checkAnyPermission(String... permissions) {
        boolean[] ret = hasPermissions(permissions);
        boolean flag = false;
        for (boolean b : ret) {
            if (b) {
                flag = true;
            }
        }
        if (!flag) {
            String name = getSubject().getPrincipal().toString();
            throw new UnauthorizedException(name + " do not has permission " + Arrays.asList(permissions));
        }
    }

    public static void checkPermission(String permission) {
        getSubject().checkPermission(permission);
    }

    public static void checkAllPermissions(String... permission) {
        getSubject().checkPermissions(permission);
    }

    public static boolean hasPermission(String permission) {
        return getSubject().isPermitted(permission);
    }

    public static boolean[] hasPermissions(String... permission) {
        return getSubject().isPermitted(permission);
    }

    public static boolean hasAllPermission(String... permission) {
        return getSubject().isPermittedAll(permission);
    }

    /**
     * 判断当前用户是否有权限进行操作 GET 需要 FIND_ALL 权限
     * FIXME: 另一种策略是 有多少权限就能查到多少元素，待完善
     *
     * @param entityName
     * @param operation
     * @param resourceId
     */
    public static void checkOperation(String entityName, Operation operation, String resourceId) {
        String op = operation.code();
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
    public static void checkOperation(String entityName, Operation operation) {
        checkOperation(entityName, operation, null);
    }

}
