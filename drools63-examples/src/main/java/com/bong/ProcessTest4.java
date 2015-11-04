package com.bong;

import java.util.Map;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

import com.nice.pilot.pilot_rule.CEPOrgIDEvent;
import com.nice.pilot.pilot_rule.FBApplAddr;
import com.nice.pilot.pilot_rule.InMemData;
import com.nice.pilot.pilot_rule.Result1;

/**
 * 
 */
public class ProcessTest4 {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	
    	    KieSession kSession = kContainer.newKieSession("ksession-process3");
        	//EntryPoint entryPoint1 = kSession.getEntryPoint("entryone");
        	
        	
    	    execute(kSession, "111");
    	    execute(kSession, "111");
    	    execute(kSession, "222");
        	
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    public static void execute(KieSession kSession, String store_cd) {
		//KieSession kSession = kContainer.newKieSession("defaultKieSession");
		
        InMemData memData = new InMemData();
        memData.setNR00101037(8);//PI등급
        
        Result1 result = new Result1();
        
        FBApplAddr orgEvent = new FBApplAddr();
        orgEvent.setOrg_id("org111");
        orgEvent.setStore_cd(store_cd);
        
        kSession.insert(memData);
        kSession.insert(result);
        kSession.insert(orgEvent);
        
        //kSession.startProcess("PilotRule.RuleFlow1");
        kSession.fireAllRules();
        
        System.out.println("------- result -----");
        System.out.println("result: " + result.getResult1());
        System.out.println("--------------------");
	}

}
