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
public class CEPDataConsumer extends BaseConsumer {
	
	public static final String CONSUMER_TAG = "CEPDataConsumer";
	
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
		
		try {
			Map<String, Object> teleMap = Utils.parseTelegram(telegram);
			
			ruleExecutor.execute(teleMap);
			
			sendAck(envelope);
		} catch (Exception e) {
			LOGGER.error(e.toString(), e);
		}
	}
	
	

}
//end of CepDataConsumer.java