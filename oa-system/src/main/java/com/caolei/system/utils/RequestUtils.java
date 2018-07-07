package com.caolei.system.utils;


import com.caolei.system.constant.Constants;
import com.caolei.system.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public static User getCurrentUser() {
        return (User) SecurityUtils.getSubject().getSession().getAttribute(Constants.USER_INFO);
    }

    public static void setCurrentUser(User user) {
        SecurityUtils.getSubject().getSession().setAttribute(Constants.USER_INFO, user);
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
        Stream.of(roles).forEach(role ->
                flag.add(SecurityUtils.getSubject().hasRole(role)));
        if (!flag.contains(true)) {
            String name = SecurityUtils.getSubject().getPrincipal().toString();
            throw new AuthorizationException(name + " do not has role " + Arrays.asList(roles));
        }
    }

    public static void checkAnyPermission(String... permissions) {
        List<Boolean> flag = new ArrayList<>();
        Stream.of(permissions).forEach(permission ->
                flag.add(SecurityUtils.getSubject().isPermitted(permission)));
        if (!flag.contains(true)) {
            String name = SecurityUtils.getSubject().getPrincipal().toString();
            throw new AuthorizationException(name + " do not has permission " + Arrays.asList(permissions));
        }
    }
}
