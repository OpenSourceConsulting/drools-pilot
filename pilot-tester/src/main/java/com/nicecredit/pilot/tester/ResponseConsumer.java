package com.nicecredit.pilot.tester;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * <pre>
 * 응답 전문을 출력한다.
 * </pre>
 * @author BongJin Kwon
 */
public class ResponseConsumer extends DefaultConsumer {

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param channel
	 */
	public ResponseConsumer(Channel channel) {
		super(channel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			BasicProperties properties, byte[] body) throws IOException {
		
		/*
		 * 응답 전문 print.
		 */
		System.out.println("response: " + new String(body));
	}
	

}
//end of ResponseConsumber.java