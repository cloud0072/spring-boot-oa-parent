package com.caolei.common.util;

import com.caolei.common.constant.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;

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
@Slf4j
public class SecurityUtils extends org.apache.shiro.SecurityUtils {

    private SecurityUtils() {

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
        List<Boolean> flag = new ArrayList<>();
        Stream.of(roles).forEach(role -> flag.add(getSubject().hasRole(role.toLowerCase())));
        if (!flag.contains(true)) {
            String name = getSubject().getPrincipal().toString();
            throw new UnauthorizedException(name + " do not has role " + Arrays.asList(roles));
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
            throw new UnauthorizedException(name + " do not has permission " + Arrays.asList(permissions));
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

}
