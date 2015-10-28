package com.nicecredit.pilot.cache;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
		String value = "val11";
		try {
			InfinispanHandler memHandler = new InfinispanHandler();
			memHandler.put(key, value);
			
			assertEquals(value, memHandler.get(key));
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

}
//end of InfinispanHandlerTest.java