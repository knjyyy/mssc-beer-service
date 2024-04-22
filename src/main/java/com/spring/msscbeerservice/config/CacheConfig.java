package com.spring.msscbeerservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
@Slf4j
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        log.trace("Creating cache manager.");
        return new ConcurrentMapCacheManager("myCache");
    }
}
