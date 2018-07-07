package com.caolei.system.utils;


import com.caolei.system.pojo.User;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 加密工具
 *
 * @author cloud0072
 * @date 2018/6/12 22:37
 */
@Component
public class EncryptUtils {

    @Value("${shiro.hash.salt}")
    private String hash_salt;
    @Value("${shiro.hash.iterations}")
    private Integer hash_iterations;

    private static EncryptUtils instance;

    private EncryptUtils() {
        instance = this;
    }


    /**
     * 加密
     *
     * @param user
     * @author cloud0072
     * @date 2018/6/12 22:37
     */
    public User encrypt(User user) {
        if (StringUtils.isEmpty(user.getSalt())) {
            user.setSalt(instance.hash_salt);
        }
        user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt(), instance.hash_iterations).toString());
        return user;
    }

    public String getHash_salt() {
        return hash_salt;
    }

    public void setHash_salt(String hash_salt) {
        this.hash_salt = hash_salt;
    }

    public Integer getHash_iterations() {
        return hash_iterations;
    }

    public void setHash_iterations(Integer hash_iterations) {
        this.hash_iterations = hash_iterations;
    }

    public static EncryptUtils getInstance() {
        return instance;
    }
}
