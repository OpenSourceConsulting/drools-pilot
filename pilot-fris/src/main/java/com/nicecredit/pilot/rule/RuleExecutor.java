package com.nicecredit.pilot.rule;

import java.util.Map;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public interface RuleExecutor {
	Object execute(Map<String, Object> teleMap);
}
//end of RuleExecutor.java