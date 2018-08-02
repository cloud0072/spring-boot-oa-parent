package com.caolei.system.utils;


import com.caolei.system.api.BaseEntity;
import com.caolei.system.api.LoggerEntity;
import com.caolei.system.api.SystemEntity;
import com.caolei.system.constant.Constants;
import com.caolei.system.constant.Operation;
import com.caolei.system.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.Session;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static com.caolei.system.constant.Constants.TXT_LOCALHOST;
import static com.caolei.system.constant.Constants.TXT_UNKNOWN;

/**
 * 请求工具类
 *
 * @author cloud0072
 * @date 2018/6/12 22:38
 */
public class RequestUtils extends LoggerEntity {

    private RequestUtils() {
    }

    /**
     * 判断shiro是否可用
     * @return
     */
    public static boolean isSubjectAvailable() {
        try {
            SecurityUtils.getSubject();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取shiro会话
     * @return
     */
    public static Session getSession() {
        try {
            return SecurityUtils.getSubject().getSession();
        } catch (UnavailableSecurityManagerException e) {
            throw new UnsupportedOperationException("无法获取当前SHIRO会话!");
        }
    }

    /**
     * 获取普通Http会话
     * @return
     */
    public static HttpSession getHttpSession() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        } catch (Exception e) {
            throw new UnsupportedOperationException("无法获取当前HTTP会话!");
        }
    }

    public static String getSessionId() {
        return getHttpSession().getId();
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
     * 下载文件
     * @param file
     * @param fileName
     * @return
     */
    public static ResponseEntity<FileSystemResource> downloadFile(File file, String fileName) {
        if (file == null) {
            throw new UnsupportedOperationException("无法读取要下载的文件!");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(file));
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
     * 获取IP地址
     *
     * @param request
     * @return
     * @author cloud0072
     * @date 2018/6/12 22:38
     */
    public static String IPAddress(HttpServletRequest request) {
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
     * 返回消息实体
     *
     * @param
     */
    public static class Result<T> {

        private String message;
        private int code;
        private T data;

        Result(String message, int code) {
            this(message, code, null);
        }

        Result(String message, int code, T data) {
            this.message = message;
            this.code = code;
            this.data = data;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
