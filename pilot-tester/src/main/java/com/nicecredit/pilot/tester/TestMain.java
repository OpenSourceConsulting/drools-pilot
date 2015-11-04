package com.nicecredit.pilot.tester;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class TestMain {

	public static void main(String[] args) {
		
		
		String exchangeName = "pilot_ex";
		String queueName1 = "rule_queue";
		String queueName2 = "cep_queue";
		String routingKey = "routingKey1";
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("user1");
		factory.setPassword("user1");
		factory.setVirtualHost("/");
		factory.setHost("207.46.141.43");
		factory.setPort(5672);
		
		Connection conn = null;
		Channel channel = null;
		try {
			conn = factory.newConnection();
			channel = conn.createChannel();
			
			System.out.println("created channel.");
			
			channel.exchangeDeclare(exchangeName, "direct", true);
			channel.queueDeclare(queueName1, true, false, false, null);
			channel.queueDeclare(queueName2, true, false, false, null);
			channel.queueBind(queueName1, exchangeName, routingKey);
			channel.queueBind(queueName2, exchangeName, routingKey);
			
			int msgCount = 5;
			String telegram = "MATCH1000           7001683169          2834014   1  44740811124.111.131cdabcdefg123456789a                                                                                                                                                      0221938302  01090670957                      ";
			String telegram2 = "MATCH1000           7001683169          2834015   1  44740811124.111.131cdabcdefg123456789a                                                                                                                                                      0221938302  01090670957                      ";
			
			for (int i = 0; i < msgCount; i++) {
				//byte[] messageBodyBytes = "7".getBytes();
				byte[] messageBodyBytes = null;
				if (i < 4) {
					messageBodyBytes = telegram.getBytes();
				} else {
					messageBodyBytes = telegram2.getBytes();
				}
				
				channel.basicPublish(exchangeName, routingKey, true,
	                    MessageProperties.PERSISTENT_TEXT_PLAIN,
	                    messageBodyBytes);
			}
			
			System.out.println("send count : "+ msgCount);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e2) {
					// ignore.
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (IOException e2) {
					// ignore.
				}
			}
		}
		
		
	}

}
//end of TestMain.java