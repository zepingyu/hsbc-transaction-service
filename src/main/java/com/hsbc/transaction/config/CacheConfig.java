package com.hsbc.transaction.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author zpyu
 */
@Configuration
@EnableCaching
public class CacheConfig {
    @Bean("simpleCacheManager")
    public SimpleCacheManager simpleCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        //Caffeine配置-单条缓存
        CaffeineCache transaction = new CaffeineCache("transaction", Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
                .expireAfterWrite(60, TimeUnit.SECONDS).build());

        //Caffeine配置-多条分页缓存
        CaffeineCache transactionPage = new CaffeineCache("transactionPage", Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(60 * 3, TimeUnit.SECONDS).build());
        cacheManager.setCaches(Arrays.asList(transaction, transactionPage));
        return cacheManager;
    }
}
