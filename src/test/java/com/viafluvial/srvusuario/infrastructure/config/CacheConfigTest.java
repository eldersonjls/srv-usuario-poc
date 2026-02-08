package com.viafluvial.srvusuario.infrastructure.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Config: CacheConfig")
class CacheConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withUserConfiguration(CacheConfig.class);

    @Test
    @DisplayName("Deve criar CacheManager do tipo Caffeine e expor caches esperados")
    void shouldCreateCaffeineCacheManagerWithNamedCaches() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(CacheManager.class);

            CacheManager cacheManager = context.getBean(CacheManager.class);
            assertThat(cacheManager).isInstanceOf(CaffeineCacheManager.class);

            Cache users = cacheManager.getCache(CacheConfig.USERS_CACHE);
            Cache byEmail = cacheManager.getCache(CacheConfig.USER_BY_EMAIL_CACHE);
            Cache boatmen = cacheManager.getCache(CacheConfig.BOATMEN_CACHE);
            Cache passengers = cacheManager.getCache(CacheConfig.PASSENGERS_CACHE);

            assertThat(users).isNotNull();
            assertThat(byEmail).isNotNull();
            assertThat(boatmen).isNotNull();
            assertThat(passengers).isNotNull();

            users.put("k", "v");
            assertThat(users.get("k", String.class)).isEqualTo("v");
        });
    }
}
