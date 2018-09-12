package com.caolei.common.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName: Shiro
 * @Description: shiro插件配置
 * @author caolei
 * @date 2018/9/11 21:28
 */
@ConfigurationProperties(prefix = "plugin.shiro")
@Component
public class Shiro {
    // cookie加密安全码
    private String cipherKey = "4028c081645b411c01645b4127c40026";
    // 加密方式
    private String hashAlgorithmName = "SHA-256";
    // 系统登录重试次数
    private int loginRetryTimes = 0;
    // 加密次数
    private int hashIterations = 100;
    // 600 秒 重置登陆次数时间 最大为 30分钟1800
    private int lockExpiredTime = 60;
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
