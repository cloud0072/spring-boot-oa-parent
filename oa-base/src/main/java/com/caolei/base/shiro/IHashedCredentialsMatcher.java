package com.caolei.base.shiro;

import com.caolei.common.autoconfig.Shiro;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自定义登陆验证器，验证密码错误次数,需要缓存
 */
@Component
public class IHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private Shiro shiro;
    private Cache<String, RetryCountAndTime> passwordRetryCache;

    @Autowired
    public IHashedCredentialsMatcher(EhCacheManager ehCacheManager, Shiro shiro) {
        this.shiro = shiro;
        passwordRetryCache = ehCacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        RetryCountAndTime retry = passwordRetryCache.get(username);
        // 设置重试时间和次数
        if (retry == null) {
            retry = new RetryCountAndTime(shiro);
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