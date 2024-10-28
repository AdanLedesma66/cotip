package py.com.cotip.application.config;

import org.ehcache.jsr107.EhcacheCachingProvider;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

@Configuration
@EnableCaching
public class CacheConfig {

    // ::: cache manager

    @Bean
    public JCacheCacheManager cacheManager() throws Exception {
        CachingProvider provider = Caching.getCachingProvider(EhcacheCachingProvider.class.getName());
        CacheManager cacheManager = provider.getCacheManager(getClass().getResource("/ehcache.xml").toURI(), getClass().getClassLoader());

        return new JCacheCacheManager(cacheManager);
    }

}
