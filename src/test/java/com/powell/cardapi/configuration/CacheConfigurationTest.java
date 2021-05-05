package com.powell.cardapi.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cache.CacheManager;

import java.util.concurrent.TimeUnit;

import static com.powell.cardapi.configuration.CacheConfiguration.IMAGE_CACHE;
import static org.assertj.core.api.Assertions.assertThat;

public class CacheConfigurationTest {

    @Test
    public void testCacheConfiguredAsExpected() {
        int timeout = 50;
        int maxSize = 10000;
        new ApplicationContextRunner()
                .withPropertyValues(
                        "cache.timeoutInMins=" + timeout,
                        "cache.maxSize=" + maxSize
                )
                .withUserConfiguration(CacheConfiguration.class).run(ctx -> {
            CacheManager manager = ctx.getBean(CacheManager.class);

            assertThat(manager.getCache(IMAGE_CACHE)).isNotNull();
            assertThat(manager).extracting("cacheBuilder").usingRecursiveComparison().isEqualTo(Caffeine.newBuilder()
                    .expireAfterWrite(timeout, TimeUnit.MINUTES)
                    .maximumSize(maxSize)
                    .softValues());
        });
    }


}