package com.github.cloud0072.base.config.shiro;

import com.github.cloud0072.common.autoconfig.ShiroProperties;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自定义密码凭证验证器，验证密码错误次数,需要缓存
 */
@Component
public class IHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private ShiroProperties shiroProperties;
    private Cache<String, RetryCountAndTime> passwordRetryCache;

    @Autowired
    public IHashedCredentialsMatcher(EhCacheManager ehCacheManager, ShiroProperties shiroProperties) {
        this.shiroProperties = shiroProperties;
        passwordRetryCache = ehCacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        RetryCountAndTime retry = passwordRetryCache.get(username);
        // 设置重试时间和次数
        if (retry == null) {
            retry = new RetryCountAndTime(shiroProperties);
            passwordRetryCache.put(username, retry);
        }
        // 超过限定次数，抛异常
        retry.checkRetryTimes();
        // 判断登录
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            passwordRetryCache.remove(username);
        }
        return matches;
    }
}
