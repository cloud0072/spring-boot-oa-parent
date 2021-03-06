package com.github.cloud0072.base.util;

import com.github.cloud0072.base.model.User;
import com.github.cloud0072.common.autoconfig.ShiroProperties;
import com.github.cloud0072.common.constant.Constants;
import com.github.cloud0072.common.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 用户相关工具
 */
@Slf4j
@Component
public class UserUtils {

    private static ShiroProperties shiroProperties;

    @Autowired
    private UserUtils(ShiroProperties shiroProperties) {
        UserUtils.shiroProperties = shiroProperties;
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
        if (user == null || StringUtils.isEmpty(user.getSalt()) || StringUtils.isEmpty(user.getPassword())) {
            throw new NullPointerException("用户的加密信息缺失,请确认后重试");
        }
        user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt(), shiroProperties.getHashIterations()).toString());
        return user;
    }

    /**
     * 验证密码是否正确
     * @param user
     * @param password
     * @return
     */
    public static boolean checkPwd(User user, String password) {
        if (user == null || StringUtils.isEmpty(user.getSalt()) || StringUtils.isEmpty(user.getPassword())) {
            throw new NullPointerException("用户的加密信息缺失,请确认后重试");
        }
        return user.getPassword().equals(new Sha256Hash(password, user.getSalt(), shiroProperties.getHashIterations()).toString());
    }

}
