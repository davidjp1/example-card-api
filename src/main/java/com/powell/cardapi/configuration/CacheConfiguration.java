package com.powell.cardapi.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {
    public static final String IMAGE_CACHE = "cardImageCache";

    @Bean
    public CacheManager cacheManager(
            @Value("${cache.timeoutInMins:20}") int timeout,
            @Value("${cache.maxSize:1000}") int maxSize
    ) {
        CaffeineCacheManager manager = new CaffeineCacheManager(IMAGE_CACHE);
        manager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(timeout, TimeUnit.MINUTES)
                .maximumSize(maxSize)
                .softValues());
        return manager;
    }

}
