package com.nicecredit.pilot.rule;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nicecredit.pilot.rule.PilotRuleExecutor;
import com.nicecredit.pilot.rule.RuleExecutor;
import com.nicecredit.pilot.util.Utils;

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
		
		PilotRuleExecutor ruleExec = new PilotRuleExecutor();
		
		Map<String, Object> teleMap = new HashMap<String, Object>();
		
		FBApplAddr addr = new FBApplAddr();
		addr.setOrg_id("org111");
		addr.setStore_cd("store111");
		
		teleMap.put("teleCode", "junitCode");
		teleMap.put(Utils.KEY_FBAPPLADDR, addr);
		try {
			ruleExec.execute(teleMap);
			
			addr = new FBApplAddr();
			addr.setOrg_id("org111");
			addr.setStore_cd("store111");
			teleMap.put(Utils.KEY_FBAPPLADDR, addr);
			
			ruleExec.execute(teleMap);
			
			addr = new FBApplAddr();
			addr.setOrg_id("org111");
			addr.setStore_cd("store222");
			teleMap.put(Utils.KEY_FBAPPLADDR, addr);
			
			ruleExec.execute(teleMap);
			
			addr = new FBApplAddr();
			addr.setOrg_id("org111");
			addr.setStore_cd("store333");
			teleMap.put(Utils.KEY_FBAPPLADDR, addr);
			
			ruleExec.execute(teleMap);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
		
	}

}
//end of RuleExecutorTest.java