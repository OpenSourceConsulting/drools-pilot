package com.bong;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

/**
 * This is a sample class to launch a fusion rule.
 * - 6.* 버전에서는 src/main/resources/META-INF/kmodule.xml 를 사용함.
 * - EntryPoint 사용 안해도 됨.
 */
public class DroolsFusionTest {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	final KieSession kSession = kContainer.newKieSession("ksession-fusions");

            
            //---------------------- insert Messaage Objects ----------------
            boolean decrese = false;
            for (int i = 0; i < 20; i++) {
            	
            	Message msg = new Message();
            	
            	msg.setStatus(Message.HELLO);
                
                msg.setMessage(i + " message");
                
                //entryPoint1.insert(msg);
                kSession.insert(msg);
                kSession.insert(new NotEvent("notEvnet"+i));
                kSession.fireAllRules();
                
                System.out.println("-------------- inserted msg:" + i);
                try {
                    Thread.sleep(2000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
				
			}

            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
