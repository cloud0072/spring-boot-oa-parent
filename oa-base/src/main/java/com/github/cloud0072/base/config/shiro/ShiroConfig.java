package com.github.cloud0072.base.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.github.cloud0072.common.autoconfig.ShiroProperties;
import com.github.cloud0072.common.util.StringUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro权限管理器
 *
 * @author cloud0072
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private ShiroProperties shiroProperties;
    @Autowired
    private EhCacheManager ehCacheManager;
    @Autowired
    private IHashedCredentialsMatcher hashedCredentialsMatcher;
    @Value("${spring.h2.console.path}")
    private String h2WebConsole;

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
        hashedCredentialsMatcher.setHashAlgorithmName(shiroProperties.getHashAlgorithmName());
        hashedCredentialsMatcher.setHashIterations(shiroProperties.getHashIterations());
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * 配置自定义的权限登录器
     */
    @Bean
    public AuthorizingRealm defaultRealm() {
        DefaultRealm defaultRealm = new DefaultRealm();
        //设置密码凭证匹配器
        defaultRealm.setCredentialsMatcher(hashedCredentialsMatcher());

        return defaultRealm;
    }

    /**
     * thymeleaf-shiro配置方言
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * cookie对象
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox 的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //记住我cookie生效时间30天（259200） ,单位秒
        simpleCookie.setMaxAge(shiroProperties.getRememberMeAge());
        simpleCookie.setHttpOnly(true);
        return simpleCookie;
    }

    /**
     * 记住我管理器 cookie管理对象;
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        byte[] cipherKey = Base64.decode(shiroProperties.getCipherKey());
        cookieRememberMeManager.setCipherKey(cipherKey);
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * 配置核心安全事务管理器
     * - 设置realm.
     * - 设置rememberMe管理器
     * - 设置ehcache缓存
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(defaultRealm());
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setCacheManager(ehCacheManager);

        return securityManager;
    }

    /**
     * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持; Controller才能使用@RequiresPermissions
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
        //配置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager());

        //配置登录的url和登录成功的url
        shiroFilterFactoryBean.setLoginUrl("/prepare_login");
        shiroFilterFactoryBean.setSuccessUrl("/index");

        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //静态资源
        filterChainDefinitionMap.put("/assets/**", "anon");
        //h2数据库web控制台
        if (!StringUtils.isEmpty(h2WebConsole)) {
            filterChainDefinitionMap.put(h2WebConsole + "/**", "anon");
        }
        //表示可以匿名访问
        filterChainDefinitionMap.put("/prepare_login", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/test/**", "anon");
        //表示需要认证才可以访问
        filterChainDefinitionMap.put("/**", "authc");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        //配置自定义拦截器
        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        shiroFilterFactoryBean.setFilters(filtersMap);

        return shiroFilterFactoryBean;
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
// org.apache.shiro.authz.UnauthenticatedException
//
// 权限异常
// 没有访问权限，访问异常
// org.apache.shiro.authz.HostUnauthorizedException
// 授权异常
// org.apache.shiro.authz.AuthorizationException
// org.apache.shiro.authz.UnauthorizedException
//
// shiro全局异常
// org.apache.shiro.ShiroException

}


 