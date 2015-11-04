package com.nicecredit.pilot.rule;

import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.CEPOrgIDEvent;
import com.nice.pilot.pilot_rule.InMemData;
import com.nice.pilot.pilot_rule.Result1;
import com.nicecredit.pilot.util.Utils;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class PilotRuleExecutor implements RuleExecutor{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PilotRuleExecutor.class);
	
	private KieContainer kContainer;
	private KieSession kSession;

	public PilotRuleExecutor() {
		KieServices ks = KieServices.Factory.get();
		ReleaseId releaseId = ks.newReleaseId( "com.nice.pilot", "pilot-rule", "1.0.0-SNAPSHOT" );
        kContainer = ks.newKieContainer( releaseId );
        KieScanner kScanner = ks.newKieScanner( kContainer );

        // Start the KieScanner polling the Maven repository every 10 seconds
        kScanner.start( 10000L );
        kSession = kContainer.newKieSession("newSession");
	}
	
	public void execute(Map<String, Object> teleMap) {
		//KieSession kSession = kContainer.newKieSession("defaultKieSession");
		
        InMemData memData = new InMemData();
        memData.setNR00101037(8);//PI등급
        
        Result1 result = new Result1();
                
        kSession.insert(memData);
        kSession.insert(result);
        kSession.insert(teleMap.get(Utils.KEY_FBAPPLADDR));
        
        kSession.startProcess("PilotRule.RuleFlow1");
        kSession.fireAllRules();
        
        LOGGER.debug("------- result -----");
        LOGGER.debug("전문코드: {}, result: {}", Utils.getTeleCode(teleMap), result.getResult1());
	}

}
//end of RuleExecutor.java