package com.nicecredit.pilot.rule;

import java.util.ArrayList;

import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.event.process.ProcessCompletedEvent;
import org.kie.api.event.process.ProcessEventListener;
import org.kie.api.event.process.ProcessNodeLeftEvent;
import org.kie.api.event.process.ProcessNodeTriggeredEvent;
import org.kie.api.event.process.ProcessStartedEvent;
import org.kie.api.event.process.ProcessVariableChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class ProcessEventListenerImpl implements ProcessEventListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessEventListenerImpl.class);

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public ProcessEventListenerImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.kie.api.event.process.ProcessEventListener#beforeProcessStarted(org.kie.api.event.process.ProcessStartedEvent)
	 */
	@Override
	public void beforeProcessStarted(ProcessStartedEvent event) {
		ProcessInstance process = event.getProcessInstance();
		
		//Context.container.set(new ArrayList<String>());
		LOGGER.debug("============= Process ID: {} start.", process.getProcessId());

	}

	/* (non-Javadoc)
	 * @see org.kie.api.event.process.ProcessEventListener#afterProcessStarted(org.kie.api.event.process.ProcessStartedEvent)
	 */
	@Override
	public void afterProcessStarted(ProcessStartedEvent event) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.kie.api.event.process.ProcessEventListener#beforeProcessCompleted(org.kie.api.event.process.ProcessCompletedEvent)
	 */
	@Override
	public void beforeProcessCompleted(ProcessCompletedEvent event) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.kie.api.event.process.ProcessEventListener#afterProcessCompleted(org.kie.api.event.process.ProcessCompletedEvent)
	 */
	@Override
	public void afterProcessCompleted(ProcessCompletedEvent event) {
		ProcessInstance process = event.getProcessInstance();
		
		//Context.container.remove();
		LOGGER.debug("============= Process ID: {} end.", process.getProcessId());
	}

	/* (non-Javadoc)
	 * @see org.kie.api.event.process.ProcessEventListener#beforeNodeTriggered(org.kie.api.event.process.ProcessNodeTriggeredEvent)
	 */
	@Override
	public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
		String processId = event.getProcessInstance().getProcessId();
		String nodeName = event.getNodeInstance().getNodeName();
		
		LOGGER.debug("[{}] Node : {}", processId, nodeName);
	}

	/* (non-Javadoc)
	 * @see org.kie.api.event.process.ProcessEventListener#afterNodeTriggered(org.kie.api.event.process.ProcessNodeTriggeredEvent)
	 */
	@Override
	public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.kie.api.event.process.ProcessEventListener#beforeNodeLeft(org.kie.api.event.process.ProcessNodeLeftEvent)
	 */
	@Override
	public void beforeNodeLeft(ProcessNodeLeftEvent event) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.kie.api.event.process.ProcessEventListener#afterNodeLeft(org.kie.api.event.process.ProcessNodeLeftEvent)
	 */
	@Override
	public void afterNodeLeft(ProcessNodeLeftEvent event) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.kie.api.event.process.ProcessEventListener#beforeVariableChanged(org.kie.api.event.process.ProcessVariableChangedEvent)
	 */
	@Override
	public void beforeVariableChanged(ProcessVariableChangedEvent event) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.kie.api.event.process.ProcessEventListener#afterVariableChanged(org.kie.api.event.process.ProcessVariableChangedEvent)
	 */
	@Override
	public void afterVariableChanged(ProcessVariableChangedEvent event) {
		// TODO Auto-generated method stub

	}

}
//end of ProcessEventListenerImpl.java