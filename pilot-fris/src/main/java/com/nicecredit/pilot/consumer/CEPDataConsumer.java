package com.nicecredit.pilot.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class CEPDataConsumer extends DefaultConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CEPDataConsumer.class);

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param channel
	 */
	public CEPDataConsumer(Channel channel) {
		super(channel);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws IOException {
		
		LOGGER.debug(new String(body));
		
		basicAck(envelope);
	}
	
	private void basicAck(Envelope envelope) throws IOException {
		long deliveryTag = envelope.getDeliveryTag();
        getChannel().basicAck(deliveryTag, false);
	}

}
//end of CepDataConsumer.java