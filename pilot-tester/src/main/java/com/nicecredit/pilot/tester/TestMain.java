package com.nicecredit.pilot.tester;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class TestMain {

	public static void main(String[] args) {
		
		
		String exchangeName = "pilot_ex";
		String queueName = "pilot_queue1";
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
			channel.queueDeclare(queueName, true, false, false, null);
			channel.queueBind(queueName, exchangeName, routingKey);
			
			byte[] messageBodyBytes = "Hello, test!".getBytes();
			
			channel.basicPublish(exchangeName, routingKey, true,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    messageBodyBytes);
			
			System.out.println("send success.");
			
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