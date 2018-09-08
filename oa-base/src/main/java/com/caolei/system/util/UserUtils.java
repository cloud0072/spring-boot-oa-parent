package com.caolei.system.util;

import com.caolei.common.api.entity.BaseEntity;
import com.caolei.common.api.entity.SystemEntity;
import com.caolei.common.constant.Constants;
import com.caolei.common.util.HttpUtils;
import com.caolei.common.util.SecurityUtils;
import com.caolei.system.pojo.User;
import org.apache.shiro.authz.UnauthorizedException;
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

    public static void checkAdmin(BaseEntity entity, String operation) {
        if (entity instanceof SystemEntity) {
            SystemEntity systemEntity = (SystemEntity) entity;
            if (systemEntity.isSystemEntity() && !getCurrentUser().isSuperUser()) {
                throw new UnauthorizedException("您无权操作系统对象");
            }
        }
        SecurityUtils.checkOperation(entity.entityPath(), operation, entity.getId());
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

    @Value("${plugin.shiro.hash-iterations}")
    private void setHashIterations(Integer hashIterations) {
        HASH_ITERATIONS = hashIterations;
    }
}
