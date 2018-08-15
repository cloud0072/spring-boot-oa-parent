package com.caolei.common.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private HttpUtils() {

    }

    /**
     * 获取普通Http会话
     *
     * @return
     */
    public static HttpSession httpSession() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest().getSession();
        } catch (Exception e) {
            logger.error("无法获取当前HTTP会话");
            throw new UnsupportedOperationException("无法获取当前HTTP会话!");
        }
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
            logger.error("无法获取当前SHIRO会话");
            throw new UnsupportedOperationException("无法获取当前SHIRO会话!");
        }
    }

    /**
     * 获取sessionId
     * @return
     */
    public static String sessionId() {
        try {
            return httpSession().getId();
        } catch (Exception e) {
            return "-1";
        }
    }


    /**
     * 下载文件
     *
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
        logger.info("download: {}\ttime: {}", fileName, DateUtils.defaultDateFormat(new Date()));
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
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
}
