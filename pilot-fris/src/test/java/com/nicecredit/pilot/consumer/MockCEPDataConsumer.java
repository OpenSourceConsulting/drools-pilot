package com.nicecredit.pilot.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nicecredit.pilot.db.TestResult;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class MockCEPDataConsumer extends CEPDataConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MockCEPDataConsumer.class);
	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param channel
	 */
	public MockCEPDataConsumer(Channel channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void sendAck(Envelope envelope) throws IOException {
		LOGGER.debug("send ack.-----------------------------");
	}

	@Override
	protected void sendResponse(TestResult result) {
		LOGGER.debug("send response.");
	}
	

}
//end of MockCEPDataConsumer.java