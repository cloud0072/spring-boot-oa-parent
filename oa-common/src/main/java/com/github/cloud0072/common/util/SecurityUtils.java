package com.github.cloud0072.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * @ClassName: SecurityUtils
 * @Description: TODO
 * @author caolei
 * @date 2018/9/27 16:49
 */
@Slf4j
public class SecurityUtils {

    public static Authentication authentication() {
        try {
            return SecurityContextHolder
                    .getContext()
                    .getAuthentication();
        } catch (Exception e) {
            throw new AuthenticationServiceException("无法获取认证信息!");
        }
    }

    public static Collection<? extends GrantedAuthority> authorities() {
        return authentication()
                .getAuthorities();
    }

    public static boolean isAuthenticated() {
        return authentication()
                .isAuthenticated();
    }


}
