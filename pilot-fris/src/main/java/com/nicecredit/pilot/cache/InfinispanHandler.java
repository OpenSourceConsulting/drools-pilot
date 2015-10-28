package com.nicecredit.pilot.cache;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author BongJin Kwon
 */
public class InfinispanHandler {
	
	private RemoteCache<String, String> cache;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public InfinispanHandler() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.tcpNoDelay(true).connectionPool()
				.numTestsPerEvictionRun(3)
				.testOnBorrow(false)
				.testOnReturn(false)
				.testWhileIdle(true)
				.addServer()
				.host("localhost").port(11222);
		RemoteCacheManager rmc = new RemoteCacheManager(cb.build());
		cache = rmc.getCache();
	}
	
	public void put(String key, String value) {
		cache.put(key, value);
	}
	
	public String get(String key) {
		return cache.get(key);
	}

}
// end of InfinispanHandler.java