package com.nicecredit.pilot.cache;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nice.pilot.pilot_rule.InMemData;

public class InfinispanHandlerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPut() {
		
		/*
		 * Infinispan 연동 테스트.
		 */
		String key = "key1";
		String org_id = "1111";
		InMemData value = new InMemData();
		value.setOrg_id(org_id);
		try {
			InfinispanHandler memHandler = InfinispanHandler.getInstance();
			memHandler.put(key, value);
			
			Object cacheValue = memHandler.get(key);
			assertTrue(cacheValue instanceof InMemData);
			assertEquals(org_id, ((InMemData)cacheValue).getOrg_id());
			
			memHandler.remove(key);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		} 
	}
	
	@Test
	public void testKeys() {
		
		try {
			InfinispanHandler memHandler = InfinispanHandler.getInstance();
			
			System.out.println(memHandler.keys().size());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		} 
	}

}
//end of InfinispanHandlerTest.java