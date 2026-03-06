package py.com.cotip.insfrastructure.config.cache;

import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @ConditionalOnProperty(value = "cotip.cache.provider", havingValue = "redis", matchIfMissing = true)
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory,
                                          @Value("${cotip.cache.redis.key-prefix:cotip:v1:}") String keyPrefix,
                                          @Value("${cotip.cache.ttl.default:PT5M}") Duration defaultTtl,
                                          @Value("${cotip.cache.ttl.continental-bank:PT5M}") Duration continentalTtl,
                                          @Value("${cotip.cache.ttl.gnb-bank:PT5M}") Duration gnbTtl,
                                          @Value("${cotip.cache.ttl.maxi-exchange:PT2M}") Duration maxiTtl,
                                          @Value("${cotip.cache.ttl.chaco-exchange:PT1M}") Duration chacoTtl,
                                          @Value("${cotip.cache.ttl.chaco-branches:PT12H}") Duration chacoBranchesTtl) {
        RedisSerializer<String> keySerializer = new StringRedisSerializer();
        RedisSerializer<Object> valueSerializer = new GenericJackson2JsonRedisSerializer();

        String normalizedPrefix = normalizePrefix(keyPrefix);
        RedisCacheConfiguration defaultConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(defaultTtl)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> normalizedPrefix + cacheName + "::");

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("continental-bank", defaultConfiguration.entryTtl(continentalTtl));
        cacheConfigurations.put("gnb-bank", defaultConfiguration.entryTtl(gnbTtl));
        cacheConfigurations.put("maxi-exchange", defaultConfiguration.entryTtl(maxiTtl));
        cacheConfigurations.put("chaco-exchange", defaultConfiguration.entryTtl(chacoTtl));
        cacheConfigurations.put("chaco-branches", defaultConfiguration.entryTtl(chacoBranchesTtl));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }

    @Bean
    @ConditionalOnProperty(value = "cotip.cache.provider", havingValue = "ehcache")
    public CacheManager ehcacheCacheManager() throws Exception {
        CachingProvider provider = Caching.getCachingProvider(EhcacheCachingProvider.class.getName());
        javax.cache.CacheManager cacheManager = provider.getCacheManager(
                getClass().getResource("/ehcache.xml").toURI(),
                getClass().getClassLoader()
        );

        return new JCacheCacheManager(cacheManager);
    }

    private String normalizePrefix(String keyPrefix) {
        if (keyPrefix == null || keyPrefix.isBlank()) {
            return "cotip:";
        }

        String normalized = keyPrefix.trim();
        if (!normalized.endsWith(":")) {
            normalized = normalized + ":";
        }
        return normalized;
    }

}
