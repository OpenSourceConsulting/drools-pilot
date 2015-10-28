package com.nicecredit.pilot.rule;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.InMemData;
import com.nice.pilot.pilot_rule.Result1;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class PilotRuleExecutor implements RuleExecutor{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PilotRuleExecutor.class);
	
	private KieContainer kContainer;

	public PilotRuleExecutor() {
		KieServices ks = KieServices.Factory.get();
		ReleaseId releaseId = ks.newReleaseId( "com.nice.pilot", "pilot-rule", "1.0.0-SNAPSHOT" );
        kContainer = ks.newKieContainer( releaseId );
        KieScanner kScanner = ks.newKieScanner( kContainer );

        // Start the KieScanner polling the Maven repository every 10 seconds
        kScanner.start( 10000L );
	}
	
	public void execute(String msg) {
		KieSession kSession = kContainer.newKieSession("defaultKieSession");
		
        InMemData memData = new InMemData();
        memData.setNR00101037(Integer.parseInt(msg));//PI등급
        
        Result1 result = new Result1();
        
        kSession.insert(memData);
        kSession.insert(result);
        
        kSession.fireAllRules();
        
        LOGGER.debug("------- result -----");
        LOGGER.debug("PI등급: {}, result: {}", msg, result.getResult1());
	}

}
//end of RuleExecutor.java