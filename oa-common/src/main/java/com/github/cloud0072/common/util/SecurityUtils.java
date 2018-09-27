package com.github.cloud0072.common.util;

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
public class SecurityUtils {

    public static Authentication authentication() {
        try {

        }catch (Exception e){
        }
        return SecurityContextHolder
                .getContext()
                .getAuthentication();
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
