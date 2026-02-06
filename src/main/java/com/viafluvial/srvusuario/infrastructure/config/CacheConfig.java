package com.viafluvial.srvusuario.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuração de cache usando Caffeine.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Define os nomes dos caches utilizados na aplicação.
     */
    public static final String USERS_CACHE = "users";
    public static final String USER_BY_EMAIL_CACHE = "usersByEmail";
    public static final String BOATMEN_CACHE = "boatmen";
    public static final String PASSENGERS_CACHE = "passengers";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
            USERS_CACHE,
            USER_BY_EMAIL_CACHE,
            BOATMEN_CACHE,
            PASSENGERS_CACHE
        );
        
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
            .initialCapacity(100)
            .maximumSize(500)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .recordStats();
    }
}
