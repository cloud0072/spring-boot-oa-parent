package com.github.cloud0072.common.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author caolei
 * @ClassName: ShiroProperties
 * @Description: shiro插件配置
 * @date 2018/9/11 21:28
 */
@ConfigurationProperties(prefix = "plugin.shiro")
@Component
@Getter
@Setter
public class ShiroProperties{
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
}
