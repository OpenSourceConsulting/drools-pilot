/* 
 *
 * Revision History
 * Author             Date              Description
 * ---------------	----------------	------------
 * Jerry Jeong	       2015. 12. 2.		    First Draft.
 */

package com.osc;

import java.io.IOException;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryCacheTest.class);
	
	public static void main(String[] args) {
		Cache<String, String> c = null;
		DefaultCacheManager mgr = null;
		try {
			mgr = new DefaultCacheManager("infinispan-hibernate.xml");
			
			LOGGER.info("Cache Names: " + mgr.getDefinedCacheNames());
			
			Cache<String, String> ccc = mgr.getCache("com.osc.entity.FBAPPLADDR");
			LOGGER.info("Cache Size: " + ccc.size());
			LOGGER.info("Cache Name: " + ccc.getName());
			LOGGER.info("Cache Version: " + ccc.getVersion());
			LOGGER.info("Cache Status: " + ccc.getStatus());
			
			System.exit(0);
			
			c = mgr.getCache("test");
			
			for(int i = 0; i < 10000; i++) {
				String key = "name" + i;
				String value = c.get(key);
				if(value == null) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					c.put(key, "value" + i);
					LOGGER.info(key);
				}
				else {
					LOGGER.info("Skip " + key);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(c != null) c.stop();
			if(mgr != null) mgr.stop();
		}
	}
}
//end of InfinispanTest.java