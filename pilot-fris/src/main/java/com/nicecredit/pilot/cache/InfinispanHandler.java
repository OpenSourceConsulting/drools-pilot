package com.nicecredit.pilot.cache;

import java.io.IOException;
import java.util.Set;

import org.infinispan.Cache;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
//import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.commons.api.BasicCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author BongJin Kwon
 */
public class InfinispanHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InfinispanHandler.class);
	
	private static volatile InfinispanHandler INSTANCE;
	
	private RemoteCacheManager rmc;
	private RemoteCache<String, Object> cache;
	
	//private DefaultCacheManager rmc;
	//private Cache<String, Object> cache;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	private InfinispanHandler() {
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		
		cb.tcpNoDelay(true).connectionPool()
				.numTestsPerEvictionRun(3)
				.testOnBorrow(false)
				.testOnReturn(false)
				.testWhileIdle(true)
				.addServer()
				//.host("23.99.106.81").port(11222);
				.host("nice-osc-db.cloudapp.net").port(11222);
		
		
		try {
			rmc = new RemoteCacheManager(cb.build());
			//rmc = new DefaultCacheManager("infinispan.xml");
			cache = rmc.getCache();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	
	public static InfinispanHandler getInstance() {
		
		if (INSTANCE == null) {
			
			synchronized (InfinispanHandler.class) {
				if (INSTANCE == null) {
					INSTANCE = new InfinispanHandler();
					LOGGER.info("create InfinispanHandler instance.");
				}
			}
		}
		return INSTANCE;
	}
	
	public void put(String key, Object valObj) {
		cache.put(key, valObj);
	}
	
	public Object get(String key) {
		return cache.get(key);
	}
	
	public void remove(String key) {
		cache.remove(key);
	}
	
	public Set<String> keys() {
		return cache.keySet();
	}
	
	public BasicCache<String, Object> getCache() {
		return cache;
	}
	
	public void stop() {
		
		if (INSTANCE != null) {
			
			synchronized (InfinispanHandler.class) {
		
				if (this.cache != null) {
					this.cache.stop();
					this.cache = null;
				}
				
				if (this.rmc != null) {
					this.rmc.stop();
					this.rmc = null;
				}
				
				INSTANCE = null;
				LOGGER.info("stoped!!");
			}
		}
	}

}
// end of InfinispanHandler.java