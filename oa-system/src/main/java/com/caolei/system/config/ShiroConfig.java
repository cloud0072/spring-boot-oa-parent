package com.caolei.system.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.caolei.system.api.BaseLogger;
import com.caolei.system.shiro.DefaultRealm;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * shiro权限管理器
 *
 * @author cloud0072
 */
@Configuration
public class ShiroConfig
        implements BaseLogger {

    @Value("${shiro.cipher.key}")
    private String CIPHER_KEY;
    @Value("${shiro.hash.algorithm.name}")
    private String HASH_ALGORITHM_NAME;
    @Value("${shiro.hash.iterations}")
    private Integer HASH_ITERATIONS;
    @Value("${shiro.login.retry.times}")
    private Integer LOGIN_RETRY_TIMES;
    @Value("${shiro.lock.expired.time}")
    private Integer LOCK_EXPIRED_TIME;
    @Value("${shiro.remember.me.age}")
    private Integer REMEMBER_ME_AGE;

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 密码匹配凭证管理器
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new IHashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(HASH_ALGORITHM_NAME);
        hashedCredentialsMatcher.setHashIterations(HASH_ITERATIONS);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * 配置自定义的权限登录器
     */
    @Bean
    public AuthorizingRealm defaultRealm() {
        DefaultRealm defaultRealm = new DefaultRealm();
        /**
         * 设置密码凭证匹配器
         */
        defaultRealm.setCredentialsMatcher(hashedCredentialsMatcher());

        return defaultRealm;
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * cookie对象;
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox 的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //记住我cookie生效时间30天（259200） ,单位秒
        simpleCookie.setMaxAge(REMEMBER_ME_AGE);
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }

    /**
     * 记住我管理器 cookie管理对象;
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        byte[] cipherKey = Base64.decode(CIPHER_KEY);
        cookieRememberMeManager.setCipherKey(cipherKey);
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:config/ehcache.xml");
        return cacheManager;
    }

    /**
     * 配置核心安全事务管理器
     * 设置realm.
     * 设置rememberMe管理器
     * 设置ehcache缓存
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(defaultRealm());
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setCacheManager(ehCacheManager());

        return securityManager;
    }

    /**
     * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持; Controller才能使用@RequiresPermissions
     *
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        //配置登录的url和登录成功的url
        shiroFilterFactoryBean.setLoginUrl("/prepare_login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //静态资源
        filterChainDefinitionMap.put("/bootstrap/**", "anon");
        filterChainDefinitionMap.put("/assets/**", "anon");
        filterChainDefinitionMap.put("/s/**", "anon");
        //表示可以匿名访问
        filterChainDefinitionMap.put("/prepare_login", "anon");
        filterChainDefinitionMap.put("/login", "anon");
//        filterChainDefinitionMap.put("/", "authc");
        //表示需要认证才可以访问
        filterChainDefinitionMap.put("/*", "authc");
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 自定义登陆验证器，验证密码错误次数,需要缓存
     */
    private class IHashedCredentialsMatcher extends HashedCredentialsMatcher {

        private Cache<String, RetryCountAndTime> passwordRetryCache;

        IHashedCredentialsMatcher() {
            passwordRetryCache = ehCacheManager().getCache("passwordRetryCache");
        }

        @Override
        public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
            String username = (String) token.getPrincipal();
            RetryCountAndTime retry = passwordRetryCache.get(username);
            // 设置重试时间和次数
            if (retry == null) {
                retry = new RetryCountAndTime();
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

    private class RetryCountAndTime implements Serializable {
        private AtomicInteger count;
        private long expiredTime;

        RetryCountAndTime() {
            this.count = new AtomicInteger(0);
            this.expiredTime = System.currentTimeMillis() + LOCK_EXPIRED_TIME * 1000;
        }

        void checkRetryTimes() {
            //没有设置重试次数则默认跳过检测
            if (LOGIN_RETRY_TIMES < 1) {
                return;
            }
            //过期重置验证信息
            if (expiredTime < System.currentTimeMillis()) {
                this.count = new AtomicInteger(0);
                this.expiredTime = System.currentTimeMillis() + LOCK_EXPIRED_TIME * 1000;
            }
            if (LOGIN_RETRY_TIMES < count.incrementAndGet()) {
                long second = (expiredTime - System.currentTimeMillis()) / 1000 + 1;
                String timeMessage = second > 60 ? second / 60 + "分钟后重试" : second + "秒后重试";
                throw new ExcessiveAttemptsException("您的登录失败次数过多! 请您在" + timeMessage);
            }
        }
    }

    // shiro常用异常
//
// 身份认证异常
// 身份令牌异常，不支持的身份令牌
// org.apache.shiro.authc.pam.UnsupportedTokenException
//
// 未知账户/没找到帐号,登录失败
// org.apache.shiro.authc.UnknownAccountException
// 帐号锁定
// org.apache.shiro.authc.LockedAccountException
// 用户禁用
// org.apache.shiro.authc.DisabledAccountException
// 登录重试次数，超限。只允许在一段时间内允许有一定数量的认证尝试
// org.apache.shiro.authc.ExcessiveAttemptsException
// 一个用户多次登录异常：不允许多次登录，只能登录一次 。即不允许多处登录
// org.apache.shiro.authc.ConcurrentAccessException
// 账户异常
// org.apache.shiro.authc.AccountException
//
// 过期的凭据异常
// org.apache.shiro.authc.ExpiredCredentialsException
// 错误的凭据异常
// org.apache.shiro.authc.IncorrectCredentialsException
// 凭据异常
// org.apache.shiro.authc.CredentialsException
// org.apache.shiro.authc.AuthenticationException
//
// 权限异常
// 没有访问权限，访问异常
// org.apache.shiro.authz.HostUnauthorizedException
// org.apache.shiro.authz.UnauthorizedException
// 授权异常
// org.apache.shiro.authz.UnauthenticatedException
// org.apache.shiro.authz.AuthorizationException
//
// shiro全局异常
// org.apache.shiro.ShiroException

}


 