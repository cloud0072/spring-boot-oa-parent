package com.caolei.system.utils;


import com.caolei.system.api.BaseEntity;
import com.caolei.system.api.NamedEntity;
import com.caolei.system.api.SystemEntity;
import com.caolei.system.constant.Constants;
import com.caolei.system.constant.Operation;
import com.caolei.system.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.Session;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Stream;

import static com.caolei.system.constant.Constants.TXT_LOCALHOST;
import static com.caolei.system.constant.Constants.TXT_UNKNOWN;

/**
 * 请求工具类
 *
 * @author cloud0072
 * @date 2018/6/12 22:38
 */
public class RequestUtils {

    private RequestUtils() {
    }

    /**
     * 获取会话
     *
     * @return
     */
    private static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    private static String getSessionId() {
        return (String) getSession().getId();
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public static User getCurrentUser() {
        return (User) getSession().getAttribute(Constants.USER_INFO);
    }

    /**
     * 重新设置当前用户属性
     *
     * @param user
     */
    public static void setCurrentUser(User user) {
        if (user == null) {
            getSession().removeAttribute(Constants.USER_INFO);
        }
        getSession().setAttribute(Constants.USER_INFO, user);
    }

    /**
     * 返回带有状态码和信息json
     *
     * @param message
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(message, HttpStatus.OK.value(), data);
    }

    public static Result success(String message) {
        return new Result<>(message, HttpStatus.OK.value());
    }

    public static <T> Result<T> success(T data) {
        return new Result<>("success", HttpStatus.OK.value(), data);
    }

    public static <T> Result<T> error(String message, HttpStatus status, T data) {
        return new Result<>(message, status.value(), data);
    }

    public static Result error(String message, HttpStatus status) {
        return new Result(message, status.value());
    }

    /**
     * 检查当前用户 是否包含其中一个角色
     *
     * @param roles
     */
    public static void checkAnyRole(String... roles) {
        List<Boolean> flag = new ArrayList<>();
        Stream.of(roles).forEach(role -> flag.add(SecurityUtils.getSubject().hasRole(role.toLowerCase())));
        if (!flag.contains(true)) {
            String name = SecurityUtils.getSubject().getPrincipal().toString();
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
        Stream.of(permissions).forEach(permission -> flag.add(SecurityUtils.getSubject().isPermitted(permission.toLowerCase())));
        if (!flag.contains(true)) {
            String name = SecurityUtils.getSubject().getPrincipal().toString();
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
        RequestUtils.checkAnyPermission(permission);
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

    public static void checkOperation(String operation, BaseEntity entity) {
        if (entity instanceof SystemEntity) {
            SystemEntity systemEntity = (SystemEntity) entity;
            if (systemEntity.isSystemEntity() && !getCurrentUser().isSuperUser()) {
                throw new UnsupportedOperationException("您无权操作系统对象");
            }
        }
        checkOperation(entity.entityName(), operation, entity.getId());
    }

    /**
     * 转化为 checkbox列表
     *
     * @param all
     * @param has
     * @param <T>
     * @return
     */
    public static <T extends NamedEntity> List<Map> getCheckedList(Collection<T> all, Collection<T> has) {
        List<Map> list = new ArrayList<>();
        for (T t : all) {
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("id", t.getId());
            entityMap.put("name", t.getName());
//            entityMap.put("groupName", t.getGroupName());
            entityMap.put("checked", has.contains(t));
            list.add(entityMap);
        }

        list.sort(Comparator.comparing(m -> "checked").thenComparing(m -> "groupName"));
        return list;
    }

    /**
     * 转换为 select 列表
     *
     * @param collection
     * @param <T>
     * @return
     */
    public static <T extends NamedEntity> LinkedHashMap<String, String> getSelectMap(Collection<T> collection, LinkedHashMap<String, String> map) {
        ArrayList<T> list = new ArrayList<>(collection);
        list.sort(Comparator.comparing(NamedEntity::getName));
        for (T t : list) {
            map.put(t.getId(), t.getName());
        }
        return map;
    }

    public static <T extends NamedEntity> LinkedHashMap<String, String> getSelectMap(Collection<T> collection) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        return getSelectMap(collection, map);
    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:38
     */
    public String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || TXT_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || TXT_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || TXT_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals(TXT_LOCALHOST)) {
                //根据网卡取本机配置的IP
                InetAddress inet;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        int l = 15;
        if (ip != null && ip.length() > l) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}
