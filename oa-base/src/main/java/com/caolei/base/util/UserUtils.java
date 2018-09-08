package com.caolei.base.util;

import com.caolei.base.pojo.User;
import com.caolei.common.constant.Constants;
import com.caolei.common.util.HttpUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 用户相关工具
 */
@Component
public class UserUtils {

    private static Integer HASH_ITERATIONS;

    private UserUtils() {
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public static User getCurrentUser() {
        return (User) HttpUtils.httpSession().getAttribute(Constants.USER_INFO);
    }

    /**
     * 重新设置当前用户属性
     *
     * @param user
     */
    public static void setCurrentUser(User user) {
        if (user == null) {
            HttpUtils.httpSession().removeAttribute(Constants.USER_INFO);
        }
        HttpUtils.httpSession().setAttribute(Constants.USER_INFO, user);
    }

    /**
     * 加密
     *
     * @param user
     * @author cloud0072
     * @date 2018/6/12 22:37
     */
    public static User encrypt(User user) {
        if (StringUtils.isEmpty(user.getSalt()) || StringUtils.isEmpty(user.getPassword())) {
            throw new NullPointerException("用户的加密信息缺失,请确认后重试");
        }
        user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt(), HASH_ITERATIONS).toString());
        return user;
    }

    @Value("${plugin.shiro.hash-iterations}")
    private void setHashIterations(Integer hashIterations) {
        HASH_ITERATIONS = hashIterations;
    }
}
