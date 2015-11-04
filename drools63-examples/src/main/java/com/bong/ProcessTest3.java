package com.bong;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

import com.bong.Message;

/**
 * fireUntilHalt() 사용하지 않은 예시.
 */
public class ProcessTest3 {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	
    	    KieSession kSession = kContainer.newKieSession("ksession-process2");
        	//EntryPoint entryPoint1 = kSession.getEntryPoint("entryone");
        	
        	
        	boolean decrese = false;
            for (int i = 0; i < 10000; i++) {
            	
            	Message msg = new Message();
            	
            	if(i > 9 && decrese) {
            		// HELLO 메시지 감소.
            		msg.setStatus(Message.GOODBYE);
            	} else {
            		msg.setStatus(Message.HELLO);
            	}
                
                msg.setMessage(i + " message");
                //entryPoint1.insert(msg);
                kSession.insert(msg);
                
                kSession.startProcess("com.sample.bpmn.hello");
                kSession.fireAllRules();
                
                System.out.println("inserted msg:" + i);
                try {
                    Thread.sleep(2000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("--------------");
			}
        	
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
