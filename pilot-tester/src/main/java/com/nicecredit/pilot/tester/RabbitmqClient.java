package com.nicecredit.pilot.tester;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * <pre>
 * 
 * </pre>
 * @author BongJin Kwon
 */
public class RabbitmqClient {
	
	private ConnectionFactory factory;
	private Connection conn;
	private Channel channel;
	
	private String exchangeName = "pilot_ex";

	/**
	 * <pre>
	 * 
	 * </pre>
	 */
	public RabbitmqClient() throws IOException {
		
		//String queueName1 = "rule_queue";
		//String queueName2 = "cep_queue";
		//String routingKey = "routingKey1";
		
		factory = new ConnectionFactory();
		factory.setUsername("user1");
		factory.setPassword("user1");
		factory.setVirtualHost("/");
		factory.setHost("207.46.141.43");
		factory.setPort(5672);
		
		connect();
	}
	
	public void sendMessage(String msg) throws IOException {
		String routingKey = null;
		byte[] messageBodyBytes = null;
		
		//	MATCH or REGIT
		routingKey = msg.substring(0, 5);
		messageBodyBytes = msg.getBytes();
		
		channel.basicPublish(exchangeName, routingKey, true,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                messageBodyBytes);
	}
	
	public void connect() throws IOException {
		
		close();
		
		conn = factory.newConnection();
		channel = conn.createChannel();
		
		/*
		channel.exchangeDeclare(exchangeName, "direct", true);
		channel.queueDeclare(queueName1, true, false, false, null);
		channel.queueDeclare(queueName2, true, false, false, null);
		channel.queueBind(queueName1, exchangeName, "MATCH");
		channel.queueBind(queueName2, exchangeName, "REGIT");
		*/
		
		System.out.println("rabbitmq connected & created channel.");
	}
	
	/**
	 * <pre>
	 * 응답 전문 수신 준비.
	 * - ResponseConsumer 를 참고하세요.
	 * </pre>
	 */
	public void basicConsume() throws IOException {
		String exName = "pilot_resp";
		String queueName = "resp_queue";
		
		channel.exchangeDeclare(exName, "direct", true);
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exName, "resp");
		
		boolean autoAck = true;
		
		String consumerTag = channel.basicConsume(queueName, autoAck, "TestResponseConsumer", new ResponseConsumer(channel));
		System.out.println(consumerTag + " wait for listening");
	}
	
	public void close() {
		if (channel != null && channel.isOpen()) {
			try {
				channel.close();
			} catch (IOException e2) {
				// ignore.
			}
			channel = null;
		}
		if (conn != null && conn.isOpen()) {
			try {
				conn.close();
			} catch (IOException e2) {
				// ignore.
			}
			conn = null;
		}
		System.out.println("rabbitmq closed!!");
	}

}
//end of RabbitmqClient.java