package com.nicecredit.pilot.rule;

import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class AgendaEventListenerImpl extends DefaultAgendaEventListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AgendaEventListenerImpl.class);

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public AgendaEventListenerImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void beforeMatchFired(BeforeMatchFiredEvent event) {
		
		//Context.container.get().add(event.getMatch().getRule().getName());
		LOGGER.debug("Rule name : {}", event.getMatch().getRule().getName());
	}

	
}
//end of AgendaEventListenerImpl.java