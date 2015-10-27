package com.nicecredit.pilot;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class RuleExecutorTest {

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("kie.maven.settings.custom", "D:\\project\\2014_hhi\\.m2\\settings.xml");
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecute() {
		RuleExecutor ruleExec = new RuleExecutor();
		
		try {
			ruleExec.execute("hello junit");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

}
//end of RuleExecutorTest.java