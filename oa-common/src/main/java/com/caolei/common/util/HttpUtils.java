package com.caolei.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import static com.caolei.common.constant.Constants.TXT_LOCALHOST;
import static com.caolei.common.constant.Constants.TXT_UNKNOWN;

@Slf4j
public class HttpUtils {

    private static String port;
    private static String context_path;

    private HttpUtils() {
    }

    /**
     * 获取当前Http请求
     *
     * @return
     */
    public static HttpServletRequest httpServletRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest();
        } catch (Exception e) {
            log.error("无法获取当前HTTP请求");
            throw new UnsupportedOperationException("无法获取当前HTTP请求!");
        }
    }

    /**
     * 获取普通Http会话
     *
     * @return
     */
    public static HttpSession httpSession() {
        return httpServletRequest().getSession();
    }

    /**
     * 获取shiro会话
     *
     * @return
     */
    public static Session shiroSession() {
        try {
            return SecurityUtils.getSubject().getSession();
        } catch (UnavailableSecurityManagerException e) {
            log.error("无法获取当前SHIRO会话");
            throw new UnsupportedOperationException("无法获取当前SHIRO会话!");
        }
    }

    /**
     * 获取服务器地址
     *
     * @return
     */
    public static String serverAddress() {
        HttpServletRequest request = httpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(),
                url.length()).append(request.getServletContext().getContextPath()).append("/").toString();
    }

    /**
     * 下载文件
     *
     * @param file
     * @param fileName
     * @param contentType
     * @return
     */
    public static ResponseEntity<FileSystemResource> downloadFile(File file, String fileName, String contentType) {
        if (file == null) {
            throw new UnsupportedOperationException("无法读取要下载的文件!");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        MediaType mediaType = contentType == null ? MediaType.APPLICATION_OCTET_STREAM :
                MediaType.valueOf(contentType);

        log.info("download: {}\ttime: {}", fileName, DateUtils.defaultDateFormat(new Date()));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(mediaType)
                .body(new FileSystemResource(file));
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

    /******************************************************************************************************************/

    public static String getPort() {
        return port;
    }

    @Value("${server.port}")
    public void setPort(String port) {
        HttpUtils.port = port;
    }

    public static String getContext_path() {
        return context_path;
    }

    @Value("${server.servlet.context-path}")
    public void setContext_path(String context_path) {
        HttpUtils.context_path = context_path;
    }


}
