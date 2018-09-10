package com.caolei.base.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: PluginProperties
 * @Description: 插件配置信息
 * @author caolei
 * @date 2018/9/10 11:29
 */
@Component
@ConfigurationProperties(prefix = "plugin")
public class PluginProperties {
    /**
     * swagger2插件配置
     */
    private Swagger2 swagger2 = new Swagger2();
    /**
     * shiro插件配置
     */
    private Shiro shiro = new Shiro();

    public Swagger2 getSwagger2() {
        return swagger2;
    }

    public void setSwagger2(Swagger2 swagger2) {
        this.swagger2 = swagger2;
    }

    public Shiro getShiro() {
        return shiro;
    }

    public void setShiro(Shiro shiro) {
        this.shiro = shiro;
    }

    public static class Swagger2 {
        // 是否显示文档
        private boolean show;

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }
    }

    public static class Shiro {
        // cookie加密安全码
        private String cipherKey = "4028c081645b411c01645b4127c40026";
        // 加密方式
        private String hashAlgorithmName = "SHA-256";
        // 系统登录重试次数
        private int loginRetryTimes = 5;
        // 加密次数
        private int hashIterations = 100;
        // 600 秒 重置登陆次数时间 最大为 30分钟1800
        private int lockExpiredTime = 600;
        // 259200 秒 - 30 天 记住我功能过期时间
        private int rememberMeAge = 259200;

        public String getCipherKey() {
            return cipherKey;
        }

        public void setCipherKey(String cipherKey) {
            this.cipherKey = cipherKey;
        }

        public String getHashAlgorithmName() {
            return hashAlgorithmName;
        }

        public void setHashAlgorithmName(String hashAlgorithmName) {
            this.hashAlgorithmName = hashAlgorithmName;
        }

        public int getLoginRetryTimes() {
            return loginRetryTimes;
        }

        public void setLoginRetryTimes(int loginRetryTimes) {
            this.loginRetryTimes = loginRetryTimes;
        }

        public int getHashIterations() {
            return hashIterations;
        }

        public void setHashIterations(int hashIterations) {
            this.hashIterations = hashIterations;
        }

        public int getLockExpiredTime() {
            return lockExpiredTime;
        }

        public void setLockExpiredTime(int lockExpiredTime) {
            this.lockExpiredTime = lockExpiredTime;
        }

        public int getRememberMeAge() {
            return rememberMeAge;
        }

        public void setRememberMeAge(int rememberMeAge) {
            this.rememberMeAge = rememberMeAge;
        }
    }


}
