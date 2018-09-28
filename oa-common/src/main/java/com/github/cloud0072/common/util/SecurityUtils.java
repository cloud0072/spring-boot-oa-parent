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
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null) {
            throw new AuthenticationServiceException("无法获取认证信息!");
        }
        return authentication;
    }

    public static Collection<? extends GrantedAuthority> authorities() {
        return authentication().getAuthorities();
    }

    public static boolean isAuthenticated() {
        try {
            return authentication().isAuthenticated() &&
                    !"anonymousUser".equals(authentication().getPrincipal());
        } catch (Exception e) {
            return false;
        }
    }


}
