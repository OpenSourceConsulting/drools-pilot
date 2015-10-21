package com.bong;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;

import com.bong.Message;

/**
 * fireUntilHalt() 사용 예시.
 */
public class ProcessTest2 {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	final KieSession kSession = kContainer.newKieSession("ksession-process2");
        	
        	EntryPoint entryPoint1 = kSession.getEntryPoint("entryone");
        	
        	new Thread() {
                
                @Override
                public void run() {
                	System.out.println("fire until halt.");
                	kSession.fireUntilHalt();
                    System.out.println("halted.");
                }
            }.start();
        	
        	boolean decrese = false;
            for (int i = 0; i < 20; i++) {
            	
            	Message msg = new Message();
            	
            	if(i > 9 && decrese) {
            		// HELLO 메시지 감소.
            		msg.setStatus(Message.GOODBYE);
            	} else {
            		msg.setStatus(Message.HELLO);
            	}
                
                msg.setMessage(i + " message");
                kSession.insert(msg);
                
                kSession.startProcess("com.sample.bpmn.hello");
                
                System.out.println("inserted msg:" + i);
                try {
                    Thread.sleep(2000);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("--------------");
			}
        	
            System.out.println("halt.");
            kSession.halt();
            kSession.dispose();
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
