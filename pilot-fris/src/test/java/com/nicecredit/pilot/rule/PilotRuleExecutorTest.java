package com.nicecredit.pilot.rule;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nicecredit.pilot.rule.PilotRuleExecutor;
import com.nicecredit.pilot.rule.RuleExecutor;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class PilotRuleExecutorTest {

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
		RuleExecutor ruleExec = new PilotRuleExecutor();
		
		try {
			ruleExec.execute("8");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

}
//end of RuleExecutorTest.java