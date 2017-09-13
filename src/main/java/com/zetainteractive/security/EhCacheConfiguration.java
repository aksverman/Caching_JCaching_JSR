package com.zetainteractive.security;



import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching
public class EhCacheConfiguration {

	
	@Bean
	public JCacheCacheManager jCacheManager() {
	   return new JCacheCacheManager(cacheManager());
	}
	
	public CacheManager	cacheManager() {
		String providerName = "org.ehcache.jcache.JCacheCachingProvider";
		CachingProvider cachingProvider = Caching.getCachingProvider(providerName);
		CacheManager cacheManager = cachingProvider.getCacheManager();
		return cacheManager;
	}
	
	/**
	 * this cacheManager is for creating cache programatically
	 * In case we don't have "ehcache.xml", 
	 */
	/*
	public CacheManager cacheManager() {
		
		String cacheName = "sampleCache";
		String providerName = "org.ehcache.jcache.JCacheCachingProvider";
		CachingProvider cachingProvider = Caching.getCachingProvider(providerName);
		CacheManager cacheManager = cachingProvider.getCacheManager();
		MutableConfiguration<Object, Object> configuration =
		        new MutableConfiguration<Object, Object>()  
		            .setTypes(Object.class, Object.class)   
		            .setStoreByValue(false)   
		            .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE));
		Cache cache = cacheManager.createCache(cacheName, configuration);
		return cacheManager;
	}*/
	/*
	 * Spring 3.1 caching configuration 
	 * 
	 */
	/*@Primary
    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
		return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
    }


    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }*/

    /*
     * As using CustomKeyGenerator.java to get customized key at method call 
     */
   /* @Bean(name="customkey")
    public KeyGenerator keyGenerator() {
      return new KeyGenerator() {
       	@Override
		public Object generate(Object obj, Method method, Object... params) {
			StringBuilder sb = new StringBuilder();
	          sb.append(obj.getClass().getName());
	          sb.append(method.getName());
	          
	          for (Object param : params) {
	            sb.append(param.toString());
	          }
	          return sb.toString();
		}
      };
    }*/
}
