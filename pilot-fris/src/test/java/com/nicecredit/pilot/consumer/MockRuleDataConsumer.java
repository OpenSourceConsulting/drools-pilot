package com.nicecredit.pilot.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class MockRuleDataConsumer extends RuleDataConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MockRuleDataConsumer.class);

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param channel
	 */
	public MockRuleDataConsumer(Channel channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void sendAck(Envelope envelope) throws IOException {
		LOGGER.debug("send ack.");
	}

}
//end of MockRuleDataConsumer.java