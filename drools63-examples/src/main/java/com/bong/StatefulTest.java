package com.bong;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;


public class StatefulTest {


    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
    	    KieSession kSession = kContainer.newKieSession("ksession-stateful");
    	    
            //---------------------- insert Messaage Objects ----------------
            boolean decrese = false;
            for (int i = 0; i < 20; i++) {
            	
            	System.out.println(kSession.getClass().getCanonicalName());
            	
            	Message msg = new Message();
            	
            	msg.setStatus(Message.HELLO);
                
                msg.setMessage(i + " message");
                
                //entryPoint1.insert(msg);
                kSession.insert(msg);
                kSession.insert(new NotEvent("notEvnet"+i));
                
                System.out.println("-------------- inserted msg:" + i);
                
                kSession.fireAllRules();
                
                
                try {
                    Thread.sleep(2000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                //kSession.dispose();
			}

            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
