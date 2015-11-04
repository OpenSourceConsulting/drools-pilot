package com.nicecredit.pilot.rule;

import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.bong.testproject.Message;
import com.nicecredit.pilot.util.Utils;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class TestRuleExecutor implements RuleExecutor{
	
	private KieContainer kContainer;

	public TestRuleExecutor() {
		KieServices ks = KieServices.Factory.get();
        ReleaseId releaseId = ks.newReleaseId( "com.bong", "testProject", "1.0.2-SNAPSHOT" );
        kContainer = ks.newKieContainer( releaseId );
        KieScanner kScanner = ks.newKieScanner( kContainer );

        // Start the KieScanner polling the Maven repository every 10 seconds
        kScanner.start( 10000L );
	}
	
	public void execute(Map<String, Object> teleMap) {
		KieSession kSession = kContainer.newKieSession("defaultKieSession");
		
        Message message = new Message();
        message.setMessage(Utils.getTeleCode(teleMap));
        message.setStatus(Message.HELLO);
        
        kSession.insert(message);
        
        
        kSession.startProcess("testProject.PilotProcess");
        kSession.fireAllRules();
        
        System.out.println("-----------");
        System.out.println("result: " + message.getStatus() + ", " + message.getMessage());
	}

}
//end of RuleExecutor.java