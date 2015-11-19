package com.sample;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.runtime.process.WorkflowProcessInstance;

/**
 * This is a sample file to launch a process.
 */
public class RemoteProcessTest {

    public static final void main(String[] args) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-process");

            // start a new process instance
            ProcessInstance proc = kSession.startProcess("com.sample.bpmn.hello");
            Object var = ((WorkflowProcessInstance)proc).getVariable("output");
            System.out.println(var);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
