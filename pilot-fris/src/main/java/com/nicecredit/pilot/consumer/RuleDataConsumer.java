package com.nicecredit.pilot.consumer;

import java.io.IOException;

import com.nicecredit.pilot.rule.PilotRuleExecutor;
import com.nicecredit.pilot.rule.RuleExecutor;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * <pre>
 * Rule 처리를 위한 data consumer
 * </pre>
 * @author BongJin Kwon
 */
public class RuleDataConsumer extends DefaultConsumer {
	
	//private RuleExecutor ruleExecutor = new TestRuleExecutor();
	private RuleExecutor ruleExecutor = new PilotRuleExecutor();

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param channel
	 */
	public RuleDataConsumer(Channel channel) {
		super(channel);
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws IOException {
		
		//String routingKey = envelope.getRoutingKey();
        //String contentType = properties.getContentType();
        
		
        ruleExecutor.execute(new String(body));
   	 
        basicAck(envelope);
	}
	
	private void basicAck(Envelope envelope) throws IOException {
		long deliveryTag = envelope.getDeliveryTag();
        getChannel().basicAck(deliveryTag, false);
	}
	
}
//end of RuleConsumer.java