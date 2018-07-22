package com.caolei.system.utils;

import com.caolei.system.pojo.User;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 加密工具
 *
 * @author cloud0072
 * @date 2018/6/12 22:37
 */
@Component
public class EncryptUtils {

    private EncryptUtils() {
    }

    private static Integer HASH_ITERATIONS;

    @Value("${shiro.hash.iterations}")
    private void setHashIterations(Integer hashIterations) {
        HASH_ITERATIONS = hashIterations;
    }

    /**
     * 加密
     *
     * @param user
     * @author cloud0072
     * @date 2018/6/12 22:37
     */
    public static User encrypt(User user) {
        if (StringUtils.isEmpty(user.getSalt())) {
            throw new NullPointerException("用户的加密信息缺失,请确认后重试");
        }
        user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt(), HASH_ITERATIONS).toString());
        return user;
    }

    public static String UUID32() {
        return UUID.randomUUID().toString().toLowerCase().replaceAll("-", "");
    }

    public static Integer getHashIterations() {
        return HASH_ITERATIONS;
    }

}
