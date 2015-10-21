package com.bong;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.bong.DroolsTest.Message;

/**
 * This is a sample file to launch a process.
 */
public class ProcessTest {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-process");
        	System.out.println(kSession.getClass().getCanonicalName());
        	
        	Message message = new Message();
            message.setMessage("Hello World");
            message.setStatus(Message.HELLO);
            kSession.insert(message);

            // start a new process instance
            kSession.startProcess("com.sample.bpmn.hello");
            kSession.fireAllRules();
            
            System.out.println("-----------");
            System.out.println("result: " + message.getStatus() + ", " + message.getMessage());
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
