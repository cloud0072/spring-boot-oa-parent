package com.caolei.system.utils;


import com.caolei.system.api.NamedEntity;
import com.caolei.system.constant.Constants;
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

import static com.caolei.system.constant.Constants.TEXT_LOCALHOST;
import static com.caolei.system.constant.Constants.TEXT_UNKNOWN;

/**
 * 请求工具类
 *
 * @author cloud0072
 * @date 2018/6/12 22:38
 */
public class RequestUtils {

    private static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static User getCurrentUser() {
        return (User) getSession().getAttribute(Constants.USER_INFO);
    }

    public static void setCurrentUser(User user) {
        if (user == null) {
            getSession().removeAttribute(Constants.USER_INFO);
        }
        getSession().setAttribute(Constants.USER_INFO, user);
    }

    public static Result error(String message, HttpStatus status) {
        return new Result(message, status);
    }

    public static <T> Result<T> error(String message, HttpStatus status, T data) {
        return new Result<>(message, status, data);
    }

    public static Result success(String message) {
        return new Result<>(message, HttpStatus.OK);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>("success", HttpStatus.OK, data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(message, HttpStatus.OK, data);
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
        if (ip == null || ip.length() == 0 || TEXT_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || TEXT_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || TEXT_UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals(TEXT_LOCALHOST)) {
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

    public static void checkAnyRole(String... roles) {
        List<Boolean> flag = new ArrayList<>();
        Stream.of(roles).forEach(role -> flag.add(SecurityUtils.getSubject().hasRole(role.toLowerCase())));
        if (!flag.contains(true)) {
            String name = SecurityUtils.getSubject().getPrincipal().toString();
            throw new AuthorizationException(name + " do not has role " + Arrays.asList(roles));
        }
    }

    public static void checkAnyPermission(String... permissions) {
        List<Boolean> flag = new ArrayList<>();
        Stream.of(permissions).forEach(permission -> flag.add(SecurityUtils.getSubject().isPermitted(permission.toLowerCase())));
        if (!flag.contains(true)) {
            String name = SecurityUtils.getSubject().getPrincipal().toString();
            throw new AuthorizationException(name + " do not has permission " + Arrays.asList(permissions));
        }
    }

    public static <T extends NamedEntity> List<Map> getCheckedList(List<T> all, List<T> has) {
        List<Map> list = new ArrayList<>();
        for (T t : all) {
            Map<String, Object> entityMap = new HashMap<>();
            entityMap.put("id", t.getId());
            entityMap.put("name", t.getName());
            entityMap.put("groupName", t.getGroupName());
            entityMap.put("checked", has.contains(t));
            list.add(entityMap);
        }

        list.sort(Comparator.comparing(m -> "checked").thenComparing(m -> "groupName"));
        return list;
    }

    public static <T extends NamedEntity> LinkedHashMap<String, String> getSelectMap(List<T> list) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        return getSelectMap(list, map);
    }

    public static <T extends NamedEntity> LinkedHashMap<String, String> getSelectMap(List<T> list, LinkedHashMap<String, String> map) {
        list.sort(Comparator.comparing(NamedEntity::getGroupName).thenComparing(NamedEntity::getName));
        for (T t : list) {
            map.put(t.getId(), t.getName());
        }
        return map;
    }

}
