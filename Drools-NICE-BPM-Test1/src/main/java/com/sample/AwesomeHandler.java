package com.sample;

import org.drools.core.process.instance.WorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;

public class AwesomeHandler implements WorkItemHandler {

     @Override
     public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
          System.out.println("Aborting...");
     }

     @Override
     public void executeWorkItem(WorkItem item, WorkItemManager mgr) {
          System.out.println("*** Executing Awesome Handler ***");
          mgr.completeWorkItem(item.getId(), null);
          System.out.println("*** Done ***");

     }

}
//end of AwesomeHandler.java
