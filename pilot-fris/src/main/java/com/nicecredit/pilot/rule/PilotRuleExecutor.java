package com.nicecredit.pilot.rule;

import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.logger.KnowledgeRuntimeLogger;
import org.kie.internal.logger.KnowledgeRuntimeLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.FBApplPhone;
import com.nice.pilot.pilot_rule.InMemData;
import com.nice.pilot.pilot_rule.Result1;
import com.nicecredit.pilot.db.TestResult;
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
	//private KnowledgeRuntimeLogger logger;

	public PilotRuleExecutor() {
		KieServices ks = KieServices.Factory.get();
		ReleaseId releaseId = ks.newReleaseId( "com.nice.pilot", "pilot-rule", "1.0.0-SNAPSHOT" );
        kContainer = ks.newKieContainer( releaseId );
        //KieScanner kScanner = ks.newKieScanner( kContainer );

        //Start the KieScanner polling the Maven repository every 10 seconds
        //kScanner.start( 10000L );
        kSession = kContainer.newKieSession("newSession");
        //logger = KnowledgeRuntimeLoggerFactory.newFileLogger(kSession, "test");
	}
	
	public Object execute(Map<String, Object> teleMap) {
		//KieSession kSession = kContainer.newKieSession("defaultKieSession");
		
        InMemData memData = new InMemData();
        memData.setNR00101037(8);//PI등급
        
        Result1 result = new Result1();
        
        FBApplAddr addr = (FBApplAddr)teleMap.get(Utils.KEY_FBAPPLADDR);
        FBApplPhone wire = (FBApplPhone)teleMap.get(Utils.KEY_FBAPPL_WPHONE);
                
        kSession.insert(memData);
        kSession.insert(result);
        kSession.insert(addr);
        kSession.insert(wire);
        
        kSession.startProcess("PilotRule.RuleFlow1");
        kSession.fireAllRules();
        
        LOGGER.debug("------- result -----");
        LOGGER.debug("전문코드: {}, result1: {}, result2: {}, result3: {}", Utils.getTeleCode(teleMap), addr.getResp_cd(), wire.getResp_cd(), result.getResult1());
        
        if (TestResult.RESP_CD_ER02.equals(addr.getResp_cd())) {
        	result.setResp_cd(TestResult.RESP_CD_ER02);
		} else if (TestResult.RESP_CD_ER03.equals(addr.getResp_cd())) {
        	result.setResp_cd(TestResult.RESP_CD_ER03);
		} else {
			result.setResp_cd(TestResult.RESP_CD_0000);
		}
        
        return result;
	}

}
//end of RuleExecutor.java