package com.nicecredit.pilot.consumer;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nicecredit.pilot.rule.CEPRuleExecutor;
import com.nicecredit.pilot.rule.RuleExecutor;
import com.nicecredit.pilot.util.Utils;
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
	
	private RuleExecutor ruleExecutor = new CEPRuleExecutor(); 

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
		
		
		String telegram = new String(body);
		LOGGER.debug("telegram:{}", telegram);
		
		Map<String, Object> teleMap = Utils.parseTelegram(telegram);
		
		ruleExecutor.execute(teleMap);
		
		basicAck(envelope);
	}
	
	private void basicAck(Envelope envelope) throws IOException {
		long deliveryTag = envelope.getDeliveryTag();
        getChannel().basicAck(deliveryTag, false);
	}

}
//end of CepDataConsumer.java