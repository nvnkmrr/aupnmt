package com.aupnmt.configuraion;

import java.util.Collections;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableCaching
public class AupnmtConfiguration {

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		cacheManager.setCaches(Collections.singletonList(createCache()));
		return cacheManager;
	}

	public Cache createCache() {
		Cache cache = new ConcurrentMapCache("default");
		return cache;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
