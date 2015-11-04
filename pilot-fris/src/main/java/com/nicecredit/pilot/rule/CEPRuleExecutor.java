package com.nicecredit.pilot.rule;

import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nicecredit.pilot.util.Utils;

/**
 * <pre>
 * CEP Rule 처리 클래스.
 * </pre>
 * @author BongJin Kwon
 */
public class CEPRuleExecutor implements RuleExecutor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CEPRuleExecutor.class);
	
	private KieContainer kContainer;

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public CEPRuleExecutor() {
		/*
		KieServices ks = KieServices.Factory.get();
		ReleaseId releaseId = ks.newReleaseId( "com.nice.pilot", "cep-rule", "1.0.0-SNAPSHOT" );
        kContainer = ks.newKieContainer( releaseId );
        KieScanner kScanner = ks.newKieScanner( kContainer );

        // Start the KieScanner polling the Maven repository every 10 seconds
        kScanner.start( 10000L );
        */
	}

	/* (non-Javadoc)
	 * @see com.nicecredit.pilot.rule.RuleExecutor#execute(java.lang.String)
	 */
	@Override
	public void execute(Map<String, Object> teleMap) {
		LOGGER.debug(Utils.getTeleCode(teleMap));

	}

}
//end of CEPRuleExecutor.java