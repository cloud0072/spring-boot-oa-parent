package com.caolei.base.config;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @ClassName: EhcacheConfig
 * @Description: TODO
 * @author caolei
 * @date 2018/9/12 16:44
 */
@Configuration
public class EhcacheConfig {

    @Value("${spring.cache.ehcache.config}")
    private String ehcacheConfig;

    @Bean
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile(ehcacheConfig);
        return cacheManager;
    }

}
